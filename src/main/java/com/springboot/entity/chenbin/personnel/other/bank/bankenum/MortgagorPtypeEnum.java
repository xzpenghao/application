package com.springboot.entity.chenbin.personnel.other.bank.bankenum;

public enum  MortgagorPtypeEnum {

    Xj("ID_CARD"),Cl("H_M_T_ID_CARD"),Dy("PASSPORT"),JG("MILITARY_ID"),
   DM("ORGANIZATION_CODE"),ZZ("BUSINESS_LICENSE"),SHDM("UNIFIED_SOCIAL_CREDIT_CODE");
    //以上是枚举的成员，必须先定义，而且使用分号结束
    private final String day;
    private MortgagorPtypeEnum(String day)
    {
        this.day=day;
    }
    public static String Sc(String i)
    {
        String a="";
        switch(i)
        {
            case "身份证":
                a = MortgagorPtypeEnum.Xj.day;
                break;
            case "港澳台身份证":
                a = MortgagorPtypeEnum.Cl.day;
                break;
            case "护照":
                a = MortgagorPtypeEnum.Dy.day;
                break;
            case "军官证":
                a = MortgagorPtypeEnum.JG.day;
                break;
            case "组织机构代码证":
                a = MortgagorPtypeEnum.DM.day;
                break;
            case "营业执照":
                a = MortgagorPtypeEnum.ZZ.day;
                break;
            case "统一社会信用代码":
                a = MortgagorPtypeEnum.SHDM.day;
                break;
        }
        return a;
    }
    public String getDay()
    {
        return day;
    }


}
