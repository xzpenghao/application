package com.springboot.popj.pub_data;

import com.springboot.util.TimeUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Sj_Info_Jyhtxx extends SJ_Information {
    private String contractId;                         //合同id
    private String contractNumber;                     //合同编号
    private String contractRecordNumber;               //交易合同备案号
    private String contractType;                       //商品房/存量房合同
    private String contractRecordTime;                 //合同备案时间
    private String contractSignTime;                   //合同签订时间
    private BigDecimal contractAmount;                     //合同金额
    private String isReal;                             //合同是否有效
    private String registrationNumber;       //契约受理号
    private String auditStatus;              //审核状态
    private String holdingDifferent;                                   //分别持证
    private String fundTrusteeship;                                    //资金托管
    private String paymentMethod;                                     //支付方式
    private String taxBurdenParty;                                   //税费承担方式
    private String deliveryMode;
    private String deliveryDays;                                      //交付天数
    private String deliveryDate;                                      //交付日期
    private String oldHouseCode;                                    //原房产户编码
    private String ext1;                               //扩展字段1
    private String ext2;                               //扩展字段2
    private String ext3;                               //扩展字段3

    private List<SJ_Bdc_Gl> glImmovableVoList;          //关联的不动产数据
    private List<SJ_Qlr_Gl> glHouseBuyerVoList;            //关联的购房人数据
    private List<SJ_Qlr_Gl> glHouseSellerVoList;            //关联的售房人数据
    private List<SJ_Qlr_Gl> glAgentVoList;                 //关联的代理人数据
    private List<SJ_Qlr_Gl> glAgentSellerVoList;                 //关联的卖方代理人数据

    private SJ_Jyht_Detail htDetail;                        //合同细节数据
    private SJ_Jyht_Bcywy htbcywy;                          //合同补充条款与违约说明

    private String registrationSubclass;                       //登记小类
    private String registrationReason;                         //登记原因

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getContractId() {
        return contractId;
    }


    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContractRecordNumber() {
        return contractRecordNumber;
    }

    public void setContractRecordNumber(String contractRecordNumber) {
        this.contractRecordNumber = contractRecordNumber;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractRecordTime() {
        return contractRecordTime;
    }

    public void setContractRecordTime(Date contractRecordTime) {
        this.contractRecordTime = TimeUtil.getTimeString(contractRecordTime);
    }

    public String getContractSignTime() {
        return contractSignTime;
    }

    public void setContractSignTime(Date contractSignTime) {
        this.contractSignTime = TimeUtil.getDateString(contractSignTime);
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public String getIsReal() {
        return isReal;
    }

    public void setIsReal(String isReal) {
        this.isReal = isReal;
    }

    public String getHoldingDifferent() {
        return holdingDifferent;
    }

    public void setHoldingDifferent(String holdingDifferent) {
        this.holdingDifferent = holdingDifferent;
    }

    public String getFundTrusteeship() {
        return fundTrusteeship;
    }

    public void setFundTrusteeship(String fundTrusteeship) {
        this.fundTrusteeship = fundTrusteeship;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTaxBurdenParty() {
        return taxBurdenParty;
    }

    public void setTaxBurdenParty(String taxBurdenParty) {
        this.taxBurdenParty = taxBurdenParty;
    }

    public String getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(String deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = TimeUtil.getDateString(deliveryDate);
    }

    public String getOldHouseCode() {
        return oldHouseCode;
    }

    public void setOldHouseCode(String oldHouseCode) {
        this.oldHouseCode = oldHouseCode;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public List<SJ_Bdc_Gl> getGlImmovableVoList() {
        return glImmovableVoList;
    }

    public void setGlImmovableVoList(List<SJ_Bdc_Gl> glImmovableVoList) {
        this.glImmovableVoList = glImmovableVoList;
    }

    public List<SJ_Qlr_Gl> getGlHouseBuyerVoList() {
        return glHouseBuyerVoList;
    }

    public void setGlHouseBuyerVoList(List<SJ_Qlr_Gl> glHouseBuyerVoList) {
        this.glHouseBuyerVoList = glHouseBuyerVoList;
    }

    public List<SJ_Qlr_Gl> getGlHouseSellerVoList() {
        return glHouseSellerVoList;
    }

    public void setGlHouseSellerVoList(List<SJ_Qlr_Gl> glHouseSellerVoList) {
        this.glHouseSellerVoList = glHouseSellerVoList;
    }

    public List<SJ_Qlr_Gl> getGlAgentVoList() {
        return glAgentVoList;
    }

    public void setGlAgentVoList(List<SJ_Qlr_Gl> glAgentVoList) {
        this.glAgentVoList = glAgentVoList;
    }

    public List<SJ_Qlr_Gl> getGlAgentSellerVoList() {
        return glAgentSellerVoList;
    }

    public void setGlAgentSellerVoList(List<SJ_Qlr_Gl> glAgentSellerVoList) {
        this.glAgentSellerVoList = glAgentSellerVoList;
    }

    public SJ_Jyht_Detail getHtDetail() {
        return htDetail;
    }

    public void setHtDetail(SJ_Jyht_Detail htDetail) {
        this.htDetail = htDetail;
    }

    public String getRegistrationSubclass() {
        return registrationSubclass;
    }

    public void setRegistrationSubclass(String registrationSubclass) {
        this.registrationSubclass = registrationSubclass;
    }

    public String getRegistrationReason() {
        return registrationReason;
    }

    public void setRegistrationReason(String registrationReason) {
        this.registrationReason = registrationReason;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public SJ_Jyht_Bcywy getHtbcywy() {
        return htbcywy;
    }

    public void setHtbcywy(SJ_Jyht_Bcywy htbcywy) {
        this.htbcywy = htbcywy;
    }
}
