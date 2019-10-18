package com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateInfoVo {
    private String realEstateId;
    private String vormerkungId;
    private String landCertificate;
    private List<RealEstateUnitInfoVo> realEstateUnitInfoVoList;
}
