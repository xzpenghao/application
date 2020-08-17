package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/14
 * description：不动产权利类型
 */
public enum DIC_BDC_QLLX_Enums {
    JTTDSYQ("集体土地所有权","1"),
    TDCBQSLSYQ("土地承包经营权/森林、林木所有权","10"),
    LDSYQ("林地使用权","11"),
    LDSYQSLSYQ("林地使用权/森林、林木使用权","12"),
    CYSYQ("草原使用权","13"),
    SYTTYZQ("水域滩涂养殖权","14"),
    HYSYQ("海域使用权","15"),
    HYSYQGJZWSYQ("海域使用权/构（建）筑物所有权","16"),
    WJMHDSYQ("无居民海岛使用权","17"),
    WJMHDSYQGJZWSYQ("无居民海岛使用权/构（建）筑物所有权","18"),
    DYQ("地役权","19"),
    GJTDSYQ("国家土地所有权","2"),
    QSY("取水权","20"),
    TKQ("探矿权","21"),
    CKQ("采矿权","22"),
    DYAQ("抵押权","23"),
    GYJSYDSYQ("国有建设用地使用权","3"),
    GYJSYDSYQFGZWSYQ("国有建设用地使用权/房屋（构筑物）所有权","4"),
    ZJDSYQ("宅基地使用权","5"),
    ZJDSYQFGZWSYQ("宅基地使用权/房屋（构筑物）所有权","6"),
    JTJSYDSYQ("集体建设用地使用权","7"),
    JTJSYDSYQFWSYQ("集体建设用地使用权/房屋（构筑物）所有权","8"),
    TDCBJYQ("土地承包经营权","9"),
    QTQL("其它权利","99");


    private String dicName;
    private String dicVal;

    DIC_BDC_QLLX_Enums (String dicName, String dicVal){
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
