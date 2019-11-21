package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class QSXX implements Serializable {
    @JsonProperty("bDCZH")
    private String BDCZH;
    @JsonProperty("jZMJ")
    private BigDecimal JZMJ;
    @JsonProperty("tNMJ")
    private BigDecimal TNMJ;
    @JsonProperty("zL")
    private String ZL;
    @JsonProperty("yT")
    private String YT;
    @JsonProperty("tDSYQR")
    private String TDSYQR;
    @JsonProperty("tDHQFS")
    private String TDHQFS = "";

    @JsonProperty("fWXX")
    private List<FWXX> FWXX;

    public String getBDCZH() {
        return BDCZH;
    }

    public void setBDCZH(String BDCZH) {
        this.BDCZH = BDCZH;
    }

    public BigDecimal getJZMJ() {
        return JZMJ;
    }

    public void setJZMJ(BigDecimal JZMJ) {
        this.JZMJ = JZMJ;
    }

    public BigDecimal getTNMJ() {
        return TNMJ;
    }

    public void setTNMJ(BigDecimal TNMJ) {
        this.TNMJ = TNMJ;
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

    public List<com.springboot.entity.chenbin.personnel.pub_use.FWXX> getFWXX() {
        return FWXX;
    }

    public void setFWXX(List<com.springboot.entity.chenbin.personnel.pub_use.FWXX> FWXX) {
        this.FWXX = FWXX;
    }

    public String getTDSYQR() {
        return TDSYQR;
    }

    public void setTDSYQR(String TDSYQR) {
        this.TDSYQR = TDSYQR;
    }

    public String getTDHQFS() {
        return TDHQFS;
    }

    public void setTDHQFS(String TDHQFS) {
        if(TDHQFS!=null) {
            this.TDHQFS = TDHQFS;
        }
    }
}
