package com.springboot.entity.chenbin.personnel.other.bank.bankenum;

public enum  MortgageModeEnum {
    Yb("一般抵押"),Yz("余值抵押"),Zg("最高额抵押");
    //以上是枚举的成员，必须先定义，而且使用分号结束
    private final String day;
    private MortgageModeEnum(String day)
    {
        this.day=day;
    }
    public static String Sc(String i)
    {
        String a="";
        switch(i)
        {
            case "MORTGAGE_ GENERAL":
                a = MortgageModeEnum.Yb.day;
                break;
            case "MORTGAGE_ SURPLUS":
                a = MortgageModeEnum.Yz.day;
                break;
            case "MORTGAGE_MAX":
                a = MortgageModeEnum.Zg.day;
                break;
        }
        return a;
    }
    public String getDay()
    {
        return day;
    }
}
