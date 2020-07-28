package com.springboot.entity.newPlat.query.bizData.fromSY.cqzs;

import lombok.Data;
import lombok.NoArgsConstructor;


/***
 *  土地相关权利信息
 */

@NoArgsConstructor
@Data
public class Tdxgqlxx {

    private String tdqllx; //  土地权利类型
    private String tdqlxz; //  土地权利性质
    private String tdsyqr; //  土地使用权人
    private String tdyt;  //   土地用途
    private String gytdmj; //  共有土地面积
    private String dytdmj;  //   独用土地面积
    private String fttdmj;    //   分摊土地面积
    private String jzzdmj;   //   建筑宗地面积
    private String  tdsyqx;   // 土地使用期限
    private String   qsrq;   //  起始日期  yyyy-mm-dd
    private String   zzrq;   //  终止日期  yyyy-mm-dd
    private String tdqdfs;  // 土地取得方式
}
