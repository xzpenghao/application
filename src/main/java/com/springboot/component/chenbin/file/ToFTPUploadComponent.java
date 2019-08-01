package com.springboot.component.chenbin.file;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component("toFTPUploadComponent")
public class ToFTPUploadComponent {

    @Value("${webplus.ftpAddressBdc}")
    private String ftpAddress;
    @Value("${webplus.ftpPortBdc}")
    private String ftpPort;
    @Value("${webplus.ftpUsernameBdc}")
    private String ftpUsername;
    @Value("${webplus.ftpPasswordBdc}")
    private String ftpPassword;

    public boolean uploadFile(String ftpPath, InputStream input){
        boolean success = false;
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("GBK");
        try {
            int reply;
            ftp.connect(ftpAddress, Integer.parseInt(ftpPort));// 连接FTP服务器
            ftp.login(ftpUsername, ftpPassword);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("FTP服务器连接失败");
                ftp.disconnect();
                return success;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            String FTP_BASEPATH1 = ftpPath.substring(0,5);
            String FTP_BASEPATH2 = ftpPath.substring(0,8);
            String FTP_BASEPATH3 = ftpPath.substring(0,11);
            String originFileName = ftpPath.substring(12);
            ftp.makeDirectory(FTP_BASEPATH1);
            ftp.changeWorkingDirectory(FTP_BASEPATH1);
            ftp.makeDirectory(FTP_BASEPATH2);
            ftp.changeWorkingDirectory(FTP_BASEPATH2);
            ftp.makeDirectory(FTP_BASEPATH3);
            ftp.changeWorkingDirectory(FTP_BASEPATH3);
            ftp.storeFile(originFileName,input);
            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }

    public boolean uploadFile(String ftpPath, byte[] input){
        System.out.println("将要上传的地址为："+ftpPath+"，附件大小为："+input.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        return uploadFile(ftpPath,bais);
    }
}
