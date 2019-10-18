package com.springboot.entity.chenbin.personnel.other.bank.notice.sealed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SealedNoticeRespVo {
    private String acceptStatus;
    private String acceptMessage;

    private String respDate;
    private String respCode;
    private String respMsg;
    private String apiName;
    private String charst;
    private String orgId;
    private String version;
    private String reqUniqueNo;
}
