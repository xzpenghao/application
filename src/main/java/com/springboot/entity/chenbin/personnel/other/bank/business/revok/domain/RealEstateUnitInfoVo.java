package com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain;

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
}
