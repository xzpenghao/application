package com.springboot.popj.pub_data;

import com.springboot.util.TimeUtil;

import java.io.Serializable;
import java.util.Date;

public class SJ_Book_Cert implements Serializable {
    private String id;
    private String infoId;
    private String receiptNumber;
    private String serviceCode;
    private String eCertType;
    private String initTime;
    private String pdfPath;
    private String ftpPath;
    private String isSaveFile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String geteCertType() {
        return eCertType;
    }

    public void seteCertType(String eCertType) {
        this.eCertType = eCertType;
    }

    public String getInitTime() {
        return initTime;
    }

    public void setInitTime(Date initTime) {
        this.initTime = TimeUtil.getTimeString(initTime);
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getIsSaveFile() {
        return isSaveFile;
    }

    public void setIsSaveFile(String isSaveFile) {
        this.isSaveFile = isSaveFile;
    }
}
