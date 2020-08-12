package com.springboot.util.newPlatBizUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.config.ZtgeoBizException;
import com.springboot.constant.penghao.BizOrBizExceptionConstant;
import com.springboot.emm.DIC_RY_GYFS_Enums;
import com.springboot.emm.DIC_RY_ZJZL_Enums;
import com.springboot.emm.DIC_RY_ZL_Enums;
import com.springboot.entity.chenbin.personnel.pub_use.SJ_Sjsq_User_Ext;
import com.springboot.entity.newPlat.jsonMap.FileNameMapping;
import com.springboot.entity.newPlat.settingTerm.NewPlatSettings;
import com.springboot.entity.newPlat.settingTerm.TurnInnerSettingsTerm;
import com.springboot.entity.newPlat.transInner.req.fromZY.NewBdcFlowRequest;
import com.springboot.entity.newPlat.transInner.req.fromZY.domain.Fwxx;
import com.springboot.entity.newPlat.transInner.req.fromZY.domain.Sqrxx;
import com.springboot.popj.pub_data.*;
import com.springboot.service.newPlat.chenbin.BdcInteractService;
import com.springboot.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.springboot.constant.AdminCommonConstant.BOOLEAN_NUMBER_TRUE;
import static com.springboot.constant.chenbin.BusinessConstant.*;
import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.*;
import static com.springboot.constant.penghao.BizOrBizExceptionConstant.*;

/**
 * @author chenb
 * @version 2020/8/7
 * description：参数处理工具类
 */
public class ParamConvertUtil {

