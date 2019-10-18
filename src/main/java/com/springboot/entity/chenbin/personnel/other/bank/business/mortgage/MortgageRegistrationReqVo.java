package com.springboot.entity.chenbin.personnel.other.bank.business.mortgage;

import com.alibaba.fastjson.annotation.JSONType;
import com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MortgageRegistrationReqVo {
    private String mortgageApplyId;
    private String mortgageApplyDate;
    private String mortgageType;
    private String mortgageWay;
    private String mortgageArea;
    private String creditAmount;
    private String evaluationValue;
    private String mortgageTerm;
    private String mortgageStartDate;
    private String mortgageEndDate;
    private String mortgageReason;
    private String note;
    private String absoluteFact;
    private String highestClaimAmount;
    private List<MortgageeInfoVo> mortgageeInfoVoList;
    private List<MortgagorInfoVo> mortgagorInfoVoList;
    private List<ObligorInfoVo> obligorInfoVoList;
    private List<AgentInfoVo> agentInfoVoList;
    private List<RealEstateInfoVo> realEstateInfoVoList;
    private List<FileInfoVo> fileInfoVoList;
    private String operatorName;
    private String operatorId;
    private String operatorSecretKey;
    private String operatorPhone;
    private String contacts;
    private String contactsPhone;
    private String contactsAdress;
    private String macAdress;
    private String businessAreas;
    private String mortgageLandArea;

    private String notifyUrl;
    private String reqDate;
    private String apiName;
    private String charst;
    private String orgId;
    private String version;
    private String reqUniqueNo;

}
