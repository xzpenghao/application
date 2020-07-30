package com.springboot.entity.newPlat.query.bizData.fromSY.djzl;

import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Cyr;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *   权利人
 */


@NoArgsConstructor
@Data
public class Qlr extends Cyr {

    private String gyfs; // 共有方式   名称
    private String gyfe; //  共有份额（权利比例）
    private String fbczzh;// 分别持证证号
    private String dlrmc;   //代理人名称
    private String dlrzjlx; //代理人证件类型
    private String dlrzjh;  //代理人证件号码
}
