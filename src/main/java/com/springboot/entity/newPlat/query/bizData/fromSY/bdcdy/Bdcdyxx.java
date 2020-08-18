package com.springboot.entity.newPlat.query.bizData.fromSY.bdcdy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/8/17
 * description：不动产单元信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bdcdyxx {
    private String bdcdyh;          //不动产单元号
    private String cfzt;            //查封状态
    private String dyzt;            //抵押状态
    private String yyzt;            //异议状态
    private String fwdm;            //房屋代码
    private String zh;              //幢号
    private String hh;              //户号
    private String zl;              //坐落
    private String fjh;             //房间号
    private String dyh;             //单元号
    private String xmmc;            //项目名称
    private String jzmj;            //建筑面积
    private String jzmc;            //建筑名称
    private String sfyc;            //是否预测
    private String yywh;            //原业务号
    private String yqllx;           //原权利类型
}
