package com.springboot.entity.chenbin.personnel.other.bank;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReMortgageRegistrationVo {

    private String mortgageApplyId;//机构抵押申请编号
    private String mortgageAcceptId;//平台抵押受理编号
    private String acceptStatus;//抵押业务接收状态
    private String acceptMessage;//抵押业务接收信息
    private String businessId;//登记中心业务号



    private String apiName;
    private String charst;

    private String orgId;
    private String version;
    private String reqUniqueNo;
    private String respDate;
    private String respCode;
    private String respMsg;

}
