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

    private String qlbid;
    private String gyfs; // 共有方式   名称
    private String gyfe; //  共有份额（权利比例）
    private String fbczzh;// 分别持证证号
    private String ryzl; //  人员种类
    private String zjlx; //
    private String zjhm; //
}
