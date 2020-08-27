package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenb
 * @version 2020/8/27
 * description：合同补充信息
 */
public class HTBCXX implements Serializable {
    @JsonProperty("YDLXTK")
    private String YDLXTK;      //应当履行条款
    @JsonProperty("YDLXTKXQ")
    private List<YDLXTKXQ> YDLXTKXQ;    //应当履行条款详情
    @JsonProperty("WYSM")
    private WYSM WYSM;          //违约说明

    public String getYDLXTK() {
        return YDLXTK;
    }
    @JsonIgnore
    public void setYDLXTK(String YDLXTK) {
        this.YDLXTK = YDLXTK;
    }

    public List<com.springboot.entity.chenbin.personnel.pub_use.YDLXTKXQ> getYDLXTKXQ() {
        return YDLXTKXQ;
    }
    @JsonIgnore
    public void setYDLXTKXQ(List<com.springboot.entity.chenbin.personnel.pub_use.YDLXTKXQ> YDLXTKXQ) {
        this.YDLXTKXQ = YDLXTKXQ;
    }

    public com.springboot.entity.chenbin.personnel.pub_use.WYSM getWYSM() {
        return WYSM;
    }
    @JsonIgnore
    public void setWYSM(com.springboot.entity.chenbin.personnel.pub_use.WYSM WYSM) {
        this.WYSM = WYSM;
    }
}
