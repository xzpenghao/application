package com.springboot.popj.registration;

import java.util.List;

/**
 * 抵押注销
 */
public class RevokeBizInfo {
    private String revokeApplyDate;//业务申请日期
    private String warrantId;//抵押证明号
    private String revokeReason;//抵押原因
    private String registerSubType;//登记小类
    private List<RealEstateInfo> realEstateInfoVoList;//不动产权证列表

    public String getRegisterSubType() {
        return registerSubType;
    }

    public void setRegisterSubType(String registerSubType) {
        this.registerSubType = registerSubType;
    }

    public List<RealEstateInfo> getRealEstateInfoVoList() {
        return realEstateInfoVoList;
    }

    public void setRealEstateInfoVoList(List<RealEstateInfo> realEstateInfoVoList) {
        this.realEstateInfoVoList = realEstateInfoVoList;
    }

    public String getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(String revokePeason) {
        this.revokeReason = revokePeason;
    }

    public String getRevokeApplyDate() {
        return revokeApplyDate;
    }

    public void setRevokeApplyDate(String revokeApplyDate) {
        this.revokeApplyDate = revokeApplyDate;
    }

    public String getWarrantId() {
        return warrantId;
    }

    public void setWarrantId(String warrantId) {
        this.warrantId = warrantId;
    }
}
