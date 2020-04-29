package com.springboot.entity.chenbin.personnel.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 电力的分发请求实体
 * @author chenb
 * @version 2020/4/29/029
 * description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DLReqEntity {
    private String originalUserName;
    private String originalUserCard;
    private String newUserName;
    private String newOriginalUserCard;
    private String newMobile;
    private String qxno;
    private String bdcno;
    private String adress;
    private String contractId;
    private String orgNo;
    private List<DLFile> data;
}
