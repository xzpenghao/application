package com.springboot.entity.chenbin.personnel.other.bank.query.progress;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressQueryReqVo implements Serializable {
    private String businessId;
    private String businessType;
    private String orgApplyId;
    private String platformAcceptId;

    private String notifyUrl;
    private String reqDate;
    private String apiName;
    private String charst;
    private String orgId;
    private String version;
    private String reqUniqueNo;
}
