package com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObligeeInfoVo implements Serializable {
    private String obligeeName;
    private String obligeeIdType;
    private String obligeeId;
    private String commonWay;
    private String sharedShare;
}
