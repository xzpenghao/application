package com.springboot.entity.chenbin.personnel.other.web;

import java.io.Serializable;

public class RequestParamQlr implements Serializable {
    private String obligeeName;
    private String obligeeDocumentType;
    private String obligeeDocumentNumber;
    private String dh;
    private String sharedMode;
    private String sharedValue;
    private String obligeeOrder;

    public String getObligeeName() {
        return obligeeName;
    }

    public void setObligeeName(String obligeeName) {
        this.obligeeName = obligeeName;
    }

    public String getObligeeDocumentType() {
        return obligeeDocumentType;
    }

    public void setObligeeDocumentType(String obligeeDocumentType) {
        this.obligeeDocumentType = obligeeDocumentType;
    }

    public String getObligeeDocumentNumber() {
        return obligeeDocumentNumber;
    }

    public void setObligeeDocumentNumber(String obligeeDocumentNumber) {
        this.obligeeDocumentNumber = obligeeDocumentNumber;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getSharedMode() {
        return sharedMode;
    }

    public void setSharedMode(String sharedMode) {
        this.sharedMode = sharedMode;
    }

    public String getSharedValue() {
        return sharedValue;
    }

    public void setSharedValue(String sharedValue) {
        this.sharedValue = sharedValue;
    }

    public String getObligeeOrder() {
        return obligeeOrder;
    }

    public void setObligeeOrder(String obligeeOrder) {
        this.obligeeOrder = obligeeOrder;
    }
}
