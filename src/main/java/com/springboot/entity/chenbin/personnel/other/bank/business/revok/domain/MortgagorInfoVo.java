package com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MortgagorInfoVo implements Serializable {
    private String mortgagorNmae;
    private String mortgagorIdType;
    private String mortgagorId;
}
