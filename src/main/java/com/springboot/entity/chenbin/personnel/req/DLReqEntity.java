package com.springboot.entity.chenbin.personnel.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private String originalUserName;        //原用户名
    private String originalUserCard;        //原用户身份证
    private String newUserName;             //新用户名
    private String newOriginalUserCard;     //新用户身份证
    private String newMobile;               //新用户联系方式
    private String qxno;                    //新不动产权证号
    private String bdcno;                   //不动产单元号
    private String adress;                  //地址（坐落）
    private String contractId;              //一窗收件编号
    private String orgNo;                   //32413
    private List<DLFile> data = new ArrayList<>();  //附件信息
}
