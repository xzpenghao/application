package com.springboot.entity.chenbin.personnel.other.other_depart;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class BookReturnEntity implements Serializable {
    @ApiModelProperty(value = "受理编号")
    private String slbh;
    @ApiModelProperty(value = "是否加载登簿数据标识(空或者0为加载；1为不加载)")
    private String isLoadBook;
    @ApiModelProperty(value = "电子证书类型列表: 0 - 电子证书、1 - 预告电子证明、2 - 抵押电子证明、3 - 注销电子证书、4 - 注销预告电子证明、5 - 注销抵押电子证明")
    private List<String> dzzslxs;

    public String getSlbh() {
        return slbh;
    }

    public void setSlbh(String slbh) {
        this.slbh = slbh;
    }

    public String getIsLoadBook() {
        return isLoadBook;
    }

    public void setIsLoadBook(String isLoadBook) {
        this.isLoadBook = isLoadBook;
    }

    public List<String> getDzzslxs() {
        return dzzslxs;
    }

    public void setDzzslxs(List<String> dzzslxs) {
        this.dzzslxs = dzzslxs;
    }
}
