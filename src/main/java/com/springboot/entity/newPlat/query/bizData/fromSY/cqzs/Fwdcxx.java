package com.springboot.entity.newPlat.query.bizData.fromSY.cqzs;

import lombok.Data;
import lombok.NoArgsConstructor;


/***
 *  房屋调查信息信息
 */

@NoArgsConstructor
@Data
public class Fwdcxx {

    private String bdcdyh; //  不动产单元号
    private String hbh;  // 户编号
    private String zbh;  // 幢编号
    private String hh;   // 户号
    private String zl;   // 坐落
    private String fjh;  // 房间号
    private String dyh;  // 单元号
    private String szc;  // 所在层
    private String zcs;  // 总层数
    private String xmmc; // 项目名称
    private String zjmc; // 建筑名称
    private String jzmj; // 建筑面积  默认字符串接收
    private String tnjzmj;  // 套内建筑面积
    private String ftjzmj;  //  分摊建筑面积
    private String fwghyt;   //  房屋规划用途
    private String fwlx;    //  房屋类型
    private String fwxz;    //  房屋性质
    private String fwqllx;  //  房屋权利类型
    private String fwqlxz;  //  房屋权利性质
    private String tdqlxz;  //  土地权利性质

    private String tdqsrq;    //  土地起始日期 yyyy-MM-dd
    private String tdzzrq;  // 土地终止日期 yyyy-MM-dd
    private String tdsyqr;  //  土地使用权人
    private String tdsyqx;  //土地使用期限
    private String tdyt;  // 土地用途
    private String gytdmj; // 共有土地面积
    private String fttdmj; // 分摊土地面积
    private String dytdmj; // 独用土地面积
    private String fwjg;  // 房屋结构
    private String fwdm;  // 房屋代码
    private String sslb;    //设施类别
}
