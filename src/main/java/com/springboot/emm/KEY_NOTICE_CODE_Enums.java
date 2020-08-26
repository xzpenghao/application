package com.springboot.emm;

/**
 * @author chenb
 * @version 2020/7/28/028
 * description：通知类型关键字
 */
public enum KEY_NOTICE_CODE_Enums {
    SLJD("受理通知","acceptNotice"),
    SHJD("审核通知","verifyNotice"),
    DBJD("登簿通知","resultNotice"),
    FQJD("废弃通知","discardNotice");

    private String keyWord;
    private String keyCode;

    KEY_NOTICE_CODE_Enums(String keyWord, String keyCode){
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
