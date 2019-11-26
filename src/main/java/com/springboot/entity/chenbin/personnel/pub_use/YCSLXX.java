package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class YCSLXX implements Serializable {
    @JsonProperty("SLBH")
    private String SLBH;
    @JsonProperty("SQSJ")
    private String SQSJ;

    public String getSLBH() {
        return SLBH;
    }
    @JsonIgnore
    public void setSLBH(String SLBH) {
        this.SLBH = SLBH;
    }

    public String getSQSJ() {
        return SQSJ;
    }
    @JsonIgnore
    public void setSQSJ(String SQSJ) {
        this.SQSJ = SQSJ;
    }
}
