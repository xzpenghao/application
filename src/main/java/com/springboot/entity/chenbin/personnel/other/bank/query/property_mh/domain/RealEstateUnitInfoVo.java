package com.springboot.entity.chenbin.personnel.other.bank.query.property_mh.domain;

import com.springboot.entity.chenbin.personnel.other.bank.query.property_mh.domain.ObligeeInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateUnitInfoVo implements Serializable {

    // 证书信息
    private String realEstateId;
    private String vormerkungId;
    private String landCertificate;
    private String abnormal;
    // 房屋信息
    private String realEstateUnitId;
    private String attachmentStatus;
    private String mortgageStatus;
    private String objectionStatus;
    private String householdId;
    private String buildingId;
    private String accountId;
    private String sit;
    private String roomId;
    private String unitId;
    private String projectName;
    private String architectureAera;
    private String architectureName;

    // 权利人信息
    private List<ObligeeInfoVo> obligeeInfoVoList;
}
