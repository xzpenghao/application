package com.springboot.entity.chenbin.personnel.other.bank.bankenum;

public enum  MortgageReasonEnum {


    Jd("借贷"),Gm("购买商品房");
    //以上是枚举的成员，必须先定义，而且使用分号结束
    private final String day;
    private MortgageReasonEnum(String day)
    {
        this.day=day;
    }
    public static String Sc(String i)
    {
        String a="";
        switch(i)
        {
            case "REASON_MOR":
                a = MortgageReasonEnum.Jd.day;
                break;
            case "REASON_BUY":
                a = MortgageReasonEnum.Gm.day;
                break;
        }
        return a;
    }
    public String getDay()
    {
        return day;
    }
}
