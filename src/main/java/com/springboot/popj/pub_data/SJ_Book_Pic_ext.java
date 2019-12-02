package com.springboot.popj.pub_data;

import com.springboot.entity.SJ_Fjfile;
import com.springboot.util.TimeUtil;

import java.io.Serializable;
import java.util.Date;

public class SJ_Book_Pic_ext implements Serializable {
    private String id;
    private String infoId;
    private String picName;
    private String picType;             //分层分户图或者宗地图
    private String bdcdyh;
    private String fileId;
    private String insertTime;

    private SJ_Fjfile file;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = TimeUtil.getTimeString(insertTime);

    }

    public SJ_Fjfile getFile() {
        return file;
    }

    public void setFile(SJ_Fjfile file) {
        this.file = file;
    }
}
