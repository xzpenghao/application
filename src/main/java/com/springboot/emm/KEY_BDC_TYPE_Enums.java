package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/7
 * description：不动产类型
 */
public enum KEY_BDC_TYPE_Enums {
    FD("房地","FW"),
    ZD("宗地","ZD");

    private String keyWord;
    private String keyCode;

    KEY_BDC_TYPE_Enums(String keyWord, String keyCode){
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
