package com.springboot.entity.chenbin.personnel.other.bank.query.unitid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitIdQueryReqVo implements Serializable {
    private String queryId;
    private String obligeeName;
    private String obligeeIdType;
    private String obligeeId;
    private String realEstateUnitId;
    private String buildingId;
    private String roomId;
    private String unitId;

    private String notifyUrl;
    private String reqDate;
    private String apiName;
    private String apiCode;
    private String charst;
    private String orgId;
    private String version;
    private String reqUniqueNo;
}
