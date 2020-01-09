package com.springboot.entity.chenbin.personnel.bdc;

import java.io.Serializable;
import java.util.List;

public class SynNewEcertEntity implements Serializable {
    private String prjid;
    private List<SynNewEcertInfoEntity> elecLicenseInfoList;

    public String getPrjid() {
        return prjid;
    }

    public void setPrjid(String prjid) {
        this.prjid = prjid;
    }

    public List<SynNewEcertInfoEntity> getElecLicenseInfoList() {
        return elecLicenseInfoList;
    }

    public void setElecLicenseInfoList(List<SynNewEcertInfoEntity> elecLicenseInfoList) {
        this.elecLicenseInfoList = elecLicenseInfoList;
    }
}
