package com.springboot.entity.chenbin.personnel.pub_use;

import java.io.Serializable;

public class SJ_Sjsq_User_Ext implements Serializable {

    private String id;                              //id
    private String receiptNumber;                   //收件编号
    private String adaptSys;                        //适配的系统
    private String userCode;                        //用户标识
    private String userName;                        //用户名
    private String dataInitMethod;                  //数据产生方式
    private String bizCode;                         //业务标识
    private String bizName;                         //不动产标准业务名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getAdaptSys() {
        return adaptSys;
    }

    public void setAdaptSys(String adaptSys) {
        this.adaptSys = adaptSys;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDataInitMethod() {
        return dataInitMethod;
    }

    public void setDataInitMethod(String dataInitMethod) {
        this.dataInitMethod = dataInitMethod;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }
}
