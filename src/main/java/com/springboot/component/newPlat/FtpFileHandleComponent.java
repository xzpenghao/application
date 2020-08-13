package com.springboot.component.newPlat;

import com.alibaba.fastjson.JSONObject;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.newPlat.result.FtpFileDownResult;
import com.springboot.entity.newPlat.settingTerm.FtpSettingTerm;
import com.springboot.entity.newPlat.settingTerm.FtpSettings;
import com.springboot.util.chenbin.ErrorDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.FTP_USE_ENV_UNIX;
import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.FTP_USE_MODE_ACTIVE;

/**
 * @author chenb
 * @version 2020/8/12
 * description：FTP附件处理Component
 */
@Slf4j
@Component
public class FtpFileHandleComponent {

    @Autowired
    private FtpSettings ftpSettings;

    /**
     * 描述：FTP附件下载
     * 作者：chenb
     * 日期：2020/8/13
     * 参数：[key, remotePath, fileName]
     * 返回：FtpFileDownResult
     * 更新记录：更新人：{}，更新日期：{}
     */
    public FtpFileDownResult downFile(String key, String remotePath, String fileName) {
        //FTP设置获取
        FtpSettingTerm ftpSetting = ftpSettings.gainTermByKey(key);
        //FTP设置是否成功获取
        if(ftpSetting==null){
            throw new ZtgeoBizException("【"+key+"】未找到对应ftp配置");
        }
        //声明返回对象
        FtpFileDownResult result = new FtpFileDownResult();
        //定义ftp client对象
        FTPClient ftp = new FTPClient();

        try {
            int reply;
            log.info("附件下载路径包括："+remotePath + "文件名：" + fileName);
            // 连接ftp
            ftp.connect(ftpSetting.getFtpAddress(), Integer.parseInt(ftpSetting.getFtpPort()));
            // 登录ftp用户
            ftp.login(ftpSetting.getFtpUsername(), ftpSetting.getFtpPassword());
            // 预处理路径标准
            if(remotePath.contains("\\")){
                remotePath = remotePath.replaceAll("\\\\","/");
            }
            // 预连接标识获取
            reply = ftp.getReplyCode();
            // 预连接标识判断连接结果
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result.fail("尝试FTP【"+ftpSetting.getFtpAddress()+"】连接失败");
            }
            log.info("ftp连接验证成功！");
            // 模式选择
            if(FTP_USE_MODE_ACTIVE.equals(ftpSetting.getFtpTransMode())){
                ftp.enterLocalActiveMode();      //主动模式
            }else {
                ftp.enterLocalPassiveMode();     //被动模式
            }
            // 切换工作目录
            ftp.changeWorkingDirectory(remotePath);
            log.info("path:" + remotePath + ",name:" + fileName );
            // 下载指定文件,写流信息入字节数组
            InputStream is = ftp.retrieveFileStream(new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(is, out);
            byte[] bs = out.toByteArray();
            int length = bs.length;
            log.info("附件字节长度：" + length);
            out.close();
            is.close();
            log.info("流操作完毕");
            ftp.logout();
            // 返回成功数据
            return result.success(Integer.toString(length),bs);
        } catch (IOException e) {
            log.error("附件下载时出现异常"+ ErrorDealUtil.getErrorInfo(e));
            return result.fail("从【"+ftpSetting.getFtpAddress()+"】下载文件失败，错误信息："+e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
    }

    /**
     * 描述：上传附件到FTP
     * 作者：chenb
     * 日期：2020/8/13
     * 参数：[key, remothPath, fileName, input]
     * 返回：boolean
     * 更新记录：更新人：{}，更新日期：{}
     */
    public boolean uploadFile(String key,String remothPath, String fileName, InputStream input) {
        // FTP设置获取
        FtpSettingTerm ftpSetting = ftpSettings.gainTermByKey(key);
        // FTP设置是否成功获取
        if(ftpSetting==null){
            throw new ZtgeoBizException("【"+key+"】未找到对应ftp配置");
        }
        // 声明返回结果
        boolean success = false;
        try {
            // 创建FTP客户端对象
            FTPClient ftp = new FTPClient();
            ftp.setControlEncoding("UTF-8");

            // 连接FTP地址和端口(握手阶段)
            ftp.connect(ftpSetting.getFtpAddress(), Integer.parseInt(ftpSetting.getFtpPort()));
            ftp.login(ftpSetting.getFtpUsername(), ftpSetting.getFtpPassword());
            int reply = ftp.getReplyCode();
            log.info("FTP连接返回：" + reply);
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                log.info("尝试FTP【"+ftpSetting.getFtpAddress()+"】连接失败");
                return false;
            }
            log.info("ftp连接验证成功！");

            //设置操作系统
            if (FTP_USE_ENV_UNIX.equals(ftpSetting.getEnv())) {
                FTPClientConfig conf = new FTPClientConfig("UNIX");
                ftp.configure(conf);
            }

            // 模式选择
            if(FTP_USE_MODE_ACTIVE.equals(ftpSetting.getFtpTransMode())){
                ftp.enterLocalActiveMode();      //主动模式
            }else {
                ftp.enterLocalPassiveMode();     //被动模式
            }

            //文件类型与缓冲区设置
            ftp.setFileType(2);
            ftp.setBufferSize(1024 * 1024);

            //检查+创建+切换工作目录
            mkDirs(ftp, remothPath);
            //核验并检查
            ftp.changeWorkingDirectory(remothPath);
            //上传+关闭输入流+登出FTP
            boolean uploadFlag = ftp.storeFile(fileName, input);
            input.close();
            ftp.logout();
            //赋值上传结果
            if (uploadFlag) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZtgeoBizException("FTP【"+ftpSetting.getFtpAddress()+"】上传文件异常！" + e.getMessage());
        }
        return success;
    }

    /**
     * ------------------------------以下为私有方法------------------------------
     */
    /**
     * 描述：创建并切换工作区
     * 作者：chenb
     * 日期：2020/8/13
     * 参数：[client, p]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    private void mkDirs(FTPClient client, String p) throws Exception {
        if (null == p) {
            return;
        }

        if (StringUtils.isNotBlank(p) && !"/".equals(p)) {
            String ps = "";
            String []jps = p.split("/");
            for (int i = 0; i < jps.length; i++) {
                ps += jps[i] + "/";
                if (!isDirExist(client, ps)) {
                    client.makeDirectory(ps);// 创建目录
                    client.changeWorkingDirectory(ps);// 进入创建的目录
                }
            }
        }
    }

    /**
     * 描述：判断工作区文件夹是否存在
     * 作者：chenb
     * 日期：2020/8/13
     * 参数：[client, dir]
     * 返回：boolean
     * 更新记录：更新人：{}，更新日期：{}
     */
    private static boolean isDirExist(FTPClient client, String dir) {
        boolean flag;
        try {
            flag = client.changeWorkingDirectory(dir);
        } catch (Exception e) {
            return false;
        }
        return flag;
    }
}
