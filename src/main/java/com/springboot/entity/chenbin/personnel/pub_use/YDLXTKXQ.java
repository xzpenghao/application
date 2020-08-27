package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author chenb
 * @version 2020/8/27
 * description：应当履行条款详情
 */
public class YDLXTKXQ implements Serializable {
    @JsonProperty("TKXH")
    private String TKXH;        //条款序号
    @JsonProperty("TKNR")
    private String TKNR;        //条款内容

    public String getTKXH() {
        return TKXH;
    }
    @JsonIgnore
    public void setTKXH(String TKXH) {
        this.TKXH = TKXH;
    }

    public String getTKNR() {
        return TKNR;
    }
    @JsonIgnore
    public void setTKNR(String TKNR) {
        this.TKNR = TKNR;
    }
}
