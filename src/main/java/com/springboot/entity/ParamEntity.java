package com.springboot.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParamEntity {

    private String bdcdyh;//不动产单元号
    private String htbah;//合同备案号
    private String ComDate;//信息修改时间
    private int orgId ; //必传默认1


    public String getComDate() {
        return ComDate;
    }

    public void setComDate(String comDate) {
        ComDate = comDate;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getHtbah() {
        return htbah;
    }

    public void setHtbah(String htbah) {
        this.htbah = htbah;
    }

}
