package com.springboot.entity.chenbin.personnel.other.bank.business.revok;

import com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain.AgentInfoVo;
import com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain.FileInfoVo;
import com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain.MortgagorInfoVo;
import com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain.RealEstateInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevokeRegistrationReqVo implements Serializable {
    private String revokeApplyId;
    private String revokeApplyDate;
    private String mortgageApplyId;
    private String mortgageAcceptId;
    private String businessId;
    private String warrantId;
    private List<RealEstateInfoVo> realEstateInfoVoList;
    private List<MortgagorInfoVo> mortgagorInfoVoList;
    private List<FileInfoVo> fileInfoVoList;
    private String operatorName;
    private String operatorId;
    private String operatorSecretKey;
    private String operatorPhone;
    private String contacts;
    private String contactsPhone;
    private String contactsAdress;
    private String removeAcceptId;
    private String removeReason;
    private String macAdress;
    private String note;
    private List<AgentInfoVo> agentInfoVoList;
    private String businessAreas;

    private String notifyUrl;
    private String reqDate;
    private String apiName;
    private String charst;
    private String orgId;
    private String version;
    private String reqUniqueNo;
}
