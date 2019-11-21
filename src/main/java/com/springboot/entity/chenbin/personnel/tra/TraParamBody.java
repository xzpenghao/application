package com.springboot.entity.chenbin.personnel.tra;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.entity.chenbin.personnel.pub_use.HTXX;
import com.springboot.entity.chenbin.personnel.pub_use.JYQLRXX;
import com.springboot.entity.chenbin.personnel.pub_use.QSXX;
import com.springboot.entity.chenbin.personnel.pub_use.YCSLXX;

import java.io.Serializable;
import java.util.List;

public class TraParamBody implements Serializable {
    @JsonProperty("dYZT")
    private String DYZT;
    @JsonProperty("yYZT")
    private String YYZT;
    @JsonProperty("cFZT")
    private String CFZT;

    @JsonProperty("hTXX")
    private com.springboot.entity.chenbin.personnel.pub_use.HTXX HTXX;
    @JsonProperty("yCSLXX")
    private com.springboot.entity.chenbin.personnel.pub_use.YCSLXX YCSLXX;
    @JsonProperty("qSXX")
    private com.springboot.entity.chenbin.personnel.pub_use.QSXX QSXX;
    @JsonProperty("jYQLRXX")
    private List<com.springboot.entity.chenbin.personnel.pub_use.JYQLRXX> JYQLRXX;
    @JsonProperty("jYDLRXX")
    private List<JYQLRXX> JYDLRXX;

    public String getDYZT() {
        return DYZT;
    }

    public void setDYZT(String DYZT) {
        this.DYZT = DYZT;
    }

    public String getYYZT() {
        return YYZT;
    }

    public void setYYZT(String YYZT) {
        this.YYZT = YYZT;
    }

    public String getCFZT() {
        return CFZT;
    }

    public void setCFZT(String CFZT) {
        this.CFZT = CFZT;
    }

    public com.springboot.entity.chenbin.personnel.pub_use.HTXX getHTXX() {
        return HTXX;
    }

    public void setHTXX(com.springboot.entity.chenbin.personnel.pub_use.HTXX HTXX) {
        this.HTXX = HTXX;
    }

    public com.springboot.entity.chenbin.personnel.pub_use.YCSLXX getYCSLXX() {
        return YCSLXX;
    }

    public void setYCSLXX(com.springboot.entity.chenbin.personnel.pub_use.YCSLXX YCSLXX) {
        this.YCSLXX = YCSLXX;
    }

    public com.springboot.entity.chenbin.personnel.pub_use.QSXX getQSXX() {
        return QSXX;
    }

    public void setQSXX(com.springboot.entity.chenbin.personnel.pub_use.QSXX QSXX) {
        this.QSXX = QSXX;
    }

    public List<com.springboot.entity.chenbin.personnel.pub_use.JYQLRXX> getJYQLRXX() {
        return JYQLRXX;
    }

    public void setJYQLRXX(List<com.springboot.entity.chenbin.personnel.pub_use.JYQLRXX> JYQLRXX) {
        this.JYQLRXX = JYQLRXX;
    }

    public List<com.springboot.entity.chenbin.personnel.pub_use.JYQLRXX> getJYDLRXX() {
        return JYDLRXX;
    }

    public void setJYDLRXX(List<com.springboot.entity.chenbin.personnel.pub_use.JYQLRXX> JYDLRXX) {
        this.JYDLRXX = JYDLRXX;
    }
}
