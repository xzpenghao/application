package com.springboot.entity.newPlat.query.bizData.fromSY.cqzs;

import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *   宗地调查信息信息
 */

@NoArgsConstructor
@Data
public class Zddcxx {

    private String zdlx; // 宗地类型
    private String zdtybm; //  宗地统一编码
    private String bdcdyh;  // 不动产单元号
    private String djh;  //  地籍号
    private String tdzl;  // 土地坐落
    private String tdyt;  // 土地用途
    private String dytdmj; // 独用土地面积
    private String fttdmj; // 分摊土地面积
    private String tdid;  //土地ID
    private String tdqsrq; // 土地起始日期 yyyy-MM-dd
    private String tdzzrq; // 土地终止日期 yyyy-MM-dd
}
