package com.springboot.popj.warrant;

/**
 * 不动产权属参数信息
 */
public class ParametricData {
    private String bdcdyh;//不动产单元号
    private String bdczh;//不动产证号
    private String obligeeName;//权利人姓名


    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getBdczh() {
        return bdczh;
    }

    public void setBdczh(String bdczh) {
        this.bdczh = bdczh;
    }

    public String getObligeeName() {
        return obligeeName;
    }

    public void setObligeeName(String obligeeName) {
        this.obligeeName = obligeeName;
    }
}
