package com.springboot.entity.chenbin.personnel.pub_use;

import java.io.Serializable;
import java.math.BigDecimal;

public class FWXX implements Serializable {
    private String BDCDYH;  //不动产单元号
    private String ZL;      //坐落
    private String YT;      //用途
    private String XMMC;    //项目名称
    private String YFCBH;   //原房产编号
    private BigDecimal TNMJ;    //套内面积
    private BigDecimal JZMJ;    //建筑面积
    private BigDecimal FTMJ;    //分摊面积
    private String FWDY;    //单元号
    private String FWFH;    //房号
    private String FWJG;    //房屋结构
    private String FWLX;    //房屋类型
    private String FWXZ;    //房屋性质
    private String FWZL;    //房屋坐落
    private String ZCS;     //总层数
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
