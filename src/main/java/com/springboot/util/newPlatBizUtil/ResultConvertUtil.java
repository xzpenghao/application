package com.springboot.util.newPlatBizUtil;

import com.alibaba.fastjson.JSONArray;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.emm.*;
import com.springboot.entity.chenbin.personnel.other.paph.PaphCfxx;
import com.springboot.entity.chenbin.personnel.other.paph.PaphDjxx;
import com.springboot.entity.chenbin.personnel.other.paph.PaphDyxx;
import com.springboot.entity.chenbin.personnel.other.paph.PaphEntity;
import com.springboot.entity.chenbin.personnel.req.PaphReqEntity;
import com.springboot.entity.newPlat.query.bizData.Dbjgxx;
import com.springboot.entity.newPlat.query.bizData.Shxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.bdcdy.Bdcdyxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.*;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Cfxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Djxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Qlr;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Yyxx;
import com.springboot.entity.newPlat.query.resp.*;
import com.springboot.popj.pub_data.*;
import com.springboot.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.springboot.constant.chenbin.KeywordConstant.*;
import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.*;
import static com.springboot.constant.penghao.BizOrBizExceptionConstant.CERTIFICATE_TYPE_OF_FC;

/**
 * @author chenb
 * @version 2020/7/27/027
 * description：新平台适配性，结果转换工具类
 */