    /**
     * 描述：取基础转内网信息
     *          带保险机制，如果未传入关键信息，将取配置项补全
     * 作者：chenb
     * 日期：2020/8/7
     * 参数：[sjsq, newPlatSettings, ywKey]
     * 返回：NewBdcFlowRequest
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static NewBdcFlowRequest getBaseFromSjsq(
            SJ_Sjsq sjsq,
            NewPlatSettings newPlatSettings,
            String ywKey,
            String djlb,
            String lzdz
    ){
        //声明返回对象
        NewBdcFlowRequest newBdcFlowRequest = new NewBdcFlowRequest();
        //处理系统对接数据（接收人和业务sid对接）
        TurnInnerSettingsTerm turnInnerSettingsTerm = newPlatSettings.gainTermByKey(ywKey);
        List<SJ_Sjsq_User_Ext> userExts = sjsq.getUserExtVoList();
        String jsrid = null;
        String jsrmc = null;
        String sid = null;
        String sname = null;
        if(userExts!=null && userExts.size()>0){
            for(SJ_Sjsq_User_Ext userExt:userExts){
                if(
                        userExt!=null
                        && StringUtils.isNotBlank(userExt.getAdaptSys())
                        && BizOrBizExceptionConstant.BDC_BIZ_ADOBE_CODE.equals(userExt.getAdaptSys())
                ){
                    boolean isBreak = true;
                    if(StringUtils.isNotBlank(userExt.getUserCode())){
                        jsrid = userExt.getUserCode();
                        jsrmc = userExt.getUserName();
                    }else{
                        isBreak = isBreak && false;
                    }
                    if(StringUtils.isNotBlank(userExt.getBizCode())){
                        sid = userExt.getBizCode();
                        sname = userExt.getBizName();
                    }else{
                        isBreak = isBreak && false;
                    }
                    if(isBreak)
                        break;
                }
            }
        }
        if(StringUtils.isBlank(jsrid))
            jsrid = turnInnerSettingsTerm.getJsrid();
        if(StringUtils.isBlank(jsrmc))
            jsrmc = turnInnerSettingsTerm.getJsrmc();
        if(StringUtils.isBlank(sid))
            sid = turnInnerSettingsTerm.getSid();
        if(StringUtils.isBlank(sname))
            sname = turnInnerSettingsTerm.getSname();
        //设置基础对接信息
        newBdcFlowRequest.setSid(sid);
        newBdcFlowRequest.setWwdjlxmc(sname);
        newBdcFlowRequest.setJsrid(jsrid);
        newBdcFlowRequest.setJsrmc(jsrmc);
        newBdcFlowRequest.setWwywh(sjsq.getReceiptNumber());
        newBdcFlowRequest.setDjlb(djlb);
        newBdcFlowRequest.setLzdz(lzdz);
        newBdcFlowRequest.setSqjly("0");
        newBdcFlowRequest.setTzr(getNullStrIfk(sjsq.getNotifiedPersonName()));
        newBdcFlowRequest.setTzrdh(getNullStrIfk(sjsq.getNotifiedPersonTelephone()));
        //返回结果
        return newBdcFlowRequest;
    }

    /**
     * 描述：从主要产权中整理转内网必要的FWXX
     * 作者：chenb
     * 日期：2020/8/8
     * 参数：[newBdcFlowRequest, qlxxVoList]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void fillMainHouseToReqByQlxx(NewBdcFlowRequest newBdcFlowRequest,List<SJ_Info_Bdcqlxgxx> qlxxVoList,String sfyc) throws ParseException {

        //获取主要不动产权属
        SJ_Info_Bdcqlxgxx qlxxzt = getMainHouseQlxx(qlxxVoList);
        //获取主体房产信息
        SJ_Bdc_Gl bdc = getMainHouse(qlxxzt.getGlImmovableVoList());
        //获取基础房屋信息
        Fwxx fwxx = getBaseFwxx(bdc,sfyc);
        //设置权利相关项
        fwxx.setQllx(getNullStrIfk(qlxxzt.getHouseRightType()));
        fwxx.setQlxz(getNullStrIfk(qlxxzt.getHouseRightNature()));
        fwxx.setQlqssj(TimeUtil.getTimeString(TimeUtil.getTimeFromString(qlxxzt.getLandUseRightStartingDate())));
        fwxx.setQljssj(TimeUtil.getTimeString(TimeUtil.getTimeFromString(qlxxzt.getLandUseRightEndingDate())));
        //土地相关设置
        fwxx.setTddymj(qlxxzt.getSingleLandArea());
        fwxx.setTdftmj(qlxxzt.getShareLandArea());
        fwxx.setTdsyqmj(qlxxzt.getCommonLandArea());
        fwxx.setTdyt(getNullStrIfk(qlxxzt.getLandPurpose()));

        //设置原业务号和房屋信息
        newBdcFlowRequest.setYywh(getNullStrIfk(qlxxzt.getAcceptanceNumber()));//原业务号
        newBdcFlowRequest.setYqllx(getNullStrIfk(qlxxzt.getHouseRightType()));//原权利类型
        newBdcFlowRequest.setFwxx(fwxx);
    }

    /**
     * 描述：从现势抵押信息中整理转内网必要的FWXX
     * 作者：chenb
     * 日期：2020/8/8
     * 参数：[newBdcFlowRequest, qlxxVoList]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static void fillMainHouseToReqByDyxx(NewBdcFlowRequest newBdcFlowRequest,List<Sj_Info_Bdcdyxgxx> dyxxVoList,String sfyc) throws ParseException {
        //抵押信息判断
        if(dyxxVoList==null || dyxxVoList.size()<1){
            throw new ZtgeoBizException("收件不动产抵押信息为空");
        }
        if(dyxxVoList.size()>1){
            throw new ZtgeoBizException("收件存在多条不动产抵押信息，这在现在是不被允许的");
        }
        //获取主体抵押信息
        Sj_Info_Bdcdyxgxx ztdyxx = dyxxVoList.get(0);
        //获取房屋主体信息
        SJ_Bdc_Gl bdc = getMainHouse(ztdyxx.getGlImmovableVoList());
        Fwxx fwxx = getBaseFwxx(bdc,sfyc);
        //设置原业务号和房屋信息
        newBdcFlowRequest.setYywh(getNullStrIfk(ztdyxx.getAcceptanceNumber()));
        newBdcFlowRequest.setYqllx(ztdyxx.getQllx());
        newBdcFlowRequest.setFwxx(fwxx);
    }

    public static Fwxx getBaseFwxx(SJ_Bdc_Gl bdc,String sfyc){
        //声明房屋主体
        Fwxx fwxx = new Fwxx();
        SJ_Bdc_Fw_Info fwInfo = bdc.getFwInfo();
        if(fwInfo==null){
            throw new ZtgeoBizException("不动产不是房屋,这暂时在一窗受理的转内网业务上是不允许的");
        }
        //设置不动产调查数据
        fwxx.setFwdm(fwInfo.getOldHouseCode());
        fwxx.setBdcdyh(fwInfo.getImmovableUnitNumber());
        fwxx.setZl(getNullStrIfk(fwInfo.getHouseLocation()));
        fwxx.setZh(getNullStrIfk(fwInfo.getSeatNumber()));
        fwxx.setDyh(getNullStrIfk(fwInfo.getUnitMark()));
        fwxx.setFjh(getNullStrIfk(fwInfo.getRoomMark()));
        fwxx.setFwjg(getNullStrIfk(fwInfo.getHouseStructure()));
        fwxx.setFwxz(getNullStrIfk(fwInfo.getHouseNature()));
        fwxx.setGhyt(getNullStrIfk(fwInfo.getImmovablePlanningUse()));
        fwxx.setMyc(getNullStrIfk(""));
        fwxx.setSjc(getNullStrIfk(fwInfo.getLocationStorey()));
        fwxx.setJzmj(fwInfo.getArchitecturalArea());
        fwxx.setDyjzmj(null);
        fwxx.setTnjzmj(fwInfo.getHouseArchitecturalArea());
        fwxx.setFtjzmj(fwInfo.getApportionmentArchitecturalArea());
        fwxx.setFzwdm(null);

        //是否预测设置
        fwxx.setSfyc(sfyc);
        return fwxx;
    }

    /**
     * 描述：新平台的权利人设置适配
     * 作者：chenb
     * 日期：2020/8/8
     * 参数：[glQlrs, ywKsy]
     * 返回：List<Sqrxx>
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static List<Sqrxx> getSqrsByQlrs(List<SJ_Qlr_Gl> glQlrs,String ywKsy){
        List<Sqrxx> sqrxxs = new ArrayList<>();
        for(SJ_Qlr_Gl glQlr:glQlrs){
            SJ_Qlr_Info qlr = glQlr.getRelatedPerson();
            SJ_Qlr_Info dlr = glQlr.getRelatedAgent();
            if(qlr==null){
                throw new ZtgeoBizException("数据异常，关联权利人对象为空");
            }
            Sqrxx sqrxx = new Sqrxx();
            String qlrzl = DicConvertUtil.getDicValByName(
                    getMappinQlrzl(glQlr.getObligeeType(),ywKsy),
                    DIC_RY_ZL_Enums.values()
            );
            sqrxx.setQlrmc(qlr.getObligeeName());
            sqrxx.setQlrzjlx(DicConvertUtil.getDicValByName(qlr.getObligeeDocumentType(), DIC_RY_ZJZL_Enums.values()));
            sqrxx.setQlrzjhm(qlr.getObligeeDocumentNumber());
            sqrxx.setQlrzl(qlrzl);
            sqrxx.setLxdh(getNullStrIfk(qlr.getDh()));
            sqrxx.setSfcz(getNullStrIfk(BOOLEAN_NUMBER_TRUE.equals(glQlr.getSfcz())?BOOLEAN_NUMBER_TRUE:""));
            sqrxx.setGyfs(StringUtils.isBlank(glQlr.getSharedMode())?null:DicConvertUtil.getDicValByName(glQlr.getSharedMode(), DIC_RY_GYFS_Enums.values()));
            sqrxx.setGyfe(getNullStrIfk(glQlr.getSharedValue()));
            if(dlr!=null){
                sqrxx.setDlrmc(dlr.getObligeeName());
                sqrxx.setDlrzl(qlrzl);
                sqrxx.setDlrzjlx(DicConvertUtil.getDicValByName(dlr.getObligeeDocumentType(), DIC_RY_ZJZL_Enums.values()));
                sqrxx.setDlrzjhm(dlr.getObligeeDocumentNumber());
                sqrxx.setDlrlxdh(getNullStrIfk(dlr.getDh()));
            }
            sqrxx.setQlrxz(null);                   //性质
            sqrxxs.add(sqrxx);
        }
        return sqrxxs;
    }

    /**
     * 描述：通过权属收件信息集合筛选主要权属
     * 作者：chenb
     * 日期：2020/8/7
     * 参数：NewBdcFlowRequest，List<SJ_Info_Bdcqlxgxx>
     * 返回：SJ_Info_Bdcqlxgxx
     * 更新记录：更新人：{}，更新日期：{}
    */
    public static SJ_Info_Bdcqlxgxx getMainHouseQlxx(List<SJ_Info_Bdcqlxgxx> qlxxVoList){
        if(qlxxVoList==null || qlxxVoList.size()<1)
            throw new ZtgeoBizException("获取主权属信息时，提供的权属信息集合为空");
        //权利信息主体
        SJ_Info_Bdcqlxgxx qlxxzt = null;
        if(qlxxVoList.size()>1) {
            for (SJ_Info_Bdcqlxgxx qlxx : qlxxVoList) {
                String dataType = qlxx.getDataType();       //权属主体标识
                if ("主要权证".equals(dataType)) {
                    if (qlxxzt != null)
                        throw new ZtgeoBizException("主要权证信息只能存在一个");
                    qlxxzt = qlxx;
                }
            }
            if (qlxxzt == null)
                throw new ZtgeoBizException("多权属不动产登记业务，主要权证未设定");
        }else{
            qlxxzt = qlxxVoList.get(0);
        }
        return qlxxzt;
    }

