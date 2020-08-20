package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/18
 * description：布尔关键字映射
 */
public enum KEY_BOOLEAN_CODE_Enums {
    SZ("true","1"),      //是&真
    FJ("false","0");      //否&假

    private String keyWord;
    private String keyCode;

    KEY_BOOLEAN_CODE_Enums(String keyWord, String keyCode){
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
