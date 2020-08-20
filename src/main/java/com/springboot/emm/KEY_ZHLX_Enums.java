package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/8/20
 * description：证号类型
 */
public enum KEY_ZHLX_Enums {
    BDCZH("不动产证","BDCZH"),
    DYZMH("抵押证明","DYZMH"),
    YGZMH("预告证明","YGZMH");

    private String keyWord;
    private String keyCode;

    KEY_ZHLX_Enums(String keyWord, String keyCode){
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
