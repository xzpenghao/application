package com.springboot.entity.chenbin.personnel.other.bank.notice.sealed;

import com.springboot.entity.chenbin.personnel.other.bank.notice.sealed.domain.MortgageeInfoVo;
import com.springboot.entity.chenbin.personnel.other.bank.notice.sealed.domain.MortgagorInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SealedNoticeReqVo {
    private String realEstateId;
    private String vormerkungId;
    private String landCertificate;
    private String realEstateUnitId;
    private String sit;
    private String attachmentStatus;
    private List<MortgageeInfoVo> mortgageeInfoVoList;
    private List<MortgagorInfoVo> mortgagorInfoVoList;
    private String warrantId;
    private String businessAreasCode;

    private String apiName;
    private String charset;
    private String orgId;
    private String notifyUrl;
    private String version;
    private String reqDate;
    private String reqUniqueNo;
}
