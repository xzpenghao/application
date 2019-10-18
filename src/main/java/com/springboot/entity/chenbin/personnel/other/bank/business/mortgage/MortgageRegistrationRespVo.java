package com.springboot.entity.chenbin.personnel.other.bank.business.mortgage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MortgageRegistrationRespVo {

    private String mortgageApplyId;
    private String mortgageAcceptId;
    private String acceptStatus;
    private String acceptMessage;
    private String businessId;

    private String respDate;
    private String respCode;
    private String respMsg;
    private String apiName;
    private String charst;
    private String orgId;
    private String version;
    private String reqUniqueNo;
}
