package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/14
 * description：
 */
public enum DIC_BDC_FW_XZ_Enums {
    SCHSPF("市场化商品房","0"),
    DQF("动迁房","1"),
    GJPT("公建配套","10"),
    CQAZF("拆迁安置房","101"),
    CK("车库","11"),
    FGF("房改房","12"),
    ZJF("自建房","13"),
    PTSPF("配套商品房","2"),
    GGZLZF("公共租赁住房","3"),
    LZZF("廉租住房","4"),
    XJPTSPZF("限价普通商品住房","5"),
    JJSYZF("经济适用住房","6"),
    DXSPF("定销商品房","7"),
    JZJF("集资建房","8"),
    FLF("福利房","9"),
    QT("其它","99");

    private String dicName;
    private String dicVal;

    DIC_BDC_FW_XZ_Enums (String dicName, String dicVal){
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
