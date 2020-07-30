package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/7/29/029
 * description：人员类型
 */
public enum DIC_RY_LX_Enums {
    TZR("通知人","1"),
    JHR("监护人","2"),
    JJR("交件人","3"),
    DLR("代理人","4"),
    QLR("权利人","5"),
    WTR("委托人","6");

    private String dicName;
    private String dicVal;

    DIC_RY_LX_Enums(String dicName, String dicVal){
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