@Slf4j
public class ResultConvertUtil {
    /**
     * 描述：产权证号查询不动产权信息，包含它项权
     *          结果转换
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[cqzs]
     * 返回：SJ_Info_Bdcqlxgxx
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static SJ_Info_Bdcqlxgxx getImmoRightsByCqzs(CqzsResponse cqzs) throws ParseException {
        //核验接口数据，做基础自检
        cqzs.checkSelfStandard();
        //登记信息转换，创建返回实体
        SJ_Info_Bdcqlxgxx bdcqlxx = getRegisterInfoByCqzs(cqzs);
        if(bdcqlxx!=null) {
            //初始化权利信息
            initBdcqlxxByCqzs(bdcqlxx, cqzs);
            //初始化不动产调查信息
            initBdcdcxxByCqzs(bdcqlxx, cqzs);
            //初始化不动产他项权信息
            initBdctxqxxByCqzs(bdcqlxx, cqzs);
            //初始化权利人信息
            bdcqlxx.setGlObligeeVoList(initXgrxx(BDC_RYZL_QLR, cqzs.getQlrlb()));
            //初始化义务人信息
            bdcqlxx.setGlObligorVoList(initXgrxx(BDC_RYZL_YWR, cqzs.getYwrlb()));
            //初始化权利代理人信息
            bdcqlxx.setGlAgentVoList(initXgcyrxx(BDC_RYLX_DLR_QL, cqzs.getQldlrlb()));
            //初始化义务代理人信息
            bdcqlxx.setGlAgentObligorVoList(initXgcyrxx(BDC_RYLX_DLR_YW, cqzs.getYwdlrlb()));
        }
        //返回实体
        return bdcqlxx;
    }

    /**
     * 描述：抵押证明号查询抵押详情
     *          结果转换
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[dyzm]
     * 返回：Sj_Info_Bdcdyxgxx
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static Sj_Info_Bdcdyxgxx getImmoMortsByDyzm(DyzmResponse dyzm) throws ParseException {
        //核验接口数据，做基础自检
        dyzm.checkSelfStandard();
        //获取登记数据并初始化一窗标准的不动产抵押对象
        Sj_Info_Bdcdyxgxx bdcdyxx = getRegisterInfoByDyzm(dyzm);;
        if(bdcdyxx!=null){
            //初始化不动产调查信息
            initBdcdcxxByDyzm(bdcdyxx,dyzm);
            //初始化抵押人信息
            bdcdyxx.setGlMortgagorVoList(initXgrxx(BDC_RYZL_DYR, dyzm.getDyrlb()));
            //初始化抵押权人信息
            bdcdyxx.setGlMortgageHolderVoList(initXgrxx(BDC_RYZL_DYQR, dyzm.getDyqrlb()));
        }
        return bdcdyxx;
    }

    /**
     * 描述：从不动产单元信息填充不动产楼盘数据（单一）
     * 作者：chenb
     * 日期：2020/8/18
     * 参数：[bdcdyResponse]
     * 返回：SJ_Info_Immovable
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static SJ_Info_Immovable getImmovInfoByDysj(BdcdyResponse bdcdyResponse){
        bdcdyResponse.checkSelfStandard();
        SJ_Info_Immovable serviceDataInfo = new SJ_Info_Immovable();
        serviceDataInfo.setSfczyc(
                DicConvertUtil.getKeyCodeByWord(bdcdyResponse.getSfczyc(), KEY_BOOLEAN_CODE_Enums.values()));
        if(bdcdyResponse.getBdcdyxxlb()!=null && bdcdyResponse.getBdcdyxxlb().size()>0){
            //初始化不动产调查信息
            initBdcdcxxByBdcdyxx(serviceDataInfo,bdcdyResponse.getBdcdyxxlb());
        }
        return serviceDataInfo;
    }

    /**
     * 描述：登记资料查询结果转金融风控要求结果
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[djzl, key, paph]
     * 返回：PaphEntity
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static PaphEntity getPaphEntity(DjzlResponse djzl, String key, PaphReqEntity paph){
        //核验接口数据，做基础自检
        djzl.checkSelfStandard();
        //登记资料解析基础金融端需求的数据
        PaphEntity paphEntity = getBasePaphEntity(djzl);
        if(paphEntity!=null){
            //解析查封数据
            List<Cfxx> cfxxs = djzl.getCfxxs();
            if(cfxxs!=null && cfxxs.size()>0){
                paphEntity.setSfcf("是");
                paphEntity.setCfxxs(getPaphCfsByDjzlCf(cfxxs));
            }
            //解析冻结信息
            List<Djxx> djxxs = djzl.getDjxxlb();
            if(djxxs!=null && djxxs.size()>0){
                paphEntity.setSfdj("是");
                paphEntity.setDjxxs(getPaphDjsByDjzlDj(djxxs));
            }
            //解析抵押数据，区分贷前和贷后
            List<com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Dyxx> dyxxs = djzl.getDyxxs();
            if(dyxxs!=null && dyxxs.size()>0){
                if("before".equals(key)){
                    paphEntity.setSfdy("是");
                    paphEntity.setDyxxs(getBeforeDyxxs(dyxxs));
                }else if("after".equals(key)){
                    List<PaphDyxx> pdyxxs = getAfterDyxxs(dyxxs,paph);
                    setPaphQtdy(pdyxxs,paphEntity);
                }
            }
        }
        return paphEntity;
    }

    /**
     * 描述：从权证查询结果获取登记信息
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[cqzs]
     * 返回：Bdcqlxgxx
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static SJ_Info_Bdcqlxgxx getRegisterInfoByCqzs(CqzsResponse cqzs) throws ParseException {
        SJ_Info_Bdcqlxgxx bdcqlxx = null;
        if(cqzs!=null){
            bdcqlxx = new SJ_Info_Bdcqlxgxx();
            bdcqlxx.setAcceptanceNumber(cqzs.getYwh());
            bdcqlxx.setRegisterType(cqzs.getDjlx());
            bdcqlxx.setImmovableCertificateNo(cqzs.getBdczh());
            bdcqlxx.setCertificateType(
                    StringUtils.isBlank(cqzs.getZslx())?CERTIFICATE_TYPE_OF_FC:DicConvertUtil.getDicNameByVal(cqzs.getZslx(), DIC_BDC_ZS_TYPE_Enums.values())
            );
            bdcqlxx.setRegistrationDate(TimeUtil.getTimeFromString(cqzs.getDjrq()));
            bdcqlxx.setImmovableSite(cqzs.getZl());
            bdcqlxx.setRemarks(cqzs.getFj());
            bdcqlxx.setOther(cqzs.getQtzk());
            bdcqlxx.initInfoTerm("",DATA_SOURCE_DEPART_BDC);
        }
        return bdcqlxx;
    }

    /**
     * 描述：从抵押证明查询结果获取登记信息
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[dyzm]
     * 返回：Sj_Info_Bdcdyxgxx
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static Sj_Info_Bdcdyxgxx getRegisterInfoByDyzm(DyzmResponse dyzm) throws ParseException {
        Sj_Info_Bdcdyxgxx bdcdyxx = null;
        if(dyzm!=null){
            bdcdyxx = new Sj_Info_Bdcdyxgxx();
            bdcdyxx.setAcceptanceNumber(dyzm.getYwh());
            bdcdyxx.setImmovableSite(dyzm.getZl());
            bdcdyxx.setMortgageCertificateNo(dyzm.getDyzmh());
            bdcdyxx.setRegistrationDate(TimeUtil.getTimeFromString(dyzm.getDjrq()));
            bdcdyxx.setMortgageMode(dyzm.getDyfs());
            bdcdyxx.setMortgageArea(getBigDecimalNotThrowNull("<抵押证明--抵押证明信息> 抵押面积 ",dyzm.getDymj()));
            bdcdyxx.setCreditAmount(getBigDecimalNotThrowNull("<抵押证明--抵押证明信息> 债权数额 ",dyzm.getZqje()));
            bdcdyxx.setMortgageAmount(getBigDecimalNotThrowNull("<抵押证明--抵押证明信息> 抵押金额 ",dyzm.getDyje()));
            bdcdyxx.setValuationValue(getBigDecimalNotThrowNull("<抵押证明--抵押证明信息> 评估价值 ",dyzm.getPgjz()));
            bdcdyxx.setMortgagePeriod(dyzm.getDyqx());
            bdcdyxx.setMortgageStartingDate(TimeUtil.getDateFromString(dyzm.getDyqssj()));
            bdcdyxx.setMortgageEndingDate(TimeUtil.getDateFromString(dyzm.getDyjssj()));
            bdcdyxx.setMortgageReason(dyzm.getDyyy());
            bdcdyxx.setRemarks(dyzm.getFj());
            bdcdyxx.setOther(dyzm.getQtzk());
            bdcdyxx.initInfoTerm("",DATA_SOURCE_DEPART_BDC);
            bdcdyxx.setQllx(StringUtils.isBlank(dyzm.getQllx())?"抵押权":dyzm.getQllx());
        }
        return bdcdyxx;
    }

    /**
     * 描述：根据登记资料获取金融查询基础数据
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[djzl]
     * 返回：PaphEntity
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static PaphEntity getBasePaphEntity(DjzlResponse djzl){
        PaphEntity paphEntity = null;
        if(djzl!=null){
            paphEntity = new PaphEntity();
            paphEntity.setBdczh(djzl.getBdcqzh());
            paphEntity.setBdczl(djzl.getZl());
            paphEntity.setBdcdyh(djzl.getBdcdyh());
            paphEntity.setQtxz(StringUtils.isBlank(djzl.getQtxz())?"无":djzl.getQtxz());
        }
        return paphEntity;
    }

    /**
     * 描述：从权证查询结果获取权利相关信息
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[bdcqlxx, cqzs]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void initBdcqlxxByCqzs(SJ_Info_Bdcqlxgxx bdcqlxx,CqzsResponse cqzs) throws ParseException {
        Fwxgqlxx fwqlxx = cqzs.getFwxgqlxx();
        Tdxgqlxx tdqlxx = cqzs.getTdxgqlxx();
        if(fwqlxx!=null) {
            bdcqlxx.setArchitecturalArea(getBigDecimalNotThrowNull("<产权证书--房屋权利信息> 建筑面积 ",fwqlxx.getJzmj()));
            bdcqlxx.setHouseArchitecturalArea(getBigDecimalNotThrowNull("<产权证书--房屋权利信息> 套内建筑面积 ",fwqlxx.getTnjzmj()));
            bdcqlxx.setApportionmentArchitecturalArea(getBigDecimalNotThrowNull("<产权证书--房屋权利信息> 分摊建筑面积 ",fwqlxx.getFtjzmj()));
            bdcqlxx.setFsss(fwqlxx.getFsss());
            bdcqlxx.setHouseObtainingWays(fwqlxx.getFwqdfs());
            bdcqlxx.setHouseObtainingPrice(getBigDecimalNotThrowNull("<产权证书--房屋权利信息> 房屋取得价格 ",fwqlxx.getFwqdjg()));
            bdcqlxx.setHousePlanningPurpose(fwqlxx.getFwghyt());
            bdcqlxx.setHouseValuationAmount(getBigDecimalNotThrowNull("<产权证书--房屋权利信息> 房屋评估金额 ",fwqlxx.getPgjz()));
            bdcqlxx.setHouseType(fwqlxx.getFwlx());
            bdcqlxx.setHouseNature(fwqlxx.getFwxz());
            bdcqlxx.setHouseRightType(fwqlxx.getFwqllx());
            bdcqlxx.setHouseRightNature(fwqlxx.getFwqlxz());
        }
        if(tdqlxx!=null){
            bdcqlxx.setLandRightType(tdqlxx.getTdqllx());
            bdcqlxx.setLandRightNature(tdqlxx.getTdqlxz());
            bdcqlxx.setLandUseRightOwner(tdqlxx.getTdsyqr());
            bdcqlxx.setLandUseTimeLimit(tdqlxx.getTdsyqx());
            bdcqlxx.setLandUseRightStartingDate(TimeUtil.getDateFromString(tdqlxx.getQsrq()));
            bdcqlxx.setLandUseRightEndingDate(TimeUtil.getDateFromString(tdqlxx.getZzrq()));
            bdcqlxx.setLandPurpose(tdqlxx.getTdyt());//用途
            bdcqlxx.setLandObtainWay(tdqlxx.getTdqdfs());
            bdcqlxx.setCommonLandArea(getBigDecimalNotThrowNull("<产权证书--土地权利信息> 共有土地面积 ",tdqlxx.getGytdmj()));
            bdcqlxx.setSingleLandArea(getBigDecimalNotThrowNull("<产权证书--土地权利信息> 独用土地面积 ",tdqlxx.getDytdmj()));
            bdcqlxx.setShareLandArea(getBigDecimalNotThrowNull("<产权证书--土地权利信息> 分摊土地面积 ",tdqlxx.getFttdmj()));
            bdcqlxx.setBuildingParcelArea(getBigDecimalNotThrowNull("<产权证书--土地权利信息> 建筑宗地面积 ",tdqlxx.getJzzdmj()));
        }
    }

    /**
     * 描述：从权证查询结果获取登记不动产调查信息
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[bdcqlxx, cqzs]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void initBdcdcxxByCqzs(SJ_Info_Bdcqlxgxx bdcqlxx,CqzsResponse cqzs){
        List<Fwdcxx> fwdcxxlb = cqzs.getFwdcxxlb();
        List<Zddcxx> zddcxxlb = cqzs.getZddcxxlb();
        if(fwdcxxlb!=null && fwdcxxlb.size()>0){
            zddcxxlb = null;
        }
        bdcqlxx.setGlImmovableVoList(getBdcdcxxByDcxx(fwdcxxlb,zddcxxlb,bdcqlxx.getHouseRightNature()));
    }

    /**
     * 描述：从抵押证明查询结果获取登记不动产调查信息
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[bdcdyxx, dyzm]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void initBdcdcxxByDyzm(Sj_Info_Bdcdyxgxx bdcdyxx,DyzmResponse dyzm){
        List<Fwdcxx> fwdcxxlb = dyzm.getGlfwdcxxlb();
        List<Zddcxx> zddcxxlb = dyzm.getZddcxxlb();
        if(fwdcxxlb!=null && fwdcxxlb.size()>0){
            zddcxxlb = null;
        }
        bdcdyxx.setGlImmovableVoList(getBdcdcxxByDcxx(fwdcxxlb,zddcxxlb,null));
    }

    /**
     * 描述：通过不动产单元信息填充房屋调查信息
     * 作者：chenb
     * 日期：2020/8/18
     * 参数：SJ_Info_Immovable，List<Bdcdyxx>
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
    */
    public static void initBdcdcxxByBdcdyxx(SJ_Info_Immovable serviceDataInfo,List<Bdcdyxx> bdcdyxxlb) {
        List<SJ_Bdc_Gl> glImmovableVoList = new ArrayList<>();
        boolean existZss = false;
        for(Bdcdyxx bdcdyxx:bdcdyxxlb) {
            //基础信息填充
            fillImmovBaseInfo(serviceDataInfo,bdcdyxx);
            //声明并设置关联不动产信息
            SJ_Bdc_Gl bdcGl = new SJ_Bdc_Gl();
            bdcGl.setImmovableType(DicConvertUtil.getKeyWordByCode(BDC_TYPE_FD, KEY_BDC_TYPE_Enums.values()));
            if(!existZss) {
                bdcGl.setSslb("Z");
                existZss = true;
            }
            //填充房屋信息
            bdcGl.setFwInfo(getFwInfoByBdcdy(bdcdyxx));
            glImmovableVoList.add(bdcGl);
        }
        serviceDataInfo.setGlImmovableVoList(glImmovableVoList);
    }

