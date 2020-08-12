/**
 * Copyright 2020 bejson.com
 */
package com.springboot.entity.newPlat.transInner.req.fromZY.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dyxx {

    private BigDecimal bdbzzqse;            //被担保主债权数额
    private String dbfw;                    //担保范围
    private String dyfs;                    //抵押方式
    private String dyyy;                    //抵押原因
    private String zwlxjssj;                //债务履行结束时间
    private String zwlxqssj;                //债务履行起始时间
    private String dyhtqdrq;                //抵押合同签订日期
    private String bdcjz;                   //不动产价值
    private String dysx;                    //抵押顺序
    private String yywh;                    //原业务号
    private String yqllx;                   //原权利类型
    private String zwr;                     //债务人
    private String zwrzjhm;                 //债务人证件号码
    private String zwrzjlx;                 //债务人证件类型
    private List<Sqrxx> sqrxx;              //申请人信息
    private List<Fjxx> fjxx;                //附件信息
}