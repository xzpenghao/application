package com.springboot.entity.chenbin.personnel.req;

import java.io.Serializable;

public class PersonnelUnitReqEntity implements Serializable {
    private String mc;
    private String zjlx;
    private String zjhm;
    private String fjlj;
    private String xczpBase64;

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx;
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getFjlj() {
        return fjlj;
    }

    public void setFjlj(String fjlj) {
        this.fjlj = fjlj;
    }

    public String getXczpBase64() {
        return xczpBase64;
    }

    public void setXczpBase64(String xczpBase64) {
        this.xczpBase64 = xczpBase64;
    }
}
