package com.springboot.entity.chenbin.personnel.other.other_depart;

import java.io.Serializable;

public class JSEntity implements Serializable {
    private String hsyj;        //核税依据
    private String sd;          //税点
    private String jnjm;        //缴纳减免
    private String sjjnje;      //实际缴纳金额
    private String swzl;        //税务种类
    private String jsjg;        //缴税结果
    private String nsr;         //纳税人
    private String jnsj;        //缴纳时间

    public JSEntity() {
        super();
    }

    public JSEntity(String hsyj, String sd, String jnjm, String sjjnje, String swzl, String jsjg, String nsr, String jnsj) {
        this.hsyj = hsyj;
        this.sd = sd;
        this.jnjm = jnjm;
        this.sjjnje = sjjnje;
        this.swzl = swzl;
        this.jsjg = jsjg;
        this.nsr = nsr;
        this.jnsj = jnsj;
    }

    public String getHsyj() {
        return hsyj;
    }

    public void setHsyj(String hsyj) {
        this.hsyj = hsyj;
    }

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public String getJnjm() {
        return jnjm;
    }

    public void setJnjm(String jnjm) {
        this.jnjm = jnjm;
    }

    public String getSjjnje() {
        return sjjnje;
    }

    public void setSjjnje(String sjjnje) {
        this.sjjnje = sjjnje;
    }

    public String getSwzl() {
        return swzl;
    }

    public void setSwzl(String swzl) {
        this.swzl = swzl;
    }

    public String getJsjg() {
        return jsjg;
    }

    public void setJsjg(String jsjg) {
        this.jsjg = jsjg;
    }

    public String getNsr() {
        return nsr;
    }

    public void setNsr(String nsr) {
        this.nsr = nsr;
    }

    public String getJnsj() {
        return jnsj;
    }

    public void setJnsj(String jnsj) {
        this.jnsj = jnsj;
    }
}
