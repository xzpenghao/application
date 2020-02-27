package com.springboot.popj.registration;

/**
 * 换证登记业务信息
 */
public class ReplaceBizInfo {
    private String registerSubType;//登记小类
    private String registerReason;//登记原因
    private String certificateType;//证书类型
    private String realEstateId;//不动产证号
    private String landCertificate;//土地证号

    public String getRegisterSubType() {
        return registerSubType;
    }

    public void setRegisterSubType(String registerSubType) {
        this.registerSubType = registerSubType;
    }

    public String getRegisterReason() {
        return registerReason;
    }

    public void setRegisterReason(String registerReason) {
        this.registerReason = registerReason;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(String realEstateId) {
        this.realEstateId = realEstateId;
    }

    public String getLandCertificate() {
        return landCertificate;
    }

    public void setLandCertificate(String landCertificate) {
        this.landCertificate = landCertificate;
    }
}