    /**
     * 描述：获取主房产（Z房产信息）
     * 作者：chenb
     * 日期：2020/8/7
     * 参数：List<SJ_Bdc_Gl>
     * 返回：SJ_Bdc_Gl
     * 更新记录：更新人：{}，更新日期：{}
    */
    public static SJ_Bdc_Gl getMainHouse(List<SJ_Bdc_Gl> glImmovableVoList){
        if(glImmovableVoList==null || glImmovableVoList.size()<1)
            throw new ZtgeoBizException("取权属主体房产信息时，传入关联的不动产集合为空");
        SJ_Bdc_Gl bdczt = null;
        if(glImmovableVoList.size()>1){
            for(SJ_Bdc_Gl glImmovable:glImmovableVoList){
                String sslb = glImmovable.getSslb();
                if(BDC_SSLB_Z.equals(sslb)) {
                    if(bdczt != null)
                        throw new ZtgeoBizException("多户不动产登记业务，主体房产信息仅能有一个");
                    bdczt = glImmovable;
                }
            }
            if(bdczt == null)
                throw new ZtgeoBizException("多户不动产登记业务，主体房产信息未设定");
        } else {
            bdczt = glImmovableVoList.get(0);
        }
        return bdczt;
    }

    /**
     * 描述：权利人合并方法
     * 作者：chenb
     * 日期：2020/8/8
     * 参数：[zwrs]
     * 返回：SJ_Qlr_Info
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static SJ_Qlr_Info getOneQlrByList(List<SJ_Qlr_Gl> qlrgls){
        SJ_Qlr_Info qlrr = null;
        if(qlrgls!=null&& qlrgls.size()>0){
            qlrr = new SJ_Qlr_Info();
            List<SJ_Qlr_Info> qlrs = qlrgls.stream().map(SJ_Qlr_Gl::getRelatedPerson).collect(Collectors.toList());
            qlrr.setObligeeName(qlrs.stream().map(SJ_Qlr_Info::getObligeeName).collect(Collectors.joining(",")));
            qlrr.setObligeeDocumentType(qlrs.stream().map(SJ_Qlr_Info::gainTransObligeeDocumentType).collect(Collectors.joining(",")));
            qlrr.setObligeeDocumentNumber(qlrs.stream().map(SJ_Qlr_Info::getObligeeDocumentNumber).collect(Collectors.joining(",")));
        }
        return qlrr;
    }

    /**
     * 描述：新平台权利人种类业务映射关系
     * 作者：chenb
     * 日期：2020/8/8
     * 参数：[qlrlx, ywKsy]
     * 返回：String
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static String getMappinQlrzl(String qlrlx,String ywKsy){
        String sqrzl = null;
        switch (ywKsy){
            case BDC_NEW_PLAT_YW_KEY_YG:        //预告类业务，人员种类映射
                if(
                        OBLIGEE_TYPE_OF_QLR.equals(qlrlx) ||
                        OBLIGEE_TYPE_OF_GFZ.equals(qlrlx)
                )
                    sqrzl = BDC_RYZL_YGQLR;
                if(
                        OBLIGEE_TYPE_OF_YWR.equals(qlrlx) ||
                        OBLIGEE_TYPE_OF_SFZ.equals(qlrlx)
                )
                    sqrzl = BDC_RYZL_YGYWR;
                break;
            case BDC_NEW_PLAT_YW_KEY_YD:        //预告抵押类业务，人员种类映射
                if(OBLIGEE_TYPE_OF_DYR.equals(qlrlx))
                    sqrzl = BDC_RYZL_YGDYR;
                if(OBLIGEE_TYPE_OF_DYQR.equals(qlrlx))
                    sqrzl = BDC_RYZL_YGDYQR;
                break;
            case BDC_NEW_PLAT_YW_KEY_QL:        //权属确权类业务，人员种类映射
                if(
                        OBLIGEE_TYPE_OF_QLR.equals(qlrlx) ||
                        OBLIGEE_TYPE_OF_GFZ.equals(qlrlx)
                )
                    sqrzl = BDC_RYZL_QLR;
                if(
                        OBLIGEE_TYPE_OF_YWR.equals(qlrlx) ||
                        OBLIGEE_TYPE_OF_SFZ.equals(qlrlx)
                )
                    sqrzl = BDC_RYZL_YWR;
                break;
            case BDC_NEW_PLAT_YW_KEY_DY:
                if(OBLIGEE_TYPE_OF_DYR.equals(qlrlx))
                    sqrzl = BDC_RYZL_DYR;
                if(OBLIGEE_TYPE_OF_DYQR.equals(qlrlx))
                    sqrzl = BDC_RYZL_DYQR;
                break;
            case BDC_NEW_PLAT_YW_KEY_CF:
                sqrzl = qlrlx;
                break;
            case BDC_NEW_PLAT_YW_KEY_YY:
                sqrzl = BDC_RYZL_YYSQR;
                break;
            default:
                throw new ZtgeoBizException("暂不支持的业务类型-->【"+ywKsy+"】");
        }
        if(StringUtils.isBlank(sqrzl))
            throw new ZtgeoBizException("人员种类映射失败，【"+ywKsy+"】类业务【"+qlrlx+"】未查找到匹配的申请人种类");
        return sqrzl;
    }

    public static String initNeedFtpPath(String platName,String firstKey,String fileName){
        return "/"+platName.toUpperCase()
                +"/"+firstKey.toUpperCase()
                +"/"+TimeUtil.getDateString(new Date()).replaceAll("-","")
                +"/"+fileName;
    }

    /**
     * 描述：空字符串转null输出
     * 作者：chenb
     * 日期：2020/8/7
     * 参数：Str
     * 返回：Str
     * 更新记录：更新人：{}，更新日期：{}
    */
    public static String getNullStrIfk(String sourceStr){
        return StringUtils.isBlank(sourceStr)?null:sourceStr;
    }

