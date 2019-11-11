package com.springboot.entity.chenbin.personnel.tra;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class TraRespBody implements Serializable {
    @ApiModelProperty(value = "收件编号")
    private String receiptNumber;                              //收件编号
    @ApiModelProperty(value = "交易侧产生的办件编号")
    private String acceptanceNumber;                            //交易侧产生的办件编号
    @ApiModelProperty(value = "合同编号")
    private String contractNumber;                             //合同编号
    @ApiModelProperty(value = "合同备案编号")
    private String contractRecordNumber;                        //合同备案编号
    @ApiModelProperty(value = "处理结果信息（1 备案成功 / 0 备案失败）")
    private String handleResult;                                //处理结果信息（1 备案成功 / 0 备案失败）
    @ApiModelProperty(value = "案的具体信息（如审核结果或失败原因等）")
    private String handleText;                                  //备案的具体信息（如审核结果或失败原因等）
    @ApiModelProperty(value = "数据提交单位")
    private String provideUnit;                                 //数据提交单位
    @ApiModelProperty(value = "业务备注或附记信息（存在的话填写）")
    private String remarks;                                     //业务备注或附记信息（存在的话填写）

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getAcceptanceNumber() {
        return acceptanceNumber;
    }

    public void setAcceptanceNumber(String acceptanceNumber) {
        this.acceptanceNumber = acceptanceNumber;
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

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public String getHandleText() {
        return handleText;
    }

    public void setHandleText(String handleText) {
        this.handleText = handleText;
    }

    public String getProvideUnit() {
        return provideUnit;
    }

    public void setProvideUnit(String provideUnit) {
        this.provideUnit = provideUnit;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
