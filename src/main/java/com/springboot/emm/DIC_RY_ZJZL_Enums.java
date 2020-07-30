package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/7/29/029
 * description：人员证件类型
 */
public enum DIC_RY_ZJZL_Enums {
    SFZ("身份证","1"),
    GATSFZ("港澳台身份证","2"),
    HZ("护照","3"),
    HKB("户口簿","4"),
    JGZ("军官证（士兵证）","5"),
    ZZJGDM("组织机构代码","6"),
    YYZZ("营业执照","7"),
    TYSHXYDMZ("统一社会信用代码证","8"),
    QT("其它","99");

    private String dicName;
    private String dicVal;

    DIC_RY_ZJZL_Enums(String dicName, String dicVal){
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
