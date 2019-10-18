package com.springboot.entity.chenbin.personnel.other.bank.query.progress;

import com.springboot.entity.chenbin.personnel.other.bank.query.progress.domain.FileInfoVo;
import com.springboot.entity.chenbin.personnel.other.bank.query.progress.domain.RealEstateInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressQueryRespVo implements Serializable {
    private String businessId;
    private String businessStatus;
    private String businessMessage;
    private String businessType;
    private String completionTime;
    private String businessNodeCode;
    private String businessNodeName;
    private List<RealEstateInfoVo> realEstateInfoVoList;
    private String warrantId;
    private List<FileInfoVo> fileInfoVoList;
    private String orgApplyId;
    private String platformAcceptId;

    private String respDate;
    private String respCode;
    private String respMsg;
    private String apiName;
    private String charst;
    private String orgId;
    private String version;
    private String reqUniqueNo;
}