    public static void main(String []args){
        BdcInteractService bdcInteractService = new BdcInteractService();
        List<SJ_Qlr_Gl> dlrgls = new ArrayList<>();
        SJ_Qlr_Gl dlrgl1 = new SJ_Qlr_Gl();
        SJ_Qlr_Info dlr1 = new SJ_Qlr_Info();
        dlr1.setObligeeName("李一");
        dlr1.setObligeeDocumentType("身份证");
        dlr1.setObligeeDocumentNumber("1");
        dlrgl1.setRelatedPerson(dlr1);
        dlrgls.add(dlrgl1);


        SJ_Qlr_Gl dlrgl2 = new SJ_Qlr_Gl();
        SJ_Qlr_Info dlr2 = new SJ_Qlr_Info();
        dlr2.setObligeeName("李二");
        dlr2.setObligeeDocumentType("统一社会信用代码证");
        dlr2.setObligeeDocumentNumber("2");
        dlrgl2.setRelatedPerson(dlr2);
        dlrgls.add(dlrgl2);

        SJ_Qlr_Gl dlrgl3 = new SJ_Qlr_Gl();
        SJ_Qlr_Info dlr3 = new SJ_Qlr_Info();
        dlr3.setObligeeName("李三");
        dlr3.setObligeeDocumentType("身份证");
        dlr3.setObligeeDocumentNumber("5");
        dlrgl3.setRelatedPerson(dlr3);
        dlrgls.add(dlrgl3);


        List<SJ_Qlr_Gl> qlrgls = new ArrayList<>();
        SJ_Qlr_Gl qlrgl1 = new SJ_Qlr_Gl();
        SJ_Qlr_Info qlr1 = new SJ_Qlr_Info();
        qlr1.setObligeeName("张一");
        qlr1.setObligeeDocumentType("身份证");
        qlr1.setObligeeDocumentNumber("1");
        qlrgl1.setRelatedAgent(dlr1);
        qlrgl1.setRelatedPerson(qlr1);
//        qlrgls.add(qlrgl1);


        SJ_Qlr_Gl qlrgl2 = new SJ_Qlr_Gl();
        SJ_Qlr_Info qlr2 = new SJ_Qlr_Info();
        qlr2.setObligeeName("张二");
        qlr2.setObligeeDocumentType("统一社会信用代码证");
        qlr2.setObligeeDocumentNumber("2");
        qlrgl2.setRelatedPerson(qlr2);
        qlrgls.add(qlrgl2);


        SJ_Qlr_Gl qlrgl3 = new SJ_Qlr_Gl();
        SJ_Qlr_Info qlr3 = new SJ_Qlr_Info();
        qlr3.setObligeeName("张三");
        qlr3.setObligeeDocumentType("身份证");
        qlr3.setObligeeDocumentNumber("3");
        qlrgl3.setRelatedPerson(qlr3);
        qlrgls.add(qlrgl3);

//        bdcInteractService.preCheckQlrAndDlr(qlrgls,dlrgls);
        FileNameMapping fileNameMapping = new FileNameMapping();
        fileNameMapping.setSid("167090000120");
        fileNameMapping.setLkey("ZYDJ");
        fileNameMapping.setSname("存量房买卖（含公房、解困房、安居房、经适房、安置房）");
        fileNameMapping.setMappingName(new ArrayList<>());
        List<FileNameMapping> fileNameMappings = new ArrayList<>();
        fileNameMappings.add(fileNameMapping);
        System.out.println(JSONArray.toJSONString(fileNameMappings));
    }
}
