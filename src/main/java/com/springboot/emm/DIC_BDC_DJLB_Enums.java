package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/7
 * description：转内网登记类别
 */
public enum DIC_BDC_DJLB_Enums {
    DBYW("单笔业务","0"),
    ZHYW("组合业务","1");

    private String dicName;
    private String dicVal;

    DIC_BDC_DJLB_Enums(String dicName,String dicVal){
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
