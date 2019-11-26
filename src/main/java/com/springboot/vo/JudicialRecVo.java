package com.springboot.vo;

import lombok.Data;

@Data
public class JudicialRecVo {
    private String bdczh;//不动产证号/证明号
    private String type;//标志位
    private String djjg;//登记机构
    private String inquirer;//查询人
    private String workId;//工作证号
    private String officialId;//查询公务号
    private String remark;//备注
}
