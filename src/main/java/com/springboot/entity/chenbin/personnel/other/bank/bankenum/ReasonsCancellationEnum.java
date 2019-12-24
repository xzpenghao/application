package com.springboot.entity.chenbin.personnel.other.bank.bankenum;

public enum  ReasonsCancellationEnum {

    Dd("抵押时间截止"),Gt("抵押贷款还清");
    //以上是枚举的成员，必须先定义，而且使用分号结束
    private final String day;
    private ReasonsCancellationEnum(String day)
    {
        this.day=day;
    }
    public static String Sc(String i)
    {
        String a="";
        switch(i)
        {
            case "RELIEVE_ MORTGAGE_TIME":
                a = ReasonsCancellationEnum.Dd.day;
                break;
            case "RELIEVE_ MORTGAGE_LOAN_OFF":
                a = ReasonsCancellationEnum.Gt.day;
                break;
        }
        return a;
    }
    public String getDay()
    {
        return day;
    }
}
