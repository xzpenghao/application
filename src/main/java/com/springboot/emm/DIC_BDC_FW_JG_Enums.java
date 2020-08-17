package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/14
 * description：房屋结构
 */
public enum DIC_BDC_FW_JG_Enums {
    WZD("未指定","0"),
    GJG("钢结构","1"),
    GHGHJG("钢和钢筋混凝土结构","2"),
    GHJG("钢筋混凝土结构","3"),
    HHJG("混合结构","4"),
    ZMJG("砖木结构","5"),
    QTJG("其它结构","6");

    private String dicName;
    private String dicVal;

    DIC_BDC_FW_JG_Enums (String dicName, String dicVal){
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
