package com.springboot.entity.newPlat.query.bizData.fromSY.djzl;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *  抵押信息
 */

@NoArgsConstructor
@Data
public class Dyxx {

    private String fwdm; // 房屋代码
    private String bdcdyh; //不动产单元号
    private String dyzmh; // 抵押证明号
    private String dyqr; // 抵押权人
    private String dyje; // 抵押金额 元 保留2位
    private String dykssj; // 抵押开始时间 yyyy-MM-dd
    private String dyjssj; // 抵押结束时间 yyyy-MM-dd
    private String djsj; //  登记时间  yyyy-MM-dd HH:mm:ss
    private String qszt; // 权属状态
    private String fj; //  附记
    private String cqzh; // 产权证号
    private String dyfs;  //  抵押方式  1 一般抵押 2 最高额抵押 放名称
    private String dbfw; // 担保范围
    private String dyr; //  抵押人
    private String dylx;// 抵押（不动产）类型
    private String zqje;//  债权金额  元 保留2位
}
