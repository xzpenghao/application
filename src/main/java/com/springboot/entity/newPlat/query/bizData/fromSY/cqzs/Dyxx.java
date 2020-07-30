package com.springboot.entity.newPlat.query.bizData.fromSY.cqzs;

import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Qlr;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 抵押信息
 */

@NoArgsConstructor
@Data
public class Dyxx {


    private String ywh; // 业务号
    private String bdcdyh;  //不动产单元号
    private String dyzmh;  // 抵押证明号
    private String dyje;   // 抵押金额
    private String zqse;   // 债权数额
    private String pgjz;   // 评估价值
    private String dymj;   // 抵押面积
    private String dysw;  //  抵押顺位
    private String dyfs;  //  抵押方式
    private String dylx;  // 抵押类型
    private String dysllx;  //  抵押设立类型
    private String dyyy;   //   抵押原因
    private String dykssj;  //  抵押开始时间  yyyy-MM-dd
    private String dyjssj;  //  抵押结束时间  yyyy-MM-dd
    private String djsj;   //   登记时间   yyyy-MM-dd HH:mm:ss
    private String zt;    //  状态
    private String xgzh;  //  相关证号
    private String dbfw;   //  担保范围
    private String fj;    //   附记
    private String qt;    //   其它
    private List<Qlr> dyqrlb;  //  抵押权人
    private List<Qlr> dyrlb;  //   抵押人
}
