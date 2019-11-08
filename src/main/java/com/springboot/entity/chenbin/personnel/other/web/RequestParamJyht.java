package com.springboot.entity.chenbin.personnel.other.web;

import com.springboot.popj.pub_data.SJ_Jyht_Detail;

import java.io.Serializable;
import java.util.List;

public class RequestParamJyht implements Serializable {
    private String contractType;                                    //合同类型
    private String contractAmount;                                  //合同金额
    private String holdingDifferent;                                //是否分别持证
    private String fundTrusteeship;                                 //是否资金托管
    private String oldHouseCode;                                    //原房产户编码
    private String paymentMethod;                                   //支付方式
    private String taxBurdenParty;                                  //税费承担方式
    private String deliveryDays;                                    //交付天数
    private String deliveryDate;                                    //交付日期
    private SJ_Jyht_Detail htDetail;                        //合同细节数据
    private List<RequestParamQlr> houseBuyerVoList;
    private List<RequestParamQlr> houseSellerVoList;

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(String contractAmount) {
        this.contractAmount = contractAmount;
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

    public String getOldHouseCode() {
        return oldHouseCode;
    }

    public void setOldHouseCode(String oldHouseCode) {
        this.oldHouseCode = oldHouseCode;
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

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public SJ_Jyht_Detail getHtDetail() {
        return htDetail;
    }

    public void setHtDetail(SJ_Jyht_Detail htDetail) {
        this.htDetail = htDetail;
    }

    public List<RequestParamQlr> getHouseBuyerVoList() {
        return houseBuyerVoList;
    }

    public void setHouseBuyerVoList(List<RequestParamQlr> houseBuyerVoList) {
        this.houseBuyerVoList = houseBuyerVoList;
    }

    public List<RequestParamQlr> getHouseSellerVoList() {
        return houseSellerVoList;
    }

    public void setHouseSellerVoList(List<RequestParamQlr> houseSellerVoList) {
        this.houseSellerVoList = houseSellerVoList;
    }
}
