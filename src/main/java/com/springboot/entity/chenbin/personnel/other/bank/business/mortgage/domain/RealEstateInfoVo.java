package com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.domain;

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
    private String mortgage;
    private List<RealEstateUnitInfoVo> realEstateUnitInfoVoList;
    private List<ObligeeInfoVo> obligeeInfoVoList;
    private List<SalerInfoVo> salerInfoVoList;

}