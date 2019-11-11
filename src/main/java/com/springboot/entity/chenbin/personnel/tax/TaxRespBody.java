package com.springboot.entity.chenbin.personnel.tax;

import com.springboot.popj.pub_data.Sj_Info_Qsxx;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class TaxRespBody implements Serializable {

    @ApiModelProperty(value = "收件编号")
    private String receiptNumber;                   //收件编号-sj_sjsq表主键

    private List<Sj_Info_Qsxx> sfxxList;            //对应的缴税信息

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public List<Sj_Info_Qsxx> getSfxxList() {
        return sfxxList;
    }

    public void setSfxxList(List<Sj_Info_Qsxx> sfxxList) {
        this.sfxxList = sfxxList;
    }
}
