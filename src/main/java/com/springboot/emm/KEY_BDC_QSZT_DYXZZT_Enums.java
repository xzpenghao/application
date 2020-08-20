package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/18
 * description：不动产单元权属限制状态字典
 */
public enum KEY_BDC_QSZT_DYXZZT_Enums {
    WDY("未抵押","wdy"),
    YDY("已抵押","ydy"),
    ECDY("二次抵押","ecdy"),
    WCF("未查封","wcf"),
    YCF("已查封","ycf"),
    WYY("未异议","wyy"),
    YYY("已异议","yyy");

    private String keyWord;
    private String keyCode;

    KEY_BDC_QSZT_DYXZZT_Enums(String keyWord, String keyCode){
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
