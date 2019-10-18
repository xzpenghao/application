package com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentInfoVo implements Serializable {
    private String agentName;
    private String agentIdType;
    private String agentId;
}