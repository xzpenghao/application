package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/12
 * description：
 */
public enum DIC_BDC_DYFS_Enums {
    YBDY("一般抵押","1"),
    ZGRDY("最高额抵押","2");

    private String dicName;
    private String dicVal;

    DIC_BDC_DYFS_Enums(String dicName,String dicVal){
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
