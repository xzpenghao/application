package com.springboot.popj.pub_data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

public class SJ_Jyht_Detail implements Serializable {
    private String infoId                                   ;				//对应交易的关联主键
    private String doesIncludeHouseProperties             ;              //是否包含房产附属设施
    private String houseProperties                          ;              //房产附属设施
    private String isHire                                   ;              //是否出租
    private String hireInstructions                         ;              //出租说明
    private String fundOpenBank                            ;              //资金监管开户行
    private String fundAccount                              ;              //资金监管账户
    private String fundBuyerPaysContent                   ;              //资金监管买方支付内容
    private String fullPaymentDate                         ;              //全款付款日期
    private String stagePaymentDate1                       ;              //分期付款日期1
    private BigDecimal stagePaymentAmount1                     ;              //分期付款金额1
    private String stagePaymentDate2                       ;              //分期付款日期2
    private BigDecimal stagePaymentAmount2                     ;              //分期付款金额2
    private String stagePaymentDate3                       ;              //分期付款日期3
    private BigDecimal stagePaymentAmount3                     ;              //分期付款金额3
    private String loanMode                                 ;              //贷款方式
    private String firstPaymentDate                        ;              //首付款日期
    private BigDecimal firstPaymentAmount                      ;              //首付款金额
    private String loanApplyDate                     ;              //贷款申请日期
    private String paymentContents                          ;              //付款内容（支付方式4）
    private String buyerPays                                ;              //买房支付（支付方式4）

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getDoesIncludeHouseProperties() {
        return doesIncludeHouseProperties;
    }

    public void setDoesIncludeHouseProperties(String doesIncludeHouseProperties) {
        this.doesIncludeHouseProperties = doesIncludeHouseProperties;
    }

    public String getHouseProperties() {
        return houseProperties;
    }

    public void setHouseProperties(String houseProperties) {
        this.houseProperties = houseProperties;
    }

    public String getIsHire() {
        return isHire;
    }

    public void setIsHire(String isHire) {
        this.isHire = isHire;
    }

    public String getHireInstructions() {
        return hireInstructions;
    }

    public void setHireInstructions(String hireInstructions) {
        this.hireInstructions = hireInstructions;
    }

    public String getFundOpenBank() {
        return fundOpenBank;
    }

    public void setFundOpenBank(String fundOpenBank) {
        this.fundOpenBank = fundOpenBank;
    }

    public String getFundAccount() {
        return fundAccount;
    }

    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
    }

    public String getFundBuyerPaysContent() {
        return fundBuyerPaysContent;
    }

    public void setFundBuyerPaysContent(String fundBuyerPaysContent) {
        this.fundBuyerPaysContent = fundBuyerPaysContent;
    }

    public String getFullPaymentDate() {
        return fullPaymentDate;
    }

    public void setFullPaymentDate(String fullPaymentDate) {
        if(StringUtils.isNotBlank(fullPaymentDate) && fullPaymentDate.length()==19){
            this.fullPaymentDate = fullPaymentDate.substring(0, 10);
        }else {
            this.fullPaymentDate = fullPaymentDate;
        }
    }

    public String getStagePaymentDate1() {
        return stagePaymentDate1;
    }

    public void setStagePaymentDate1(String stagePaymentDate1) {
        this.stagePaymentDate1 = stagePaymentDate1;
    }

    public BigDecimal getStagePaymentAmount1() {
        return stagePaymentAmount1;
    }

    public void setStagePaymentAmount1(BigDecimal stagePaymentAmount1) {
        this.stagePaymentAmount1 = stagePaymentAmount1;
    }

    public String getStagePaymentDate2() {
        return stagePaymentDate2;
    }

    public void setStagePaymentDate2(String stagePaymentDate2) {
        this.stagePaymentDate2 = stagePaymentDate2;
    }

    public BigDecimal getStagePaymentAmount2() {
        return stagePaymentAmount2;
    }

    public void setStagePaymentAmount2(BigDecimal stagePaymentAmount2) {
        this.stagePaymentAmount2 = stagePaymentAmount2;
    }

    public String getStagePaymentDate3() {
        return stagePaymentDate3;
    }

    public void setStagePaymentDate3(String stagePaymentDate3) {
        this.stagePaymentDate3 = stagePaymentDate3;
    }

    public BigDecimal getStagePaymentAmount3() {
        return stagePaymentAmount3;
    }

    public void setStagePaymentAmount3(BigDecimal stagePaymentAmount3) {
        this.stagePaymentAmount3 = stagePaymentAmount3;
    }

    public String getLoanMode() {
        return loanMode;
    }

    public void setLoanMode(String loanMode) {
        this.loanMode = loanMode;
    }

    public String getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(String firstPaymentDate) {
        this.firstPaymentDate = firstPaymentDate;
    }

    public BigDecimal getFirstPaymentAmount() {
        return firstPaymentAmount;
    }

    public void setFirstPaymentAmount(BigDecimal firstPaymentAmount) {
        this.firstPaymentAmount = firstPaymentAmount;
    }

    public String getLoanApplyDate() {
        return loanApplyDate;
    }

    public void setLoanApplyDate(String loanApplyDate) {
        this.loanApplyDate = loanApplyDate;
    }

    public String getPaymentContents() {
        return paymentContents;
    }

    public void setPaymentContents(String paymentContents) {
        this.paymentContents = paymentContents;
    }

    public String getBuyerPays() {
        return buyerPays;
    }

    public void setBuyerPays(String buyerPays) {
        this.buyerPays = buyerPays;
    }
}
