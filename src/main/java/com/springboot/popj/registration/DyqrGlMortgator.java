package com.springboot.popj.registration;

public class DyqrGlMortgator {
    private String mortgageeId;//证件号码
    private String mortgageeIdType;//证件类型编码
    private String mortgageeName;//姓名
    private String order;//顺序
    private String phone;
    private String address;


    public String getMortgageeId() {
        return mortgageeId;
    }

    public void setMortgageeId(String mortgageeId) {
        this.mortgageeId = mortgageeId;
    }

    public String getMortgageeIdType() {
        return mortgageeIdType;
    }

    public void setMortgageeIdType(String mortgageeIdType) {
        this.mortgageeIdType = mortgageeIdType;
    }

    public String getMortgageeName() {
        return mortgageeName;
    }

    public void setMortgageeName(String mortgageeName) {
        this.mortgageeName = mortgageeName;
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
