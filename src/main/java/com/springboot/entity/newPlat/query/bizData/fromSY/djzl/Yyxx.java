package com.springboot.entity.newPlat.query.bizData.fromSY.djzl;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/***
 * 异议信息
 */

@NoArgsConstructor
@Data
public class Yyxx {

    private String fwdm; // 房屋代码
    private String bdcdyh; // 不动产单元号
    private String yysx;  // 异议事项
    private String djsj; //  登记时间 yyyy-MM-dd HH:mm:ss
    private String qsztmc;// 权属状态 1 现势 放名称
    private String fj; // 附记
    private String yysqr; // 异议申请人
    private String yysqrzjh; // 申请人证件号码
}
