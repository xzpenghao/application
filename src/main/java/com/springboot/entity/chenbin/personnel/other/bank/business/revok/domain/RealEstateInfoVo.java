package com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateInfoVo implements Serializable {
    private String realEstateId;
    private String vormerkungId;
    private String landCertificate;
    private List<RealEstateUnitInfoVo> realEstateUnitInfoVoList;
}
