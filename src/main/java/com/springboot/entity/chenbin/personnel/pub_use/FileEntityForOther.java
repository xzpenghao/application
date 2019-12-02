package com.springboot.entity.chenbin.personnel.pub_use;

import java.io.Serializable;

public class FileEntityForOther implements Serializable {
    private String fileId;
    private String fileName;
    private String fileExt;
    private String ftpPath;
    private String rqDepart;
    private String fileBase64;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getRqDepart() {
        return rqDepart;
    }

    public void setRqDepart(String rqDepart) {
        this.rqDepart = rqDepart;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }
}
