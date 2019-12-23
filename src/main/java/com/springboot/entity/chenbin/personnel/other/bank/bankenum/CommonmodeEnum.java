package com.springboot.entity.chenbin.personnel.other.bank.bankenum;

public enum  CommonmodeEnum {
    Dd("单独所有"),Gt("共同所有"),Af("按份所有"),Qt("其他所有");
    //以上是枚举的成员，必须先定义，而且使用分号结束
    private final String day;
    private CommonmodeEnum(String day)
    {
        this.day=day;
    }
    public static String Sc(String i)
    {
        String a="";
        switch(i)
        {
            case "ALONE":
                a = CommonmodeEnum.Dd.day;
                break;
            case "COPARCENARY":
                a = CommonmodeEnum.Gt.day;
                break;
            case "SHARE":
                a = CommonmodeEnum.Af.day;
                break;
            case "OTHER":
                a = CommonmodeEnum.Qt.day;
                break;
        }
        return a;
    }
    public String getDay()
    {
        return day;
    }
}
