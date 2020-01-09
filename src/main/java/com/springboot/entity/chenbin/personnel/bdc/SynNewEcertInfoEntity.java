package com.springboot.entity.chenbin.personnel.bdc;

import java.io.Serializable;

public class SynNewEcertInfoEntity implements Serializable {
    private String slbh;
    private String certificateId;
    private String certificateType;
    private String ftpPath;
    private boolean useFtp;

    public String getSlbh() {
        return slbh;
    }

    public void setSlbh(String slbh) {
        this.slbh = slbh;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public boolean getUseFtp() {
        return useFtp;
    }

    public void setUseFtp(boolean useFtp) {
        this.useFtp = useFtp;
    }
}