    /**
     * 描述：从权证查询结果获取登记不动产他项权信息
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[bdcqlxx, cqzs]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void initBdctxqxxByCqzs(SJ_Info_Bdcqlxgxx bdcqlxx,CqzsResponse cqzs){
        List<SJ_Its_Right> itsRightVoList = new ArrayList<>();
        List<Dyxx> dyxxlb = cqzs.getDyxxlb();
        List<Cfxx> cfxxlb = cqzs.getCfxxlb();
        List<Yyxx> yyxxlb = cqzs.getYyxxlb();
        List<Djxx> djxxlb = cqzs.getDjxxlb();

        if(cfxxlb!=null && cfxxlb.size()>0){
            for(Cfxx cfxx:cfxxlb){
                itsRightVoList.add(new SJ_Its_Right().initByCfxx(cfxx));
            }
        }
        if(dyxxlb!=null && dyxxlb.size()>0){
            for(Dyxx dyxx : dyxxlb){
                itsRightVoList.add(new SJ_Its_Right().initByDyxx(dyxx));
            }
        }
        if(yyxxlb!=null && yyxxlb.size()>0){
            for(Yyxx yyxx:yyxxlb){
                itsRightVoList.add(new SJ_Its_Right().initByYyxx(yyxx));
            }
        }
        if(djxxlb!=null && djxxlb.size()>0){
            for(Djxx djxx:djxxlb){
                itsRightVoList.add(new SJ_Its_Right().initByDjxx(djxx));
            }
        }
        bdcqlxx.setItsRightVoList(itsRightVoList);
    }

    /**
     * 描述：权利人的通用处理方法
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[ryzl, qlrlb]
     * 返回：一窗规范的权利人对象集合
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static List<SJ_Qlr_Gl> initXgrxx(String ryzl, List<Qlr> qlrlb){
        List<SJ_Qlr_Gl> glycqlrlb = new ArrayList<>();
        if(qlrlb!=null) {
            int index = 1;
            for (Qlr qlr : qlrlb) {
                SJ_Qlr_Gl ycqlrgl = new SJ_Qlr_Gl().initByDjqlr(ryzl,index,qlr);
                ycqlrgl.setRelatedPerson(getQlrxxByCyr(qlr));
                glycqlrlb.add(ycqlrgl);
                index++;
            }
        }
        return glycqlrlb;
    }

    /**
     * 描述：参与人（代理人，委托人...）的通用处理方法
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[ryzl, cyrlb]
     * 返回：List<SJ_Qlr_Gl>
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static List<SJ_Qlr_Gl> initXgcyrxx(String ryzl, List<Cyr> cyrlb){
        List<SJ_Qlr_Gl> glycqlrlb = new ArrayList<>();
        if(cyrlb!=null) {
            int index = 1;
            for (Cyr cyr : cyrlb) {
                SJ_Qlr_Gl ycqlrgl = new SJ_Qlr_Gl().initByDjcyr(ryzl,index,cyr);
                ycqlrgl.setRelatedPerson(getQlrxxByCyr(cyr));
                glycqlrlb.add(ycqlrgl);
                index++;
            }
        }
        return glycqlrlb;
    }

    /**
     * 描述：具体不动产关联信息生成
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：List<Fwdcxx> fwdcxxlb, List<Zddcxx> zddcxxlb
     * 返回：List<SJ_Bdc_Gl>
     * 更新记录：更新人：{}，更新日期：{}
    */
    public static List<SJ_Bdc_Gl> getBdcdcxxByDcxx(List<Fwdcxx> fwdcxxlb, List<Zddcxx> zddcxxlb,String qlxz){
        List<SJ_Bdc_Gl> glImmovableVoList = new ArrayList<>();
        if(fwdcxxlb!=null){
            for(Fwdcxx fwdcxx:fwdcxxlb){
                glImmovableVoList.add(new SJ_Bdc_Gl().initFwBdcgl().fillFwdcxx(fwdcxx,qlxz));
            }
        }
        if(zddcxxlb!=null){
            for(Zddcxx zddcxx:zddcxxlb){
                glImmovableVoList.add(new SJ_Bdc_Gl().initZdBdcgl().fillZddcxx(zddcxx));
            }
        }
        return glImmovableVoList;
    }

