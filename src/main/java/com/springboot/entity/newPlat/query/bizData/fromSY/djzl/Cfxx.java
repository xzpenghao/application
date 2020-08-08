package com.springboot.entity.newPlat.query.bizData.fromSY.djzl;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 查封信息
 */

@NoArgsConstructor
@Data
public class Cfxx {

    private String id;
    private String ywh; // 业务号
    private String fwdm; // 房屋代码
    private String bdcdyh; // 不动产单元号
    private String cfjg; // 查封机构
    private String cflx; //  1 查封 2 轮候查封 3 预查封 4 轮候预查封 5 续封 放名称
    private String cflxmc; // 查封类型
    private String cfwh; //  查封文号
    private String cfqx;  //  查封期限
    private String cfkssj; //  查封开始(起始)时间    yyyy-MM-dd
    private String cfjssj; //  查封结束时间  yyyy-MM-dd
    private String  djsj; //  登记时间  yyyy-MM-dd HH:mm:ss
    private String  qszt; //   权属状态
    private String  fj;  // 附记
    private String cqzh; // 产权证号
    private String cfyy; // 查封原因
    private String zt; // 状态
    private String xgzh; // 相关证号
    private String sqzxr; //  申请执行人
    private String bzxr;  //  被执行人

}
