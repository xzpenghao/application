package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/7/29/029
 * description：人员共有方式
 */
public enum DIC_RY_GYFS_Enums {
    DDSY("单独所有","0"),
    GTGY("共同共有","1"),
    AFGY("按份共有","2"),
    QTGY("其它共有","3");

    private String dicName;
    private String dicVal;

    DIC_RY_GYFS_Enums(String dicName, String dicVal){
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
