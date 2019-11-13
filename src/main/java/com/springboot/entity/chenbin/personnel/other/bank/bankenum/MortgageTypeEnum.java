package com.springboot.entity.chenbin.personnel.other.bank.bankenum;

import com.springboot.entity.chenbin.personnel.other.bank.bankenum.MortgageModeEnum;

public enum  MortgageTypeEnum {

   Xj("新建商品房预告抵押登记"),Cl("存量房预告抵押登记"),Dy("抵押登记（个人）");
    //以上是枚举的成员，必须先定义，而且使用分号结束
    private final String day;
    private MortgageTypeEnum(String day)
    {
        this.day=day;
    }
    public static String Sc(String i)
    {
        String a="";
        switch(i)
        {
            case "MORTGAGE_NEW_TRAILER":
                a = MortgageTypeEnum.Xj.day;
                break;
            case "MORTGAGE_OLD_TRAILER":
                a = MortgageTypeEnum.Cl.day;
                break;
            case "MORTGAGE_PERSONAL":
                a = MortgageTypeEnum.Dy.day;
                break;
        }
        return a;
    }
    public String getDay()
    {
        return day;
    }

}
