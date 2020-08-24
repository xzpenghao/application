package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/21
 * description：不动产业务类型——归类后的字典
 */
public enum KEY_YWLX_GLH_Enums {
    DY("抵押","DY"),
    DYZX("抵押注销","DYZX"),
    QS("权属","QS"),
    QSZX("权属注销","QSZX"),
    CF("查封","CF"),
    CFZX("查封注销","CFZX"),
    YG("预告","YG"),
    YGZX("预告注销","YGZX"),
    YY("异议","YY"),
    YYZX("异议注销","YYZX");

    private String keyWord;
    private String keyCode;

    KEY_YWLX_GLH_Enums(String keyWord, String keyCode){
        this.keyWord = keyWord;
        this.keyCode = keyCode;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }
}
