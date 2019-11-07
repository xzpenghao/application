package com.springboot.entity.chenbin.personnel.other.bank.bankenum;

public enum  DistrictCodeEnum {

    SF("宿城区"),GAT("宿豫区");
    //以上是枚举的成员，必须先定义，而且使用分号结束
    private final String day;
    private DistrictCodeEnum(String day)
    {
        this.day=day;
    }
    public static String Sc(String i)
    {
        String a="";
        switch(i)
        {
            case "321301":
                a = DistrictCodeEnum.SF.day;
                break;
            case "321302":
                a = DistrictCodeEnum.GAT.day;
                break;

        }
        return a;
    }
    public String getDay()
    {
        return day;
    }
}
