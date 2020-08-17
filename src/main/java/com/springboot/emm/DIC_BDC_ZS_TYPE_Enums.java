package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/7/28/028
 * description：证书类型
 */
public enum DIC_BDC_ZS_TYPE_Enums {
    FWBDCQZ("房屋不动产证","1"),
    TDBDCQZ("土地不动产证","2");

    private String dicName;
    private String dicVal;

    DIC_BDC_ZS_TYPE_Enums(String dicName, String dicVal){
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
