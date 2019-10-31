package com.springboot.entity.chenbin.personnel.other.bank.bankenum;

public enum   FileTypeEnum {

    BD("不动产登记申请书"),BJ("不动产登记询问笔录"),MM("经备案的商品房买卖合同"),JK("借款合同"),
    DYHT("抵押合同"),SFZ("身份证"),HKB("户口本"),JHZ("结婚证或婚姻状况证明"),ZP("客户经理谈话照片")
    ,FP("购房发票") ,QS("契税完税发票") ,QLS("他项权利证书或不动产登记证明") ,BDCZ("不动产证")
    ,ZM("抵押电子证明") ,DK("贷款结清证明");
    //以上是枚举的成员，必须先定义，而且使用分号结束
    private final String day;
    private FileTypeEnum(String day)
    {
        this.day=day;
    }
    public static String Sc(String i)
    {
        String a="";
        switch(i)
        {
            case "REALESTATE_REG_APPLY":
                a = FileTypeEnum.BD.day;
                break;
            case "REALESTATE_RECORD":
                a = FileTypeEnum.BJ.day;
                break;
            case "COMMODITY_HOUSE_CONTRACT":
                a = FileTypeEnum.MM.day;
                break;
            case "LOAN_ CONTRACT":
                a = FileTypeEnum.JK.day;
                break;
            case "MORTGAGE_CONTRACT":
                a = FileTypeEnum.DYHT.day;
                break;
            case "ID_CARD":
                a = FileTypeEnum.SFZ.day;
                break;
            case "RESIDENCE":
                a = FileTypeEnum.HKB.day;
                break;
            case "MARRIAGE_PROVE":
                a = FileTypeEnum.JHZ.day;
                break;
            case "MANAGER_TALK":
                a = FileTypeEnum.ZP.day;
                break;
            case "PURCHASE_INVOICE":
                a = FileTypeEnum.FP.day;
                break;
            case "DEED_TAX_INVOICE":
                a = FileTypeEnum.QS.day;
                break;
            case "OTHER_OR_REALESTATE_ PROVE":
                a = FileTypeEnum.QLS.day;
                break;
            case "REALESTATE_PROVE":
                a = FileTypeEnum.BDCZ.day;
                break;
            case "MORTGAGE_EL_PROVE":
                a = FileTypeEnum.ZM.day;
                break;
            case "LOAN_SETTLE_PROVE":
                a = FileTypeEnum.DK.day;
                break;
        }
        return a;
    }
    public String getDay()
    {
        return day;
    }

}
