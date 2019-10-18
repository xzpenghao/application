package com.springboot.entity.chenbin.personnel.other.bank.query.progress.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateUnitInfoVo implements Serializable {
    private String realEstateUnitId;
    private String householdId;
}
