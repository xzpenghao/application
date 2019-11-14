package com.springboot.entity.chenbin.personnel.other.web;

import java.io.Serializable;

public class ImmovableFile implements Serializable {

    private String fileName;
    private String fileType;
    private String fileSize;
    private String fileAdress;
    private String pName;
    private String fileSequence;
    private String fileBase64;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileAdress() {
        return fileAdress;
    }

    public void setFileAdress(String fileAdress) {
        this.fileAdress = fileAdress;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getFileSequence() {
        return fileSequence;
    }

    public void setFileSequence(String fileSequence) {
        this.fileSequence = fileSequence;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }
}
