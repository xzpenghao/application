package com.springboot.entity.newPlat.query.resp;

import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.newPlat.query.bizData.Xgzxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Fwdcxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Zddcxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Cfxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Djxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Qlr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author chenb
 * @version 2020/7/27/027
 * description：抵押证明查询结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DyzmResponse {

    private String ywh;                 //业务号
    private String dyzmh;               //抵押证明号
    private String zl;                  //坐落
    private String djlx;                //登记类型
    private String djrq;                //登记日期
    private String dyfs;                //抵押方式
    private String dyyy;                //抵押原因
    private String dylx;                //抵押类型
    private String zqje;                //债权金额
    private String dyje;                //抵押金额
    private String dysllx;              //抵押设立类型（预告抵押/在建工程抵押/现房抵押/土地抵押）
    private String pgjz;                //评估价值
    private String dyqx;                //抵押期限
    private String dyqssj;              //抵押起始时间
    private String dyjssj;              //抵押结束时间
    private String dysw;                //抵押顺位
    private String zwr;                 //债务人
    private String zmbh;                //证明编号（序列号？）
    private String dbfw;                //担保范围
    private String dymj;                //抵押面积
    private String dytdmj;              //抵押土地面积
    private String bz;                  //备注
    private String zt;                  //状态
    private String fj;                  //附记
    private String qtzk;                //其它状况
    private String  qtxz;               //登记其它限制（其它限制,内部限制/其它限制/内部限制/无或返回空值）

    private List<Qlr> dyrlb;              //抵押人列表
    private List<Qlr> dyqrlb;             //抵押权人列表
    private List<Xgzxx> xgzlb;          //相关证列表
    private List<Fwdcxx> glfwdcxxlb;         //关联房屋调查信息列表
    private List<Zddcxx> zddcxxlb;           //宗地调查信息列表
    private List<Cfxx> cfxxlb;             //查封信息列表
    private List<Djxx> djxxlb;              //冻结信息列表

    public void checkSelfStandard(){
        if(StringUtils.isBlank(this.ywh))
            throw new ZtgeoBizException("获取不动产抵押信息业务号为空");
    }
}
