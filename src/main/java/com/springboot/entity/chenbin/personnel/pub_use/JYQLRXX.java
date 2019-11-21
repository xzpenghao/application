package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class JYQLRXX implements Serializable {
    @JsonProperty("qLRMC")
    private String QLRMC;
    @JsonProperty("zJLX")
    private String ZJLX;
    @JsonProperty("zJHM")
    private String ZJHM;
    @JsonProperty("dH")
    private String DH;
    @JsonProperty("gYFS")
    private String GYFS;
    @JsonProperty("gYFE")
    private String GYFE;
    @JsonProperty("qLRBS")
    private String QLRBS;           //买卖方

    public String getQLRMC() {
        return QLRMC;
    }

    public void setQLRMC(String QLRMC) {
        this.QLRMC = QLRMC;
    }

    public String getZJLX() {
        return ZJLX;
    }

    public void setZJLX(String ZJLX) {
        this.ZJLX = ZJLX;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getDH() {
        return DH;
    }

    public void setDH(String DH) {
        this.DH = DH;
    }

    public String getGYFS() {
        return GYFS;
    }

    public void setGYFS(String GYFS) {
        this.GYFS = GYFS;
    }

    public String getGYFE() {
        return GYFE;
    }

    public void setGYFE(String GYFE) {
        this.GYFE = GYFE;
    }

    public String getQLRBS() {
        return QLRBS;
    }

    public void setQLRBS(String QLRBS) {
        this.QLRBS = QLRBS;
    }
}
