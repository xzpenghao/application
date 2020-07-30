package com.springboot.entity.newPlat.query.bizData.fromSY.cqzs;

import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *   参与人信息（包含：代理人，委托人，通知人...）
 */


@NoArgsConstructor
@Data
public class Cyr {

    private String qlrmc;  // 权利人名称
    private String qlrzjzl; //  权利人证件种类  名称
    private String qlrzjhm; //   权利人证件号码
    private String dh;  //  电话
    private String dz;  //   地址
}
