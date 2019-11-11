package com.springboot.entity.chenbin.personnel.other.other_depart;

import java.io.Serializable;

public class JFEntity implements Serializable {
    private String jfReason;
    private String jfResult;
    private String jfAmount;
    private String jfPerson;
    private String jfTime;

    public JFEntity() {
        super();
    }

    public JFEntity(String jfReason, String jfResult, String jfAmount, String jfPerson, String jfTime) {
        this.jfReason = jfReason;
        this.jfResult = jfResult;
        this.jfAmount = jfAmount;
        this.jfPerson = jfPerson;
        this.jfTime = jfTime;
    }

    public String getJfReason() {
        return jfReason;
    }

    public void setJfReason(String jfReason) {
        this.jfReason = jfReason;
    }

    public String getJfResult() {
        return jfResult;
    }

    public void setJfResult(String jfResult) {
        this.jfResult = jfResult;
    }

    public String getJfAmount() {
        return jfAmount;
    }

    public void setJfAmount(String jfAmount) {
        this.jfAmount = jfAmount;
    }

    public String getJfPerson() {
        return jfPerson;
    }

    public void setJfPerson(String jfPerson) {
        this.jfPerson = jfPerson;
    }

    public String getJfTime() {
        return jfTime;
    }

    public void setJfTime(String jfTime) {
        this.jfTime = jfTime;
    }
}
