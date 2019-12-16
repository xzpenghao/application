package com.springboot.popj.registration;

public class BDCSalerInfo {
    private String salerId;//证件号码
    private String salerIdType;//证件类型编码
    private String salerName;//姓名
    private String order;
    private String phone;
    private String address;

    public String getSalerId() {
        return salerId;
    }

    public void setSalerId(String salerId) {
        this.salerId = salerId;
    }

    public String getSalerIdType() {
        return salerIdType;
    }

    public void setSalerIdType(String salerIdType) {
        this.salerIdType = salerIdType;
    }

    public String getSalerName() {
        return salerName;
    }

    public void setSalerName(String salerName) {
        this.salerName = salerName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
