package com.springboot.entity.chenbin.personnel.OtherEntity;

import java.io.Serializable;
import java.util.List;

public class FcIndexAndTdzh implements Serializable {
    private Integer indexFc = 0;
    private String tdzh;
    private List<Integer> fsw_indexs;

    public Integer getIndexFc() {
        return indexFc;
    }

    public void setIndexFc(Integer indexFc) {
        this.indexFc = indexFc;
    }

    public String getTdzh() {
        return tdzh;
    }

    public void setTdzh(String tdzh) {
        this.tdzh = tdzh;
    }

    public List<Integer> getFsw_indexs() {
        return fsw_indexs;
    }

    public void setFsw_indexs(List<Integer> fsw_indexs) {
        this.fsw_indexs = fsw_indexs;
    }
}
