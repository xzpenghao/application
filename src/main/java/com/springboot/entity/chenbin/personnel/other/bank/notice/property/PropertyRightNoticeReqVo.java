package com.springboot.entity.chenbin.personnel.other.bank.notice.property;

import com.springboot.entity.chenbin.personnel.other.bank.notice.property.domain.MortgageeInfoVo;
import com.springboot.entity.chenbin.personnel.other.bank.notice.property.domain.MortgagorInfoVo;
import com.springboot.entity.chenbin.personnel.other.bank.notice.property.domain.RealEstateUnitInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyRightNoticeReqVo {

    private String realEstateId;
    private String vormerkungId;
    private String landCertificate;
    private List<RealEstateUnitInfoVo> realEstateUnitInfoVoList;
    private List<MortgagorInfoVo> mortgagorInfoVoList;
    private List<MortgageeInfoVo> mortgageeInfoVoList;
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
