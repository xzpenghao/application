package com.springboot.entity.chenbin.personnel.other.bank.query.property_mh;

import com.springboot.entity.chenbin.personnel.other.bank.query.property_mh.domain.RealEstateUnitInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyRightQueryRespVo implements Serializable {
    private String queryId;
    private List<RealEstateUnitInfoVo> realEstateUnitInfoVoList;
    private String respDate;
    private String respCode;
    private String respMsg;
    private String apiName;
    private String charst;
    private String orgId;
    private String version;
    private String reqUniqueNo;
}
