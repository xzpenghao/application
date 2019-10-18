package com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MortgageeInfoVo implements Serializable {
    private String mortgageeName;
    private String mortgageeIdType;
    private String mortgageeId;
}
