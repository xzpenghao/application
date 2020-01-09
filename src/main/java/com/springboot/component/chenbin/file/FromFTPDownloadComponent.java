package com.springboot.component.chenbin.file;

import com.alibaba.fastjson.JSONObject;
import com.springboot.component.BdcFTPDownloadComponent;
import com.springboot.entity.SJ_Fjfile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component("fromFTPDownloadComponent")
public class FromFTPDownloadComponent {
    @Value("${webplus.ftpAddress}")
    private String ftpAddress;
    @Value("${webplus.ftpPort}")
    private String ftpPort;
    @Value("${webplus.ftpUsername}")
    private String ftpUsername;
    @Value("${webplus.ftpPassword}")
    private String ftpPassword;

    @Value("${webplus.ftpAddressBdc}")
    private String ftpAddressBdc;
    @Value("${webplus.ftpPortBdc}")
    private String ftpPortBdc;
    @Value("${webplus.ftpUsernameBdc}")
    private String ftpUsernameBdc;
    @Value("${webplus.ftpPasswordBdc}")
    private String ftpPasswordBdc;

    @Autowired
    private BdcFTPDownloadComponent bdcFTPDownloadComponent;

    public byte[] downloadFile(String ftpPath,String from_){
        String address = ftpAddress;
        String port = ftpPort;
        String username = ftpUsername;
        String password = ftpPassword;
        if(from_!=null && "bdc".equals(from_)){
            address = ftpAddressBdc;
            port = ftpPortBdc;
            username = ftpUsernameBdc;
            password = ftpPasswordBdc;
        }
        String path = "";
        String fileName = "";
        if(ftpPath.contains("\\"))
            ftpPath = ftpPath.replaceAll("\\\\","/");
        if(!ftpPath.startsWith("/"))
            ftpPath = "/"+ftpPath;
        if(ftpPath.lastIndexOf("/")>0) {
            path = ftpPath.substring(0, ftpPath.lastIndexOf("/"));
            fileName = ftpPath.substring(ftpPath.lastIndexOf("/")+1);
        }else if(ftpPath.length()>1){
            fileName = ftpPath.substring(1);
        }
        System.out.println("FTP path: "+path);
        System.out.println("fileName: "+fileName);
        return bdcFTPDownloadComponent.downFile(path,fileName,null , address, port, username, password);
    }

    public boolean downFile(String remotePath, String fileName, SJ_Fjfile fj) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(ftpAddress, Integer.parseInt(ftpPort));
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(ftpUsername, ftpPassword);// 登录
            ftp.enterLocalPassiveMode();      //被动模式
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            System.out.println(remotePath + "   " + fileName);
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            System.out.println("path:" + remotePath + ",name:" + fileName + ",fj:" + JSONObject.toJSONString(fj));
            //下载指定文件
            InputStream is = ftp.retrieveFileStream(new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(is, out);
            byte[] bs = out.toByteArray();
            int length = bs.length;
            System.out.println("附件字节长度：" + length);
            fj.setFileSize(Integer.toString(length));
            fj.setFileContent(bs);
            out.close();
            is.close();
            System.out.println("流操作完毕");
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
}
