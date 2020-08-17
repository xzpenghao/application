package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/14
 * description：
 */
public enum DIC_BDC_FW_LX_Enums {
    WZD("未指定","0"),
    ZZ("住宅","1"),
    SYYF("商业用房","2"),
    BGYF("办公用房","3"),
    GYYF("工业用房","4"),
    CCYF("仓储用房","5"),
    CK("车库","6"),
    GJPT("公建配套","4"),
    QT("其它","5");

    private String dicName;
    private String dicVal;

    DIC_BDC_FW_LX_Enums (String dicName, String dicVal){
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
