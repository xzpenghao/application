package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class FJXX implements Serializable {
    @JsonProperty("FJMC")
    private String FJMC;            //附件名称
    @JsonProperty("FJKZM")
    private String FJKZM;           //附件扩展名
    @JsonProperty("FJDX")
    private String FJDX;            //附件大小
    @JsonProperty("FTPDZ")
    private String FTPDZ;           //ftp地址
    @JsonProperty("FJFL")
    private String FJFL;            //附件分类
    @JsonProperty("XH")
    private String XH;              //序号

    public String getFJMC() {
        return FJMC;
    }
    @JsonIgnore
    public void setFJMC(String FJMC) {
        this.FJMC = FJMC;
    }

    public String getFJKZM() {
        return FJKZM;
    }
    @JsonIgnore
    public void setFJKZM(String FJKZM) {
        this.FJKZM = FJKZM;
    }

    public String getFJDX() {
        return FJDX;
    }
    @JsonIgnore
    public void setFJDX(String FJDX) {
        this.FJDX = FJDX;
    }

    public String getFTPDZ() {
        return FTPDZ;
    }
    @JsonIgnore
    public void setFTPDZ(String FTPDZ) {
        this.FTPDZ = FTPDZ;
    }

    public String getFJFL() {
        return FJFL;
    }
    @JsonIgnore
    public void setFJFL(String FJFL) {
        this.FJFL = FJFL;
    }

    public String getXH() {
        return XH;
    }
    @JsonIgnore
    public void setXH(String XH) {
        this.XH = XH;
    }
}
