package com.springboot.util.newPlatBizUtil;

import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Fwdcxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Fwxgqlxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Tdxgqlxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Zddcxx;
import com.springboot.entity.newPlat.query.resp.CqzsResponse;
import com.springboot.popj.pub_data.SJ_Bdc_Fw_Info;
import com.springboot.popj.pub_data.SJ_Bdc_Gl;
import com.springboot.popj.pub_data.SJ_Bdc_Zd_Info;
import com.springboot.popj.pub_data.SJ_Info_Bdcqlxgxx;
import com.springboot.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.springboot.constant.chenbin.KeywordConstant.*;
import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.*;

/**
 * @author chenb
 * @version 2020/7/27/027
 * description：新平台适配性，结果转换工具类
 */
@Slf4j
public class ResultConvertUtil {
    public static SJ_Info_Bdcqlxgxx getImmoRightsByCqzs(CqzsResponse cqzs) throws ParseException {
        //核验接口数据
        cqzs.checkSelfStandard();
        //登记信息转换，创建返回实体
        SJ_Info_Bdcqlxgxx bdcqlxx = getRegisterInfoByCqzs(cqzs);
        //初始化权利信息
        initBdcqlxxByCqzs(bdcqlxx,cqzs);
        //初始化不动产调查信息
        initBdcdcxxByCqzs(bdcqlxx,cqzs);
//        //初始化权利人信息
//        initXgrxx(,cqzs.getQlrlb());
//        //初始化义务人信息
//        initXgrxx(,cqzs.getQlrlb());
//        //初始化权利代理人信息
//        initXgdlrxx(,cqzs.getQlrlb());
//        //初始化义务代理人信息
//        initXgdlrxx(,cqzs.getQlrlb());
        //返回实体
        return bdcqlxx;
    }

    public static SJ_Info_Bdcqlxgxx getRegisterInfoByCqzs(CqzsResponse cqzs) throws ParseException {
        SJ_Info_Bdcqlxgxx bdcqlxx = null;
        if(cqzs!=null){
            bdcqlxx = new SJ_Info_Bdcqlxgxx();
            bdcqlxx.setAcceptanceNumber(cqzs.getYwh());
            bdcqlxx.setRegisterType(cqzs.getDjlx());
            bdcqlxx.setImmovableCertificateNo(cqzs.getBdczh());
            bdcqlxx.setCertificateType(cqzs.getZslx());
            bdcqlxx.setRegistrationDate(TimeUtil.getTimeFromString(cqzs.getDjrq()));
            bdcqlxx.setImmovableSite(cqzs.getZl());
            bdcqlxx.setRemarks(cqzs.getFj());
            bdcqlxx.setOther(cqzs.getQtzk());
            bdcqlxx.initInfoTerm("",DATA_SOURCE_DEPART_BDC);
        }
        return bdcqlxx;
    }

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

    public static void initBdcdcxxByCqzs(SJ_Info_Bdcqlxgxx bdcqlxx,CqzsResponse cqzs){
        List<SJ_Bdc_Gl> glImmovableVoList = new ArrayList<>();
        List<Fwdcxx> fwdcxxlb = cqzs.getFwdcxxlb();
        List<Zddcxx> zddcxxlb = cqzs.getZddcxxlb();
        if(fwdcxxlb!=null){
            for(Fwdcxx fwdcxx:fwdcxxlb){
                glImmovableVoList.add(new SJ_Bdc_Gl().initFwBdcgl(INFO_TABLE_CODE_BDCQL).fillFwdcxx(fwdcxx));
            }
        }
        if(zddcxxlb!=null){
            for(Zddcxx zddcxx:zddcxxlb){
                glImmovableVoList.add(new SJ_Bdc_Gl().initZdBdcgl(INFO_TABLE_CODE_BDCQL).fillZddcxx(zddcxx));
            }
        }
        bdcqlxx.setGlImmovableVoList(glImmovableVoList);
    }

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
}
