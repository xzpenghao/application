package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/7/28/028
 * description：权利人种类
 */
public enum DIC_RY_ZL_Enums {
    QLR("权利人","1"),
    YWR("义务人","2"),
    DYR("抵押人","3"),
    DYQR("抵押权人","4"),
    YGDYQR("预告抵押权人","5"),
    YGDYR("预告抵押人","6"),
    YGQLR("预告权利人","7"),
    YGYWR("预告义务人","8"),
    YYSQR("异议申请人","9");

    private String dicName;
    private String dicVal;

    DIC_RY_ZL_Enums(String dicName, String dicVal){
        this.dicName = dicName;
        this.dicVal = dicVal;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getDicVal() {
        return dicVal;
    }

    public void setDicVal(String dicVal) {
        this.dicVal = dicVal;
    }
}
