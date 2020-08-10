package com.springboot.entity.newPlat.query.resp;


import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.*;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Cfxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Djxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Qlr;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Yyxx;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.*;

/***
 *  产权查询 响应实体
 */

@NoArgsConstructor
@Data
public class CqzsResponse {

    private String ywh; // 业务号
    private String djlx; // 登记类型
    private String bdczh; // 不动产证号
    private String   djrq; //登记日期  yyyy-MM-dd HH:mm:ss
    private String bdclx; // 不动产类型 “FW”（房屋）；“ZD”（宗地）
    private String zslx; // 证书类型   “房屋不动产证-0”、“土地不动产证-1”
    private String zl; // 坐落
    private String fj; // 附记
    private String qtzk; //  其他状况
    private String  qtxz;   //登记其它限制（其它限制,内部限制/其它限制/内部限制/无或返回空值）

    private Fwxgqlxx fwxgqlxx; //  房屋相关权利信息
    private Tdxgqlxx tdxgqlxx; //  土地相关权利信息
    private Ygxx ygxx;         // 预告信息列表
    private List<Qlr> qlrlb; // 权利人列表
    private List<Qlr> ywrlb;  // 义务人列表
    private List<Cyr> qldlrlb; // 权利代理人列表
    private List<Cyr> ywdlrlb; // 义务代理人列表
    private List<Fwdcxx> fwdcxxlb; //  房屋调查信息列表
    private List<Zddcxx> zddcxxlb; //  宗地调查信息列表
    private List<Dyxx> dyxxlb;  //  抵押信息列表
    private List<Cfxx> cfxxlb; //  查封信息列表
    private List<Yyxx> yyxxlb; //  异议信息列表
    private List<Djxx> djxxlb;              //冻结信息列表

    /**
     * 描述：返回数据的基础自检方法
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[]
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void checkSelfStandard(){
        //证书类型范围检查
        if(!BDC_CERT_TYPE_FW.equals(this.zslx) && !BDC_CERT_TYPE_TD.equals(this.zslx)){
            throw new ZtgeoBizException("【产权证书-信息核验】证书类型取值超出定义");
        }
        //不动产类型与调查信息核验
        if(StringUtils.isNotBlank(this.bdclx) && !BDC_TYPE_FD.equals(this.bdclx) && !BDC_TYPE_JD.equals(this.bdclx)){
            throw new ZtgeoBizException("【产权证书-信息核验】不动产类型取值超出定义");
        }else if(BDC_TYPE_JD.equals(this.bdclx)){
            if(this.zddcxxlb==null || this.zddcxxlb.size()<1)
                throw new ZtgeoBizException("【产权证书-信息核验】不动产类型设置为<ZD>时，宗地调查信息未给出");
        }else{
            if(this.fwdcxxlb==null || this.fwdcxxlb.size()<1)
                throw new ZtgeoBizException("【产权证书-信息核验】不动产类型设置为<FW>时，房屋调查信息未给出");
            this.bdclx = BDC_TYPE_FD;
        }
        //权利信息的基本核验
        if(this.tdxgqlxx==null){
            throw new ZtgeoBizException("【产权证书-信息核验】缺失基础土地权利信息");
        }
    }
}
