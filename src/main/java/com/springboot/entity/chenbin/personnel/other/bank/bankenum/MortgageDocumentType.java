package com.springboot.entity.chenbin.personnel.other.bank.bankenum;

public enum  MortgageDocumentType {
    SF("身份证"),GAT("港澳台身份证"),HZ("护照"),JG("军官证"),JGDM("组织机构代码证"),YYZZ("营业执照"),XYM("统一社会信用代码");
    //以上是枚举的成员，必须先定义，而且使用分号结束
    private final String day;
    private MortgageDocumentType(String day)
    {
        this.day=day;
    }
    public static String Sc(String i)
    {
        String a="";
        switch(i)
        {
            case "ID_CARD":
                a = MortgageDocumentType.SF.day;
                break;
            case "H_M_T_ID_CARD":
                a = MortgageDocumentType.GAT.day;
                break;
            case "PASSPORT":
                a = MortgageDocumentType.HZ.day;
                break;
            case "MILITARY_ID":
                a = MortgageDocumentType.JG.day;
                break;
            case "ORGANIZATION_CODE":
                a = MortgageDocumentType.JGDM.day;
                break;
            case "BUSINESS_LICENSE":
                a = MortgageDocumentType.YYZZ.day;
                break;
            case "UNIFIED_SOCIAL_CREDIT_CODE":
                a = MortgageDocumentType.XYM.day;
                break;
        }
        return a;
    }
    public String getDay()
    {
        return day;
    }
}
