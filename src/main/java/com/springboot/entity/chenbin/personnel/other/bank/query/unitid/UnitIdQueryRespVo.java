package com.springboot.entity.chenbin.personnel.other.bank.query.unitid;

import com.springboot.entity.chenbin.personnel.other.bank.query.unitid.domain.ObligeeInfoVo;
import com.springboot.entity.chenbin.personnel.other.bank.query.unitid.domain.RealEstateUnitInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitIdQueryRespVo implements Serializable {
    private String queryId;
    private String realEstateId;
    private String vormerkungId;
    private String landCertificate;
    private String abnormal;
    private List<RealEstateUnitInfoVo> realEstateUnitInfoVoList;
    private List<ObligeeInfoVo> obligeeInfoVoList;

    private String respDate;
    private String respCode;
    private String respMsg;
    private String apiName;
    private String charst;
    private String orgId;
    private String version;
    private String reqUniqueNo;
}
