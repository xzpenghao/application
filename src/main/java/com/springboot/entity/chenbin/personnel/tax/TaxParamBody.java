package com.springboot.entity.chenbin.personnel.tax;
import com.springboot.entity.chenbin.personnel.pub_use.HTXX;
import com.springboot.entity.chenbin.personnel.pub_use.JYQLRXX;
import com.springboot.entity.chenbin.personnel.pub_use.QSXX;
import com.springboot.entity.chenbin.personnel.pub_use.YCSLXX;

import java.io.Serializable;
import java.util.List;

public class TaxParamBody implements Serializable {
    private String DYZT;
    private String YYZT;
    private String CFZT;

    private HTXX HTXX;
    private YCSLXX YCSLXX;
    private List<QSXX> QSXX;
    private List<JYQLRXX> JYQLRXX;
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

    public List<com.springboot.entity.chenbin.personnel.pub_use.QSXX> getQSXX() {
        return QSXX;
    }

    public void setQSXX(List<com.springboot.entity.chenbin.personnel.pub_use.QSXX> QSXX) {
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
