package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class FWXX implements Serializable {
    @JsonProperty("BDCDYH")
    private String BDCDYH;  //不动产单元号
    @JsonProperty("ZL")
    private String ZL;      //坐落
    @JsonProperty("YT")
    private String YT;      //用途
    @JsonProperty("XMMC")
    private String XMMC;    //项目名称
    @JsonProperty("YFCBH")
    private String YFCBH;   //原房产编号
    @JsonProperty("TNMJ")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal TNMJ = new BigDecimal(-999);    //套内面积
    @JsonProperty("JZMJ")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal JZMJ = new BigDecimal(-999);    //建筑面积
    @JsonProperty("FTMJ")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal FTMJ = new BigDecimal(-999);    //分摊面积
    @JsonProperty("FWDY")
    private String FWDY;    //单元号
    @JsonProperty("FWFH")
    private String FWFH;    //房号
    @JsonProperty("FWJG")
    private String FWJG;    //房屋结构
    @JsonProperty("FWLX")
    private String FWLX;    //房屋类型
    @JsonProperty("FWXZ")
    private String FWXZ;    //房屋性质
    @JsonProperty("FWZL")
    private String FWZL;    //房屋坐落
    @JsonProperty("ZCS")
    private String ZCS;     //总层数
    @JsonProperty("SZC")
    private String SZC;     //所在层

    public String getBDCDYH() {
        return BDCDYH;
    }
    @JsonIgnore
    public void setBDCDYH(String BDCDYH) {
        this.BDCDYH = BDCDYH;
    }

    public String getZL() {
        return ZL;
    }
    @JsonIgnore
    public void setZL(String ZL) {
        this.ZL = ZL;
    }

    public String getYT() {
        return YT;
    }
    @JsonIgnore
    public void setYT(String YT) {
        this.YT = YT;
    }

    public String getXMMC() {
        return XMMC;
    }
    @JsonIgnore
    public void setXMMC(String XMMC) {
        this.XMMC = XMMC;
    }

    public String getYFCBH() {
        return YFCBH;
    }
    @JsonIgnore
    public void setYFCBH(String YFCBH) {
        this.YFCBH = YFCBH;
    }

    public BigDecimal getTNMJ() {
        return TNMJ;
    }
    @JsonIgnore
    public void setTNMJ(BigDecimal TNMJ) {
        if(TNMJ==null){
            TNMJ = new BigDecimal(-999);
        }
        this.TNMJ = TNMJ;
    }

    public BigDecimal getJZMJ() {
        return JZMJ;
    }
    @JsonIgnore
    public void setJZMJ(BigDecimal JZMJ) {
        if(JZMJ==null){
            JZMJ = new BigDecimal(-999);
        }
        this.JZMJ = JZMJ;
    }

    public BigDecimal getFTMJ() {
        return FTMJ;
    }
    @JsonIgnore
    public void setFTMJ(BigDecimal FTMJ) {
        if(FTMJ==null){
            FTMJ = new BigDecimal(-999);
        }
        this.FTMJ = FTMJ;
    }

    public String getFWDY() {
        return FWDY;
    }
    @JsonIgnore
    public void setFWDY(String FWDY) {
        this.FWDY = FWDY;
    }

    public String getFWFH() {
        return FWFH;
    }
    @JsonIgnore
    public void setFWFH(String FWFH) {
        this.FWFH = FWFH;
    }

    public String getFWJG() {
        return FWJG;
    }
    @JsonIgnore
    public void setFWJG(String FWJG) {
        this.FWJG = FWJG;
    }

    public String getFWLX() {
        return FWLX;
    }
    @JsonIgnore
    public void setFWLX(String FWLX) {
        this.FWLX = FWLX;
    }

    public String getFWXZ() {
        return FWXZ;
    }
    @JsonIgnore
    public void setFWXZ(String FWXZ) {
        this.FWXZ = FWXZ;
    }

    public String getFWZL() {
        return FWZL;
    }
    @JsonIgnore
    public void setFWZL(String FWZL) {
        this.FWZL = FWZL;
    }

    public String getZCS() {
        return ZCS;
    }
    @JsonIgnore
    public void setZCS(String ZCS) {
        this.ZCS = ZCS;
    }

    public String getSZC() {
        return SZC;
    }
    @JsonIgnore
    public void setSZC(String SZC) {
        this.SZC = SZC;
    }
}
