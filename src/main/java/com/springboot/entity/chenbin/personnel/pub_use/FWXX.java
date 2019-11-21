package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class FWXX implements Serializable {
    @JsonProperty("bDCDYH")
    private String BDCDYH;  //不动产单元号
    @JsonProperty("zL")
    private String ZL;      //坐落
    @JsonProperty("yT")
    private String YT;      //用途
    @JsonProperty("xMMC")
    private String XMMC;    //项目名称
    @JsonProperty("yFCBH")
    private String YFCBH;   //原房产编号
    @JsonProperty("tNMJ")
    private BigDecimal TNMJ;    //套内面积
    @JsonProperty("jZMJ")
    private BigDecimal JZMJ;    //建筑面积
    @JsonProperty("fTMJ")
    private BigDecimal FTMJ;    //分摊面积
    @JsonProperty("fWDY")
    private String FWDY;    //单元号
    @JsonProperty("fWFH")
    private String FWFH;    //房号
    @JsonProperty("fWJG")
    private String FWJG;    //房屋结构
    @JsonProperty("fWLX")
    private String FWLX;    //房屋类型
    @JsonProperty("fWXZ")
    private String FWXZ;    //房屋性质
    @JsonProperty("fWZL")
    private String FWZL;    //房屋坐落
    @JsonProperty("zCS")
    private String ZCS;     //总层数
    @JsonProperty("sZC")
    private String SZC;     //所在层

    public String getBDCDYH() {
        return BDCDYH;
    }

    public void setBDCDYH(String BDCDYH) {
        this.BDCDYH = BDCDYH;
    }

    public String getZL() {
        return ZL;
    }

    public void setZL(String ZL) {
        this.ZL = ZL;
    }

    public String getYT() {
        return YT;
    }

    public void setYT(String YT) {
        this.YT = YT;
    }

    public String getXMMC() {
        return XMMC;
    }

    public void setXMMC(String XMMC) {
        this.XMMC = XMMC;
    }

    public String getYFCBH() {
        return YFCBH;
    }

    public void setYFCBH(String YFCBH) {
        this.YFCBH = YFCBH;
    }

    public BigDecimal getTNMJ() {
        return TNMJ;
    }

    public void setTNMJ(BigDecimal TNMJ) {
        this.TNMJ = TNMJ;
    }

    public BigDecimal getJZMJ() {
        return JZMJ;
    }

    public void setJZMJ(BigDecimal JZMJ) {
        this.JZMJ = JZMJ;
    }

    public BigDecimal getFTMJ() {
        return FTMJ;
    }

    public void setFTMJ(BigDecimal FTMJ) {
        this.FTMJ = FTMJ;
    }

    public String getFWDY() {
        return FWDY;
    }

    public void setFWDY(String FWDY) {
        this.FWDY = FWDY;
    }

    public String getFWFH() {
        return FWFH;
    }

    public void setFWFH(String FWFH) {
        this.FWFH = FWFH;
    }

    public String getFWJG() {
        return FWJG;
    }

    public void setFWJG(String FWJG) {
        this.FWJG = FWJG;
    }

    public String getFWLX() {
        return FWLX;
    }

    public void setFWLX(String FWLX) {
        this.FWLX = FWLX;
    }

    public String getFWXZ() {
        return FWXZ;
    }

    public void setFWXZ(String FWXZ) {
        this.FWXZ = FWXZ;
    }

    public String getFWZL() {
        return FWZL;
    }

    public void setFWZL(String FWZL) {
        this.FWZL = FWZL;
    }

    public String getZCS() {
        return ZCS;
    }

    public void setZCS(String ZCS) {
        this.ZCS = ZCS;
    }

    public String getSZC() {
        return SZC;
    }

    public void setSZC(String SZC) {
        this.SZC = SZC;
    }
}