    /**
     * 描述：填充楼盘的基本信息(单户)
     * 作者：chenb
     * 日期：2020/8/18
     * 参数：[serviceDataInfo, bdcdyxx]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void fillImmovBaseInfo(SJ_Info_Immovable serviceDataInfo,Bdcdyxx bdcdyxx){
        //填充是否预测
        if(StringUtils.isBlank(serviceDataInfo.getSfyc())){
            serviceDataInfo.setSfyc(bdcdyxx.getSfyc());
        }
        //填充项目名称
        if(StringUtils.isBlank(serviceDataInfo.getXmmc())){
            serviceDataInfo.setXmmc(bdcdyxx.getXmmc());
        }
        //填充楼盘名称
        if(StringUtils.isBlank(serviceDataInfo.getLpmc())){
            serviceDataInfo.setLpmc(bdcdyxx.getJzmc());
        }
        //填充幢号
        if(StringUtils.isBlank(serviceDataInfo.getZh())){
            serviceDataInfo.setZh(bdcdyxx.getZh());
        }
        //原业务号
        if(StringUtils.isBlank(serviceDataInfo.getYywh())){
            serviceDataInfo.setYywh(bdcdyxx.getYywh());
        }
        //原权利类型
        if(StringUtils.isBlank(serviceDataInfo.getYqllx())){
            serviceDataInfo.setYqllx(bdcdyxx.getYqllx());
        }
    }

    /**
     * 描述：填充房屋调查信息
     * 作者：chenb
     * 日期：2020/8/18
     * 参数：[bdcdyxx]
     * 返回：SJ_Bdc_Fw_Info
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static SJ_Bdc_Fw_Info getFwInfoByBdcdy(Bdcdyxx bdcdyxx){
        //声明待返回的房屋信息
        SJ_Bdc_Fw_Info fwInfo = new SJ_Bdc_Fw_Info();

        fwInfo.setImmovableUnitNumber(bdcdyxx.getBdcdyh());
        fwInfo.setOldHouseCode(bdcdyxx.getFwdm());
        fwInfo.setSeatNumber(bdcdyxx.getZh());
        fwInfo.setUnitMark(bdcdyxx.getDyh());
        fwInfo.setRoomMark(bdcdyxx.getFjh());
        fwInfo.setHouseholdMark(bdcdyxx.getHh());
        fwInfo.setHouseholdNumber(bdcdyxx.getHh());
        fwInfo.setHouseLocation(bdcdyxx.getZl());
        fwInfo.setProjectName(bdcdyxx.getXmmc());
        fwInfo.setBuildingName(bdcdyxx.getJzmc());
        fwInfo.setArchitecturalArea(
                getBigDecimalNotThrowNull("<不动产关联--房屋调查信息> 建筑面积 ",bdcdyxx.getJzmj()));
        fwInfo.setMortgageSituation(
                DicConvertUtil.getKeyWordByCode(bdcdyxx.getDyzt(), KEY_BDC_QSZT_DYXZZT_Enums.values()));
        fwInfo.setClosureSituation(
                DicConvertUtil.getKeyWordByCode(bdcdyxx.getCfzt(), KEY_BDC_QSZT_DYXZZT_Enums.values()));
        fwInfo.setObjectionSituation(
                DicConvertUtil.getKeyWordByCode(bdcdyxx.getYyzt(), KEY_BDC_QSZT_DYXZZT_Enums.values()));
        return fwInfo;
    }

    /**
     * 描述：具体权利人/参与人信息生成
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[cyr]
     * 返回：SJ_Qlr_Info
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static SJ_Qlr_Info getQlrxxByCyr(Cyr cyr){
        SJ_Qlr_Info qlrInfo = new SJ_Qlr_Info();
        qlrInfo.setObligeeName(cyr.getQlrmc());
        qlrInfo.setObligeeDocumentType(DicConvertUtil.getDicNameByVal(cyr.getQlrzjzl(), DIC_RY_ZJZL_Enums.values()));
        qlrInfo.setObligeeDocumentNumber(cyr.getQlrzjhm());
        qlrInfo.setDh(cyr.getDh());
        qlrInfo.setDz(cyr.getDz());
        return qlrInfo;
    }

    /**
     * 描述：填充房屋调查信息
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[fwInfo, fwdcxx]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void fillFwxx(SJ_Bdc_Fw_Info fwInfo,Fwdcxx fwdcxx){
        fwInfo.setOldHouseCode(fwdcxx.getFwdm());
        fwInfo.setImmovableUnicode(fwdcxx.getFwdm());
        fwInfo.setImmovablePlanningUse(fwdcxx.getFwghyt());
        fwInfo.setHouseLocation(fwdcxx.getZl());  //坐落
        fwInfo.setImmovableUnitNumber(fwdcxx.getBdcdyh());    //不动产单元号
        fwInfo.setHouseholdNumber(fwdcxx.getHbh());//户编号
        fwInfo.setSeatNumber(fwdcxx.getZbh());
        fwInfo.setHouseholdMark(fwdcxx.getHh());
        fwInfo.setRoomMark(fwdcxx.getFjh());
        fwInfo.setUnitMark(fwdcxx.getDyh());
        fwInfo.setTotalStorey(fwdcxx.getZcs());    //总层数
        fwInfo.setLocationStorey(fwdcxx.getSzc()); //所在层
        fwInfo.setProjectName(fwdcxx.getXmmc());
        fwInfo.setArchitecturalArea(getBigDecimalNotThrowNull("<不动产关联--房屋调查信息> 建筑面积 ",fwdcxx.getJzmj()));          //建筑
        fwInfo.setHouseArchitecturalArea(getBigDecimalNotThrowNull("<不动产关联--房屋调查信息> 套内建筑面积 ",fwdcxx.getTnjzmj()));     //套内
        fwInfo.setApportionmentArchitecturalArea(getBigDecimalNotThrowNull("<不动产关联--房屋调查信息> 分摊建筑面积 ",fwdcxx.getFtjzmj())); //分摊
        fwInfo.setBuildingName(fwdcxx.getZjmc());
        fwInfo.setHouseType(fwdcxx.getFwlx());
        fwInfo.setHouseNature(fwdcxx.getFwxz());
        fwInfo.setHouseStructure(fwdcxx.getFwjg());         //结构
    }

    /**
     * 描述：填充宗地调查信息
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[zdInfo, zddcxx]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void fillZdxx(SJ_Bdc_Zd_Info zdInfo,Zddcxx zddcxx) {
        zdInfo.setParcelType(zddcxx.getZdlx());
        zdInfo.setParcelUnicode(zddcxx.getTdid());
        zdInfo.setImmovableUnitNumber(zddcxx.getBdcdyh());
        zdInfo.setCadastralNumber(zddcxx.getDjh());        //地籍号
        zdInfo.setParcelLocation(zddcxx.getTdzl());
        zdInfo.setLandUse(zddcxx.getTdyt());
        zdInfo.setCommonLandArea(null);
        zdInfo.setPrivateLandArea(getBigDecimalNotThrowNull("<不动产关联--宗地调查信息> 独用土地面积 ",zddcxx.getDytdmj()));
        zdInfo.setApportionmentLandArea(getBigDecimalNotThrowNull("<不动产关联--宗地调查信息> 分摊土地面积 ",zddcxx.getFttdmj()));
        try {
            zdInfo.setLandRightEndDate(TimeUtil.getDateFromString(zddcxx.getTdzzrq()));
        } catch (ParseException e) {
            throw new ZtgeoBizException("【结果转换异常】--> 返回土地中止使用日期格式异常");
        }
    }

    /**
     * 描述：从登记资料的查封信息
     *          解析金融机构需要的查封信息
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[cfxxs]
     * 返回：【PaphCfxx】
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static List<PaphCfxx> getPaphCfsByDjzlCf(List<Cfxx> cfxxs){
        List<PaphCfxx> pcfxxs = new ArrayList<>();
        for(Cfxx cfxx:cfxxs){
            PaphCfxx pcfxx = new PaphCfxx();
            pcfxx.setCfqxq(cfxx.getCfkssj());
            pcfxx.setCfqxz(cfxx.getCfjssj());
            pcfxxs.add(pcfxx);
        }
        return pcfxxs;
    }

    /**
     * 描述：从登记资料的冻结信息
     *          解析金融机构需要的冻结信息
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[djxxs]
     * 返回：【PaphDjxx】
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static List<PaphDjxx> getPaphDjsByDjzlDj(List<Djxx> djxxs){
        List<PaphDjxx> pdjxxs = new ArrayList<>();
        for(Djxx djxx:djxxs){
            PaphDjxx pdjxx = new PaphDjxx();
            pdjxx.setDjqxq(djxx.getDjkssj());
            pdjxx.setDjqxz(djxx.getDjjssj());
            pdjxxs.add(pdjxx);
        }
        return pdjxxs;
    }

    /**
     * 描述：获取贷前风控结果
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[dyxxs]
     * 返回：List<PaphDyxx>
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static List<PaphDyxx> getBeforeDyxxs(List<com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Dyxx> dyxxs){
        List<PaphDyxx> pdyxxs = new ArrayList<PaphDyxx>();
        for(int i=0;i<dyxxs.size();i++){
            com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Dyxx dyxx = dyxxs.get(i);
            if(dyxx!=null) {
                PaphDyxx pdyxx = new PaphDyxx().initByDjzlDy(dyxx);
                if(StringUtils.isNotBlank(dyxx.getDyqr())){
                    if(dyxx.getDyqr().contains("银行"))
                        pdyxx.setDyqr("银行");
                    else
                        pdyxx.setDyqr("其它");
                }
                pdyxxs.add(pdyxx);
            }
        }
        return pdyxxs;
    }

    /**
     * 描述：获取贷后风控结果
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[dyxxs, paph]
     * 返回：List<PaphDyxx>
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static List<PaphDyxx> getAfterDyxxs(List<com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Dyxx> dyxxs,PaphReqEntity paph){
        List<PaphDyxx> pdyxxs = new ArrayList<>();
        for(com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Dyxx dyxx:dyxxs) {
            if(dyxx!=null) {
                if (StringUtils.isNotBlank(dyxx.getDyqr())) {
                    if (!dyxx.getDyqr().contains(paph.getDyqrmc())) {
                        PaphDyxx pdyxx = new PaphDyxx().initByDjzlDy(dyxx);
                        if (dyxx.getDyqr().contains("银行")) {
                            pdyxx.setDyqr("银行");
                        } else {
                            pdyxx.setDyqr("其它");
                        }
                        pdyxxs.add(pdyxx);
                    }
                }
            }
        }
        return pdyxxs;
    }

    /**
     * 描述：处理债务履行期限
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[dyxx]
     * 返回：String
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static String creatZwlxqx(com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Dyxx dyxx){
        String ks = dyxx.getDykssj();
        String js = dyxx.getDyjssj();
        return (StringUtils.isNotBlank(ks)? ks+" 至 ":"")+(StringUtils.isNotBlank(js)?js:"未定");
    }

    /**
     * 描述：设置贷后风控其它抵押结果
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[pdyxxs, paphEntity]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void setPaphQtdy(List<PaphDyxx> pdyxxs,PaphEntity paphEntity){
        if(pdyxxs!=null && pdyxxs.size()>0){
            paphEntity.setSfqtdy("是");
            paphEntity.setDyxxs(pdyxxs);
        }
    }

    /**
     * 描述：数字类型字段处理（String --> BigDecimal）
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[describe, in]
     * 返回：BigDecimal
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static BigDecimal getBigDecimalNotThrowNull(String describe,String in){
        BigDecimal out = null;
        try {
            if (StringUtils.isNotBlank(in))
                out = new BigDecimal(in);
        }catch (NumberFormatException e){
            log.warn("【结果转换告警】--> 输入数字不规范，"+describe+"要求数字,实际："+in);
            throw new ZtgeoBizException(describe+"数字格式异常");
        }
        return out;
    }

    /**
     * 描述：数字类型字段处理（BigDecimal --> String）
     *          整型保留整型
     *          小数型按formatStr保留小数点位数
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：BigDecimal
     * 返回：String
     * 更新记录：更新人：{}，更新日期：{}
    */
    public static String getStringFromBigDecimalNotThrowNull(BigDecimal in,String formatStr){
        DecimalFormat dff = new DecimalFormat(formatStr);
        String out = null;
        if(in!=null){
            if(isIntegerValue(in))
                out = String.valueOf(in.intValue());
            else
                out = dff.format(in);
        }
        return out;
    }

