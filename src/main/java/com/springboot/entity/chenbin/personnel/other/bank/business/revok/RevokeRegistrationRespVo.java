package com.springboot.entity.chenbin.personnel.other.bank.business.revok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevokeRegistrationRespVo implements Serializable {
    private String revokeApplyId;
    private String revokeAcceptId;
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
