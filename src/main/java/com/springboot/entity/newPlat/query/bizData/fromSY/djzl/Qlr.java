package com.springboot.entity.newPlat.query.bizData.fromSY.djzl;

import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *   权利人
 */


@NoArgsConstructor
@Data
public class Qlr {

    private String qlrmc; //  产权人
    private String qlrzjzl; // 产权人证件种类   名称
    private String qlrzjhm;//  产权证证件号码
    private String dh;    //  电话
    private String dz;    //  地址
    private String gyfs; // 共有方式   名称
    private String gyfe; //  共有份额（权利比例）
    private String fbczzh;// 分别持证证号
}
