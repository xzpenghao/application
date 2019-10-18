package com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateUnitInfoVo implements Serializable {
    private String realEstateUnitId;
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
