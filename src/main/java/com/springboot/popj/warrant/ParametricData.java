package com.springboot.popj.warrant;

/**
 * 不动产权属参数信息
 */
public class ParametricData {
    private String bdcdyh;//不动产单元号
    private String budzh;//不动产证号
    private String dyzmh;//抵押证明号

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getBudzh() {
        return budzh;
    }

    public void setBudzh(String budzh) {
        this.budzh = budzh;
    }

    public String getDyzmh() {
        return dyzmh;
    }

    public void setDyzmh(String dyzmh) {
        this.dyzmh = dyzmh;
    }
}
