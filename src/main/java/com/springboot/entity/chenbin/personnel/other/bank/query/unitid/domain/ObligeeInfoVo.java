package com.springboot.entity.chenbin.personnel.other.bank.query.unitid.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObligeeInfoVo implements Serializable {
    private String obligeeName;
    private String obligeeIdType;
    private String obligeeId;
}
