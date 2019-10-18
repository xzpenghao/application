package com.springboot.entity.chenbin.personnel.other.bank.notice.result;

import com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultNoticeReqVo {
    private String businessId;
    private String businessStatus;
    private String businessMessage;
    private String businessType;
    private String completionTime;
    private String businessNodeCode;
    private String businessNodeName;
    private List<RealEstateInfoVo> realEstateInfoVoList;
    private String warrantId;
    private List<FileInfoVo> fileInfoVoList;
    private String orgApplyId;
    private String platformAcceptId;
    private List<MortgagorInfoVo> mortgagorInfoVoList;
    private List<MortgageeInfoVo> mortgageeInfoVoList;
    private List<ObligorInfoVo> obligorInfoVoList;
    private String mortagageLandArea;
    private String mortgageArea;
    private String creditAmount;
    private String mortgageTerm;
    private String oldWarrantId;
    private String businessAreasCode;

    private String apiName;
    private String charset;
    private String orgId;
    private String notifyUrl;
    private String version;
    private String reqDate;
    private String reqUniqueNo;

}
