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
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

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
            log.info("ftp连接验证成功！");
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
}
