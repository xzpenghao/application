package com.springboot.entity.chenbin.personnel.other.bank.query.unitid.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateUnitInfoVo implements Serializable {
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
}
