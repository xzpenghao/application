package com.springboot.entity.newPlat.query.bizData.fromSY.cqzs;

import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *   权利代理人信息
 */


@NoArgsConstructor
@Data
public class Qldlr {

    private String qlrmc;  // 权利人名称
    private String qlrzjzl; //  权利人证件种类  名称
    private String qlrzjhm; //   权利人证件号码
    private String dh;  //  电话
    private String dz;  //   地址
}
