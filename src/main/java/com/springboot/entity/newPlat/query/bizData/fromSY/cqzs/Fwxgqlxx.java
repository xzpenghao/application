package com.springboot.entity.newPlat.query.bizData.fromSY.cqzs;

import lombok.Data;
import lombok.NoArgsConstructor;



/***
 * 房屋相关权利信息
 */

@NoArgsConstructor
@Data
public class Fwxgqlxx {

    private String fwqllx; // 房屋权利类型
    private String fwqlxz; // 房屋权利性质
    private String jzmj;  // 建筑面积  保留4位小数
    private String tnjzmj; // 套内建筑面积   保留4位小数
    private String ftjzmj;   //  分摊建筑面积   保留4位小数
    private String fwqdfs;  // 房屋取得方式
    private String fwqdjg;  // 房屋获取价格
    private String fwghyt;  // 房屋规划用途
    private String pgjz;   //  评估价值
    private String fwlx;   //  房屋类型
    private String fwxz;  //   房屋性质
    private String fsss;    //附属设施
}
