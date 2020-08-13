package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/13
 * description：房屋规划用途字典
 */
public enum DIC_BDC_FW_GHYT_Enums {
    ZZ("住宅","10"),
    CTZZ("成套住宅","11"),
    BS("别墅","111"),
    GDGY("高档公寓","112"),
    JDSGY("酒店式公寓(住)","113"),
    FCTZZ("非成套住宅","12"),
    JTSS("集体宿舍","13"),
    GYJTCC("工业、交通、仓储","20"),
    GY("工业","21"),
    GGSS("公共设施","22"),
    TL("铁路","23"),
    MH("民航","24"),
    HY("航运","25"),
    GGYS("公共运输","26"),
    CC("仓储","27"),
    SYJRXX("商业、金融、信息","30"),
    SYFW("商业服务","31"),
    JY("经营","32"),
    CK("车库","3201"),
    LY("旅游","33"),
    JRBX("金融保险","34"),
    DXXX("电讯信息","35"),
    JYYLWSKY("教育、医疗、卫生、科研","40"),
    JYU("教育","41"),
    YLWS("医疗卫生","42"),
    KY("科研","43"),
    WHYLTY("文化、娱乐、体育","50"),
    WH("文化","51"),
    XW("新闻","52"),
    YL("娱乐","53"),
    YLLH("园林绿化","54"),
    TY("体育","55"),
    BG("办公","60"),
    JS("军事","70"),
    QT("其它","80"),
    SW("涉外","81"),
    ZJ("宗教","82"),
    JIANY("监狱","83"),
    WGYF("物管用房","84"),
    GJPT("公建配套","8401");

    private String dicName;
    private String dicVal;

    DIC_BDC_FW_GHYT_Enums(String dicName, String dicVal){
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
