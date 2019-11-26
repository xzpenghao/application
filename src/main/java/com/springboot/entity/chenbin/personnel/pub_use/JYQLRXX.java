package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class JYQLRXX implements Serializable {
    @JsonProperty("QLRMC")
    private String QLRMC;
    @JsonProperty("ZJLX")
    private String ZJLX;
    @JsonProperty("ZJHM")
    private String ZJHM;
    @JsonProperty("DH")
    private String DH;
    @JsonProperty("GYFS")
    private String GYFS;
    @JsonProperty("GYFE")
    private String GYFE;
    @JsonProperty("QLRBS")
    private String QLRBS;           //买卖方

    public String getQLRMC() {
        return QLRMC;
    }
    @JsonIgnore
    public void setQLRMC(String QLRMC) {
        this.QLRMC = QLRMC;
    }

    public String getZJLX() {
        return ZJLX;
    }
    @JsonIgnore
    public void setZJLX(String ZJLX) {
        this.ZJLX = ZJLX;
    }

    public String getZJHM() {
        return ZJHM;
    }
    @JsonIgnore
    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getDH() {
        return DH;
    }
    @JsonIgnore
    public void setDH(String DH) {
        this.DH = DH;
    }

    public String getGYFS() {
        return GYFS;
    }
    @JsonIgnore
    public void setGYFS(String GYFS) {
        this.GYFS = GYFS;
    }

    public String getGYFE() {
        return GYFE;
    }
    @JsonIgnore
    public void setGYFE(String GYFE) {
        this.GYFE = GYFE;
    }

    public String getQLRBS() {
        return QLRBS;
    }
    @JsonIgnore
    public void setQLRBS(String QLRBS) {
        this.QLRBS = QLRBS;
    }
}
