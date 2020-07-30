package com.springboot.entity.newPlat.query.bizData.fromSY.cqzs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/7/29/029
 * description：异议信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Yyxx {
    private String ywh;     //业务号
    private String bdcdyh;  //不动产单元号
    private String fwdm;    //房屋代码
    private String zt;      //现势状态
}
