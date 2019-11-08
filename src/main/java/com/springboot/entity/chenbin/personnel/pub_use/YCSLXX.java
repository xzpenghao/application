package com.springboot.entity.chenbin.personnel.pub_use;

import java.io.Serializable;

public class YCSLXX implements Serializable {
    private String SLBH;
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
