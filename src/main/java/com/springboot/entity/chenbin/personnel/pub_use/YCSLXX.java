package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class YCSLXX implements Serializable {
    @JsonProperty("sLBH")
    private String SLBH;
    @JsonProperty("sQSJ")
    private String SQSJ;

    public String getSLBH() {
        return SLBH;
    }

    public void setSLBH(String SLBH) {
        this.SLBH = SLBH;
    }

    public String getSQSJ() {
        return SQSJ;
    }

    public void setSQSJ(String SQSJ) {
        this.SQSJ = SQSJ;
    }
}
