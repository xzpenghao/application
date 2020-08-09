package com.springboot.component.chenbin.file;

import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Maps;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.newPlat.settingTerm.FtpSettingTerm;
import com.springboot.entity.newPlat.settingTerm.FtpSettings;
import com.springboot.util.DateUtils;
import com.springboot.util.StrUtil;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component("toFTPUploadComponent")
@Slf4j
public class ToFTPUploadComponent {
    @Autowired
    private FtpSettings ftpSettings;


    private static String LOCAL_CHARSET = "GBK";


    public  String uploadFile(MultipartFile uploadFile){
        //链接
        FTPClient ftpClient = new FTPClient();
        boolean returnValue  = false;
        String fileName=null;
        String hz=null;
        //路径年/月/日/entryId名称
        String path=DateUtils.getNowYear()+File.separator+DateUtils.getNowMonth()+ File.separator+DateUtils.getNowDay();
        try {
            log.info("进入附件处理");
            int reply;
            log.info("连接ftp服务器");
            FtpSettingTerm ftpSettingTerm = ftpSettings.gainTermByKey("ycsl");
            ftpClient.connect(ftpSettingTerm.getFtpAddress(), Integer.parseInt(ftpSettingTerm.getFtpPort()));// 连接FTP服务器
            log.info("登录ftp用户");
            ftpClient.login(ftpSettingTerm.getFtpUsername(), ftpSettingTerm.getFtpPassword());// 登录
            ftpClient.enterLocalPassiveMode();
            //如果没有需求上传图片的话还ok，
            /**
             * 确认应答状态码是否正确完成响应
             * 凡是 2开头的 isPositiveCompletion 都会返回 true，因为它底层判断是：
             * return (reply >= 200 && reply < 300);
             */
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return null;
            }
            // 但是要是传图片，就需要设置一下文件类型为二进制
            // 这样上传的图片才不会报错（记得我的错误貌似是什么ASCII编码什么的。。）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(ftpClient.BINARY_FILE_TYPE);
//            ftpClient.setBufferSize(10240*10240);
            //创建目录
            mkDirs(ftpClient,path);//创建目录
            ftpClient.changeWorkingDirectory("/" + path);//创建完了目录需要将当前工作目录切换过来，然后直接在下面创建文件
            log.info("ftp:"+ftpClient.changeWorkingDirectory("/" + path));
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
            }
            hz=uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".")+1);
            log.info("hz"+hz);
            fileName=IDUtil.getFinstId()+"."+hz;
            log.info("fileName"+fileName);
            FTPFile[] fs = ftpClient.listFiles(fileName);
            if (fs.length == 0) {
                log.info("this file not exist ftp");
            } else if (fs.length == 1) {
                log.info("this file exist ftp");
                ftpClient.deleteFile(fs[0].getName());
            }
            InputStream is = uploadFile.getInputStream();
            log.info("is"+is);
            returnValue = ftpClient.storeFile(new String(fileName.getBytes("UTF-8"),"iso-8859-1"),is);
            log.info("resultValue:"+returnValue);
            ftpClient.logout();
            is.close();
        } catch (Exception e) {
            returnValue=false;
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
                if (returnValue==false){
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (returnValue==true){
            return path+File.separator+fileName;
        }
        return null;
    }









    public boolean uploadFile(String ftpPath, InputStream input) {
        String path_temp = ftpPath;
        boolean success = false;
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("GBK");
        try {
            int reply;
            FtpSettingTerm ftpSettingTerm = ftpSettings.gainTermByKey("bdc");
            ftp.connect(ftpSettingTerm.getFtpAddress(), Integer.parseInt(ftpSettingTerm.getFtpPort()));// 连接FTP服务器
            ftp.login(ftpSettingTerm.getFtpUsername(), ftpSettingTerm.getFtpPassword());// 登录
            ftp.enterLocalPassiveMode();      //被动模式
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("FTP服务器连接失败");
                ftp.disconnect();
                return success;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            if(StringUtils.isNotBlank(path_temp) && path_temp.contains("\\")){
                path_temp = path_temp.replaceAll("\\\\","/");
            }
            if(StringUtils.isBlank(path_temp)){
                throw new ZtgeoBizException("FTP路径为空");
            }
            String originFileName = StrUtil.getFTPFileNameByFTPPath(path_temp);
            path_temp = StrUtil.getFTPRemotePathByFTPPath(path_temp);
            mkDirs(ftp,path_temp);
//            String FTP_BASEPATH1 = ftpPath.substring(0, 5);
//            String FTP_BASEPATH2 = ftpPath.substring(0, 8);
//            String FTP_BASEPATH3 = ftpPath.substring(0, 11);
//            String originFileName = ftpPath.substring(12);
//            ftp.makeDirectory(FTP_BASEPATH1);
//            ftp.changeWorkingDirectory(FTP_BASEPATH1);
//            ftp.makeDirectory(FTP_BASEPATH2);
//            ftp.changeWorkingDirectory(FTP_BASEPATH2);
//            ftp.makeDirectory(FTP_BASEPATH3);
//            ftp.changeWorkingDirectory(FTP_BASEPATH3);
            boolean flag = ftp.storeFile(originFileName, input);
            input.close();
            ftp.logout();
            success = true;
            log.info("success登记平台"+flag);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("附件上传出现IO异常，详细信息为："+e);
        } catch (Exception e){
            e.printStackTrace();
            log.error("附件上传出现其它运行时异常，详细信息为："+e);
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }

    public boolean uploadFile(String ftpPath, byte[] input) {
        System.out.println("将要上传的地址为：" + ftpPath + "，附件大小为：" + input.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        return uploadFile(ftpPath, bais);
    }

    public String ycslUpload(byte[] bytes, String path, String fileName, String from_){
        FtpSettingTerm ftpSettingTermy = ftpSettings.gainTermByKey("ycsl");
        String address = ftpSettingTermy.getFtpAddress();
        String port = ftpSettingTermy.getFtpPort();
        String username = ftpSettingTermy.getFtpUsername();
        String password = ftpSettingTermy.getFtpPassword();
        if(from_!=null && "bdc".equals(from_)){
            FtpSettingTerm ftpSettingTerm = ftpSettings.gainTermByKey("bdc");
            address = ftpSettingTerm.getFtpAddress();
            port = ftpSettingTerm.getFtpPort();
            username = ftpSettingTerm.getFtpUsername();
            password = ftpSettingTerm.getFtpPassword();
        }
        Object r = ycslUpload(
                bytes,fileName,fileName.contains(".")?fileName.substring(fileName.lastIndexOf(".")):"",path,
                address,port,username,password
        );
        if(r==null){
            throw new ZtgeoBizException("电子证书上传失败");
        }
        return path+"/"+fileName;
    }

    public Object ycslUpload(byte[] bytes, String fileName, String Type,String path,String yftpAddress,String yftpPort,String yftpUsername,String yftpPassword) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return upYcslloadFile(bais, fileName, Type,path,yftpAddress,yftpPort,yftpUsername,yftpPassword);
    }


    /**
     * ftp服务器上
     *
     * @param
     * @return
     */
    public Object upYcslloadFile(InputStream input, String fileName, String fileType,String path,String yftpAddress,String yftpPort,String yftpUsername,String yftpPassword) {
        System.out.println(fileName + "\t" + fileType);
        Map<String, Object> map = Maps.newHashMap();
        FTPClient ftpClient = new FTPClient();
        boolean returnValue = false;
        //路径年/月/日/entryId名称
//        String path = ;
        try {
            log.info("进入附件处理");
            int reply;
            log.info("连接ftp服务器");
            ftpClient.connect(yftpAddress, Integer.parseInt(yftpPort));// 连接FTP服务器
            log.info("登录ftp用户");
            ftpClient.login(yftpUsername, yftpPassword);// 登录
            /**
             * 确认应答状态码是否正确完成响应
             * 凡是 2开头的 isPositiveCompletion 都会返回 true，因为它底层判断是：
             * return (reply >= 200 && reply < 300);
             */
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return null;
            }
            log.info("reply"+reply);
            ftpClient.enterLocalPassiveMode();
            //如果没有需求上传图片的话还ok，
            // 但是要是传图片，就需要设置一下文件类型为二进制
            // 这样上传的图片才不会报错（记得我的错误貌似是什么ASCII编码什么的。。）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(ftpClient.BINARY_FILE_TYPE);
            log.info("创建目录"+path);
            mkDirs(ftpClient,path);
            //创建目录
//            ftpClient.changeWorkingDirectory("/" + path);//创建完了目录需要将当前工作目录切换过来，然后直接在下面创建文件
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
            }
            log.info("fileName"+fileName);
            /*log.info("printWorkingDirectory"+ftpClient.printWorkingDirectory());*/
            returnValue = ftpClient.storeFile(fileName, input);
            log.info("s:"+returnValue);
            log.info("jjj");
            ftpClient.logout();
            input.close();
        } catch (Exception e) {
            returnValue = false;
            e.printStackTrace();
            System.out.print(e.getMessage());
        }
        if (returnValue == true) {
            map.put("fileName", fileName);
            map.put("path", path);
//            return path+File.separator+fileName;
            return map;
        }
        return null;
    }

    /**
     * ------------------------------以下为私有方法------------------------------
     */
    // 创建文件夹
    private void mkDirs(FTPClient client, String p) throws Exception {
        if (null == p) {
            return;
        }
        if(p.contains(File.separator)){
            p = p.replaceAll("\\\\","/");
        }
        System.out.println("give me ptf:"+p);
        if (p != null && !"".equals(p) && !"/".equals(p)) {
            String ps = "/";
            for (int i = 0; i < p.split("/").length; i++) {
                ps += p.split("/")[i] + "/";
                if (!isDirExist(client, ps)) {
                    client.makeDirectory(ps);// 创建目录
                    client.changeWorkingDirectory(ps);// 进入创建的目录
                }
            }
        }
    }

    // 判断文件夹是否存在
    private static boolean isDirExist(FTPClient client, String dir) {
        boolean flag;
        try {
            flag = client.changeWorkingDirectory(dir);
        } catch (Exception e) {
            return false;
        }
        return flag;
    }



    public boolean uploadFileBDC(String pathFold,String fileName, String fileType, String fileName_ftp,InputStream is) {
        log.info("进入"+fileName+"附件处理");
        log.info("接收到的附件保存路径为："+pathFold);
        boolean success = false;;
        // 上传文件
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("UTF-8");
        try {
//            String fileName_ftp = UUID.randomUUID().toString()+"."+fileType;
//            String fileName_ftp = "";
//            if(!fileName.contains(fileType)) {
//                fileName_ftp = new String((fileName + "." + fileType).getBytes(),"UTF-8");
//            }else{
//                fileName_ftp = new String(fileName.getBytes(),"UTF-8");
//            }
            //路径年/月/日/entryId名称
            String mulu = pathFold;
            FtpSettingTerm ftpSettingTermy = ftpSettings.gainTermByKey("ycsl");
            ftp.connect(ftpSettingTermy.getFtpAddress(), Integer.valueOf(ftpSettingTermy.getFtpPort()));
            ftp.login(ftpSettingTermy.getFtpUsername(), ftpSettingTermy.getFtpPassword());
            ftp.enterLocalPassiveMode();
            int reply = ftp.getReplyCode();
            log.info("FTP连接返回：" + reply);
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
//            ftp.setBufferSize(10240 * 10240);
            boolean flag = ftp.changeWorkingDirectory(mulu);
            if (!flag) {
                mkDir(mulu,ftp);
                ftp.changeWorkingDirectory(mulu);
            }
//            ftp.changeWorkingDirectory(mulu);
            success = ftp.storeFile(fileName_ftp, is);
            is.close();
            ftp.logout();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FTP上传文件异常！" + e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return success;
    }

    public static boolean mkDir(String ftpPath,FTPClient ftpClient) {
        try {
            // 将路径中的斜杠统一
            char[] chars = ftpPath.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpPath = sbStr.toString();
            // System.out.println("ftpPath:" + ftpPath);
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient.makeDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
                ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftpClient.makeDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
