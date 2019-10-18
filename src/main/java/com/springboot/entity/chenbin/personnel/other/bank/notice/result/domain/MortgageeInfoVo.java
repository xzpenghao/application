package com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MortgageeInfoVo {
    private String mortgageeName;
    private String mortgageeIdType;
    private String mortgageeId;
}
