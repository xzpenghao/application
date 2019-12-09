package com.springboot.popj.registration;

import java.util.List;

public class TransferBizInfo {

    private String realEstateId;//不动产权证号
    private String landCertificate;//土地证号
    private String htbh;//合同编号
    private String djdl;
    private String dl_val;
    private String transferReason;//转移原因
    private String registerSubType;//登记小类
    private String commonWay;//共有方式
    private List<QlrGlMortgator> obligeeInfoVoList; //不动产权利人列表
    private List<WtdlrGlMortgator> obligeeAgentInfoVoList; //权利人代理人信息列表
    private List<WtdlrGlMortgator> salerAgentInfoVoList; //权利人代理人信息列表


    public String getRegisterSubType() {
        return registerSubType;
    }

    public void setRegisterSubType(String registerSubType) {
        this.registerSubType = registerSubType;
    }

    public String getTransferReason() {
        return transferReason;
    }

    public void setTransferReason(String transferReason) {
        this.transferReason = transferReason;
    }

    public List<QlrGlMortgator> getObligeeInfoVoList() {
        return obligeeInfoVoList;
    }

    public void setObligeeInfoVoList(List<QlrGlMortgator> obligeeInfoVoList) {
        this.obligeeInfoVoList = obligeeInfoVoList;
    }

    public List<WtdlrGlMortgator> getObligeeAgentInfoVoList() {
        return obligeeAgentInfoVoList;
    }

    public void setObligeeAgentInfoVoList(List<WtdlrGlMortgator> obligeeAgentInfoVoList) {
        this.obligeeAgentInfoVoList = obligeeAgentInfoVoList;
    }

    public List<WtdlrGlMortgator> getSalerAgentInfoVoList() {
        return salerAgentInfoVoList;
    }

    public void setSalerAgentInfoVoList(List<WtdlrGlMortgator> salerAgentInfoVoList) {
        this.salerAgentInfoVoList = salerAgentInfoVoList;
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

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getDjdl() {
        return djdl;
    }

    public void setDjdl(String djdl) {
        this.djdl = djdl;
    }

    public String getDl_val() {
        return dl_val;
    }

    public void setDl_val(String dl_val) {
        this.dl_val = dl_val;
    }

    public String getCommonWay() {
        return commonWay;
    }

    public void setCommonWay(String commonWay) {
        this.commonWay = commonWay;
    }
}
