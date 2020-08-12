/**
 * Copyright 2020 bejson.com
 */
package com.springboot.entity.newPlat.transInner.req.fromZY.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fwxx {

    private String qllx;                //权利类型
    private String qlxz;                //权利性质
    private String qlqssj;              //土地权利起始时间
    private String qljssj;              //土地权利结束时间

    private String fwdm;                //房屋代码
    private String bdcdyh;              //不动产单元号
    private String zl;                  //坐落
    private String zh;                  //幢号
    private String dyh;                 //单元号
    private String fjh;                 //房间号
    private String fwjg;                //房屋结构
    private String fwxz;                //房屋性质
    private String ghyt;                //规划用途
    private String myc;                 //名义层
    private String sjc;                 //实际层
    private BigDecimal jzmj;                //建筑面积
    private BigDecimal dyjzmj;              //独用建筑面积
    private BigDecimal tnjzmj;              //套内建筑面积
    private BigDecimal ftjzmj;              //分摊建筑面积
    private String fzwdm;               //附着物代码

    private BigDecimal tddymj;              //土地独用面积
    private BigDecimal tdftmj;              //土地分摊面积
    private BigDecimal tdsyqmj;             //土地使用权面积
    private String tdyt;                //土地用途

    private String sfyc;                //是否预测0-实测/1-预测
}