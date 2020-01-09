package com.springboot.entity.chenbin.personnel.bdc;

import java.io.Serializable;
import java.util.List;

public class SynNewEcertsReqEntity implements Serializable {
    private boolean fileDealModel;          //true-ftp/false-本地保存
    private String localPath;
    private String commonInterfaceAttributer;
    private List<String> registNumbers;

    public SynNewEcertsReqEntity(boolean fileDealModel, String localPath ,String commonInterfaceAttributer, List<String> registNumbers) {
        this.fileDealModel = fileDealModel;
        this.localPath = localPath;
        this.commonInterfaceAttributer = commonInterfaceAttributer;
        this.registNumbers = registNumbers;
    }

    public boolean getFileDealModel() {
        return fileDealModel;
    }

    public void setFileDealModel(boolean fileDealModel) {
        this.fileDealModel = fileDealModel;
    }

    public String getCommonInterfaceAttributer() {
        return commonInterfaceAttributer;
    }

    public void setCommonInterfaceAttributer(String commonInterfaceAttributer) {
        this.commonInterfaceAttributer = commonInterfaceAttributer;
    }

    public List<String> getRegistNumbers() {
        return registNumbers;
    }

    public void setRegistNumbers(List<String> registNumbers) {
        this.registNumbers = registNumbers;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
