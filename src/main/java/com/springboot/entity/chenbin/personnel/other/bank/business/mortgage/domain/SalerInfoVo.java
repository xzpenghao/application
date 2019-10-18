package com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalerInfoVo implements Serializable {
    private String salerName;
    private String salerIdType;
    private String salerId;

}
