package com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MortgagorAgentInfoVo implements Serializable {
    private String mortgagorAgentName;
    private String mortgagorAgentIdType;
    private String mortgagorAgentId;
}
