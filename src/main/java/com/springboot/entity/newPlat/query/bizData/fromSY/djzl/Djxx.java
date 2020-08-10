package com.springboot.entity.newPlat.query.bizData.fromSY.djzl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/8/10
 * description：冻结信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Djxx {
    private String ywh;            //业务号
    private String bdcdyh;        //不动产单元号
    private String fwdm;          //房屋代码
    private String djqx;          //冻结期限
    private String djkssj;        //冻结开始时间
    private String djjssj;        //冻结结束时间
    private String djsj;          //登记时间
    private String xszt;          //现势状态
    private String fj;            //附记
    private String djyy;          //登记原因
}
