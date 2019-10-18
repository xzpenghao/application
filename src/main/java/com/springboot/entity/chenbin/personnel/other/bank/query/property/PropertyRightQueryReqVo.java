package com.springboot.entity.chenbin.personnel.other.bank.query.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyRightQueryReqVo implements Serializable {
    private String queryId;
    private String obligeeName;
    private String obligeeIdType;
    private String obligeeId;
    private String realEstateId;
    private String vormerkungId;

    private String notifyUrl;
    private String reqDate;
    private String apiName;
    private String charst;
    private String orgId;
    private String version;
    private String reqUniqueNo;
}
