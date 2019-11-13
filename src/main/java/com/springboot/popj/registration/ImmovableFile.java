package com.springboot.popj.registration;

import java.io.Serializable;

public class ImmovableFile implements Serializable {
    private String fileName;
    private String fileType;
    private String fileSize;
    private String fileAdress;
    private String pName;
    private String fileSequence;

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

    public String getFileAdress() {
        return fileAdress;
    }

    public void setFileAdress(String fileAdress) {
        this.fileAdress = fileAdress;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSequence() {
        return fileSequence;
    }

    public void setFileSequence(String fileSequence) {
        this.fileSequence = fileSequence;
    }
}