    /**
     * 描述：数字类型整型和浮点型处理小数点位数
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[in, formatStr]
     * 返回：BigDecimal
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static BigDecimal dealDecimal(BigDecimal in,String formatStr){
        DecimalFormat dff = new DecimalFormat(formatStr);
        if(in!=null){
            if(isIntegerValue(in))
                return new BigDecimal(in.intValue());
            else
                in = new BigDecimal(dff.format(in));
        }
        return in;
    }

    /**
     * 描述：数值的整数型判断
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：BigDecimal
     * 返回：boolean
     * 更新记录：更新人：{}，更新日期：{}
    */
    public static boolean isIntegerValue(BigDecimal decimalVal) {
        return decimalVal.scale() <= 0 || decimalVal.stripTrailingZeros().scale() <= 0;
    }

    public static void checkYcResult(ObjectRestResponse yczxjg){
        if(yczxjg.getStatus()!=200){
            throw new ZtgeoBizException(yczxjg.getMessage());
        }
    }

    /**
     * 描述：登簿结果的非空检查
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[dbjglb]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void checkDbjgNonNull(String ywh,List<Dbjgxx> dbjglb){
        if(dbjglb==null || dbjglb.size()<1)
            throw new ZtgeoBizException("非注销类不动产业务登簿结果为空，但此时不应为空，产生异常的业务号：【"+ywh+"】");
        if(dbjglb.stream().anyMatch(dbjg -> StringUtils.isBlank(dbjg.getBdczh())))
            throw new ZtgeoBizException("非注销类不动产业务登簿结果返回“不动产证号”为空，但此时不应为空，产生异常的业务号：【"+ywh+"】");
    }

    /**
     * 描述：注销类登登簿结果检查
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[dbjglb]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void checkZxldbjg(List<Dbjgxx> dbjglb){

    }

    /**
     * 描述：通过接口取得数据结果初始化不动产审核结果信息（产生remark字符串）
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[ywh, djshxx]
     * 返回：String
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static String initBdcShjgByInterfceGet(String ywh,Djshxx djshxx){
        if(djshxx==null){
            throw new ZtgeoBizException(ywh+"号不动产办件返回的审批信息为null");
        }
        if(djshxx.getShxxlb()==null || djshxx.getShxxlb().size()<1){
            throw new ZtgeoBizException(ywh+"号不动产办件未返回有效审批信息，审批详情列表为空");
        }
        String remark = ywh+"号不动产业务（"+djshxx.getShxxlb().get(0).getDjxl()+"）办件,各节点审批详情：";
        for(Shxx shxx:djshxx.getShxxlb()){
            remark += shxx.getJdsm()+"节点审核意见--"+shxx.getSpyj()+";";
        }
        remark = remark.substring(0,remark.lastIndexOf(";"))+"。";
        return remark;
    }

    public static void main(String[] args){
//        System.out.println("是否整数："+isIntegerValue(null));
        String testStr = "12kdj";
        System.out.println(JSONArray.toJSONString(testStr.split(",")));
        System.out.println("保留两位小数(String)："+getStringFromBigDecimalNotThrowNull(new BigDecimal(3),"#.00"));
        System.out.println("保留两位小数(BigDecimal)："+getStringFromBigDecimalNotThrowNull(new BigDecimal(3),"#.00"));
    }
}
