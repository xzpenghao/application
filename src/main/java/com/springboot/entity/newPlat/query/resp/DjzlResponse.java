package com.springboot.entity.newPlat.query.resp;

import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Cfxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Dyxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Qlr;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Yyxx;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@NoArgsConstructor
@Data
public class DjzlResponse {

    private String id;  //
    private String ywh;
    private String djlx; // 登记类型   100  首次登记
    private String djlxmc; // 登记类型名称
    private String bdcdyh; //不动产单元号
    private String fwdm; // 房屋代码
    private String zddm; // 宗地代码
    private String bdcqzh; // 产权证号
    private String fwlxmc; // 房屋类型
    private String fwlx;

    private String ytmc; // 房屋用途
    private String zl; // 坐落
    private String fwxzmc; // 房屋性质
    private String fwjgmc; //房屋结构
    private Integer szc; // 所在层(实际层)
    private Integer zcs; // 总层数
    private String  fwxzlx; // 房屋性质类型

    private String myc; // 名义层
    private String zh; // 自然幢号
    private String fjh; // 房间号

    private String jzmj; // 建筑面积
    private String tnjzmj; // 套内建筑面积
    private String ftjzmj; // 分摊建筑面积

    private String djsj; // 登记时间  yyyy-MM-dd HH:mm:ss
    private String fzsj; // 发证时间  yyyy-MM-dd HH:mm:ss

    private String qllxmc; // 权利类型
    private String qlqssj; // 权利起始时间  yyyy-MM-dd
    private String qljssj; //权利结束时间   yyyy-MM-dd
    private String qlxzmc; //权利性质
    private String tdytmc; // 土地用途
    private String fj; // 附记

    private String  tdsyqmj; //土地使用权面积 平方米
    private String  fttdmj; //分摊土地面积  平方米
    private String  dytdmj; //独用土地面积  平方米


    private List<Yyxx> yyxxs;  // 异议信息列表
    private List<Qlr> qlrVos;   // 权利人列表  // todo qlrs
    private List<Dyxx> dyxxs; // 抵押信息列表
    private List<Cfxx> cfxxs; // 查封信息列表
	
	public void checkSelfStandard(){
        if (StringUtils.isBlank(this.bdcdyh))
            throw new ZtgeoBizException("不动产单元号为空");
        if (StringUtils.isBlank(this.zl))
            throw new ZtgeoBizException("不动产坐落为空");
        if (StringUtils.isBlank(this.bdcqzh))
            throw new ZtgeoBizException("不动产证号为空");
    }
}
