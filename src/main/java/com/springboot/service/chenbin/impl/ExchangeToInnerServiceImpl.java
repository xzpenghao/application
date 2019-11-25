package com.springboot.service.chenbin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.AnonymousInnerComponent;
import com.springboot.component.chenbin.ExchangeToInnerComponent;
import com.springboot.component.chenbin.HttpCallComponent;
import com.springboot.component.chenbin.OtherComponent;
import com.springboot.config.DJJUser;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.chenbin.personnel.other.paph.PaphCfxx;
import com.springboot.entity.chenbin.personnel.other.paph.PaphDyxx;
import com.springboot.entity.chenbin.personnel.other.paph.PaphEntity;
import com.springboot.entity.chenbin.personnel.req.PaphReqEntity;
import com.springboot.feign.ForImmovableFeign;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.popj.registration.*;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.service.chenbin.ExchangeToInnerService;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import com.springboot.util.HttpClientUtils;
import com.springboot.util.SysPubDataDealUtil;
import com.springboot.util.chenbin.ErrorDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("exchangeToInnerService")
public class ExchangeToInnerServiceImpl implements ExchangeToInnerService {

    @Autowired
    private HttpCallComponent httpCallComponent;
    @Autowired
    private OtherComponent otherComponent;
    @Autowired
    private ForImmovableFeign immovableFeign;
    @Autowired
    private ExchangeToInnerComponent exchangeToInnerComponent;
    @Autowired
    private OuterBackFeign backFeign;

    @Value("${chenbin.idType}")
    private String idType;
    @Value("${businessType.areaNo}")
    private String areaNo;
    @Value("${businessType.dealPerson}")
    private String dealPerson;
    @Value("${chenbin.registrationBureau.username}")
    private String username;
    @Value("${chenbin.registrationBureau.password}")
    private String password;
    @Value("${chenbin.registrationBureau.forecast.commercialHouse.bizType}")
    private String bizType;
    @Value("${chenbin.registrationBureau.forecast.commercialHouse.pid}")
    private String pid;
    @Value("${chenbin.registrationBureau.forecast.commercialHouse.isSubmit}")
    private boolean isSubmit;

    @Value("${djj.bsryname}")
    private String bsryname;
    @Value("${djj.bsrypassword}")
    private String bsrypassword;

    @Value("${penghao.transferRegister.pid}")
    private String transferRegisterPid;
    @Value("${penghao.transferMortgage.pid}")
    private String transferMortgagePid;
    @Value("${businessType.registrationOfTransfer}")
    private String registrationOfTransfer;
    @Value("${businessType.transferAndMortgage}")
    private String transferAndMortgage;

    @Autowired
    private AnonymousInnerComponent anonymousInnerComponent;

    @Override
    public String dealYGYD2Inner(String commonInterfaceAttributer) throws ParseException {
        String result = "处理成功";
        log.info("转JSON前：" + commonInterfaceAttributer);
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        log.info("转JSON后：" + JSONObject.toJSONString(sjsq));
        Sj_Info_Jyhtxx jyht = sjsq.getTransactionContractInfo();
        Sj_Info_Dyhtxx dyht = sjsq.getMortgageContractInfo();
        if (jyht == null) {
            throw new ZtgeoBizException("预告预抵业务转内网办件时异常，交易合同信息为空");
        }
        if (dyht == null) {
            throw new ZtgeoBizException("预告预抵业务转内网办件时异常，交易合同信息为空");
        }
        RegistrationBureau registrationBureau = BusinessDealBaseUtil.dealBaseInfo(sjsq, pid, isSubmit, bizType, dealPerson, areaNo);
        MortgageBizInfo mortgageBizInfo = BusinessDealBaseUtil.getMortgageBizInfoByContract(jyht, dyht, idType);
        AdvanceBizInfo advanceBizInfo = new AdvanceBizInfo();
//        AdvanceBizInfo advanceBizInfo = BusinessDealBaseUtil.getAdvanceBizInfoByContract(jyht,dyht,idType);
        advanceBizInfo.setHtbh(mortgageBizInfo.getHtbh());
        advanceBizInfo.setApplyDate(mortgageBizInfo.getMortgageApplyDate());
        advanceBizInfo.setRealEstateInfoVoList(mortgageBizInfo.getRealEstateInfoVoList());
        registrationBureau.setMortgageBizInfo(mortgageBizInfo);
        registrationBureau.setAdvanceBizInfo(advanceBizInfo);

//        System.out.println("传入："+JSONObject.toJSONString(registrationBureau));
//        com.alibaba.fastjson.JSONObject  tokenObject = httpCallComponent.getTokenYcsl(DJJUser.USERNAME, DJJUser.PASSWORD);//获得token
//        String token=anonymousInnerComponent.getToken(tokenObject,"YGYD2Inner",sjsq.getRegisterNumber(),"预告预抵业务转内网办件",sjsq.getReceiptNumber());
        String token = httpCallComponent.getToken(username, password);
        if (StringUtils.isBlank(token)) {
            return Msgagger.USER_LOGIN_BAD;
        }
        //操作FTP上传附件
        List<SJ_Fjfile> fileVoList = httpCallComponent.getFileVoList(sjsq.getReceiptNumber(), token);
        log.warn("双预告附件信息获取成功，为：" + JSONArray.toJSONString(fileVoList));
//        List<ImmovableFile> fileList = otherComponent.getInnerFileListByOut(fileVoList);
//        registrationBureau.setFileInfoVoList(fileList);

        JSONObject resultObject = httpCallComponent.callRegistrationBureauForRegister(registrationBureau);
        if (resultObject != null) {
            if (!(boolean) resultObject.get("success")) {
                log.error("登记局受理失败,原因:" + resultObject.getString("message"));
                throw new ZtgeoBizException("登记局受理失败,原因:" + resultObject.getString("message"));
            }
        } else {
            throw new ZtgeoBizException("内网接口请求异常，返回数据为空，错误无法判定");
        }
        Map<String, String> mapParmeter = new HashMap<>();
        mapParmeter.put("receiptNumber", sjsq.getReceiptNumber());
        mapParmeter.put("registerNumber", resultObject.getString("slbh"));
        //返回数据到一窗受理平台保存受理编号和登记编号
        String resultJson = httpCallComponent.preservationRegistryData(mapParmeter, token);
        ObjectRestResponse<Object> resultRV = httpCallComponent.adaptationPreservationReturn(resultJson);
        if (resultRV.getStatus() == 200) {
            result = (String) resultRV.getData();
        } else {
            log.error((String) resultRV.getData());
            throw new ZtgeoBizException((String) resultRV.getData());
        }
        return result;
    }

    @Override
    public String secTra2InnerWithoutDY(String commonInterfaceAttributer) throws ParseException {
        //获取一窗受理操作token
        String token = backFeign.getToken(new JwtAuthenticationRequest(bsryname,bsrypassword)).getData();
        //处理数据
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        RegistrationBureau registrationBureau = BusinessDealBaseUtil.dealBaseInfo(sjsq, transferRegisterPid, false, registrationOfTransfer, dealPerson, areaNo);
        TransferBizInfo transferBizInfo = BusinessDealBaseUtil.getTransferBizInfoByJyhtAndBdcqls(sjsq,idType);
        registrationBureau.setTransferBizInfo(transferBizInfo);
        return handleCreateFlow(token,sjsq,registrationBureau,true);
    }

    @Override
    public String secTra2InnerWithDY(String commonInterfaceAttributer) throws ParseException {
        //获取一窗受理操作token
        String token = backFeign.getToken(new JwtAuthenticationRequest(bsryname,bsrypassword)).getData();
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        RegistrationBureau registrationBureau = BusinessDealBaseUtil.dealBaseInfo(sjsq, transferMortgagePid, false, transferAndMortgage, dealPerson, areaNo);
        TransferBizInfo transferBizInfo = BusinessDealBaseUtil.getTransferBizInfoByJyhtAndBdcqls(sjsq,idType);
        MortgageBizInfo mortgageBizInfo = BusinessDealBaseUtil.getMortgageBizInfoByDyhtAndBiztype(sjsq,idType,true);
        List<RealEstateInfo> realEstateInfos = mortgageBizInfo.getRealEstateInfoVoList();
        if(realEstateInfos!=null){
            for(RealEstateInfo realEstateInfo:realEstateInfos){
                realEstateInfo.setRealEstateId(transferBizInfo.getRealEstateId());
                realEstateInfo.setLandCertificate(transferBizInfo.getLandCertificate());
            }
        }
        registrationBureau.setTransferBizInfo(transferBizInfo);
        registrationBureau.setMortgageBizInfo(mortgageBizInfo);
        return handleCreateFlow(token,sjsq,registrationBureau,true);
    }

    @Override
    public String processAutoSubmit(String commonInterfaceAttributer) throws ParseException {
        //获取一窗受理操作token
        String token = backFeign.getToken(new JwtAuthenticationRequest(bsryname,bsrypassword)).getData();
        //处理数据
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        //回填slbh至一窗受理
        Map<String,String> params = new HashMap<String,String>();
        params.put("receiptNumber", sjsq.getReceiptNumber());
        params.put("registerNumber",sjsq.getRegisterNumber());
        backFeign.DealRecieveFromOuter2(token,params);
        return "流程自动提交成功";
    }

    @Override
    public List<PaphEntity> getPaphMortBefore(PaphReqEntity paph) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("obligeeName",paph.getQlrmc());
        params.put("obligeeId",paph.getQlrzjh());
        String bdczl = paph.getBdczl();
        if(StringUtils.isNotBlank(bdczl)) {
            String[] bdczls = bdczl.split("\\$");
            System.out.println("bdczls:" + bdczls + JSONObject.toJSONString(bdczls));
            params.put("sit", bdczls);
        }
        List<JSONObject> objs = immovableFeign.getBdcInfoByZL(params);
        System.out.println("返回："+objs+"， 详情："+JSONObject.toJSONString(objs));
        List<PaphEntity> paphEntitys = new ArrayList<PaphEntity>();
        if (objs!=null && objs.size()>0){
            for(JSONObject obj:objs){
                PaphEntity paphEntity = getBasePaph(obj);
                JSONArray mortArray = obj.getJSONArray("mortgageInfoVoList");
                if(mortArray!=null && mortArray.size()>0){
                    paphEntity.setSfdy("是");
                    List<PaphDyxx> dyxxs = getBeforeDyxxs(mortArray);
                    paphEntity.setDyxxs(dyxxs);
                }
                JSONArray attachArray = obj.getJSONArray("attachInfoVoList");
                if (attachArray!=null && attachArray.size()>0){
                    paphEntity.setSfcf("是");
                    List<PaphCfxx> cfxxs = getCfxxs(attachArray);
                    paphEntity.setCfxxs(cfxxs);
                }
                paphEntitys.add(paphEntity);
            }
        }else{
            throw new ZtgeoBizException("未查询到该权利人存在现势登记的权属信息");
        }
        return paphEntitys;
    }

    @Override
    public List<PaphEntity> getPaphMortAfter(PaphReqEntity paph) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("obligeeName",paph.getDyrmc());
        params.put("obligeeId",paph.getDyrzjh());
        List<JSONObject> objs = immovableFeign.getBdcInfoByZL(params);
        List<PaphEntity> paphEntitys = new ArrayList<PaphEntity>();
        if (objs!=null && objs.size()>0){
            for(JSONObject obj:objs){
                PaphEntity paphEntity = getBasePaph(obj);
                JSONArray mortArray = obj.getJSONArray("mortgageInfoVoList");
                if(mortArray!=null && mortArray.size()>0){
                    List<PaphDyxx> dyxxs = getAfterDyxxs(mortArray,paph);
                    if(dyxxs!=null && dyxxs.size()>0){
                        paphEntity.setSfqtdy("是");
                        paphEntity.setDyxxs(dyxxs);
                    }
                }
                JSONArray attachArray = obj.getJSONArray("attachInfoVoList");
                if (attachArray!=null && attachArray.size()>0){
                    paphEntity.setSfcf("是");
                    List<PaphCfxx> cfxxs = getCfxxs(attachArray);
                    paphEntity.setCfxxs(cfxxs);
                }
                paphEntitys.add(paphEntity);
            }
        }else{
            throw new ZtgeoBizException("未查询到该权利人存在现势登记的权属信息");
        }
        return paphEntitys;
    }

    @Override
    public List<SJ_Info_Bdcqlxgxx> getBdcQlInfoWithItsRights(ParametricData parametricData) {
        return exchangeToInnerComponent.getBdcQlInfoWithItsRights(parametricData);
    }


    public String handleCreateFlow(String token,SJ_Sjsq sjsq,RegistrationBureau registrationBureau,boolean dealFile){
        System.out.println("进入"+sjsq.getReceiptNumber()+"业务的附件获取功能");
        //处理附件
        List<SJ_Fjfile> fileVoList = httpCallComponent.getFileVoList(sjsq.getReceiptNumber(), token);
        List<ImmovableFile> immovableFileList=new ArrayList<>();
        log.warn(" 二手房转移 附件信息获取成功，为：" + JSONArray.toJSONString(fileVoList));
        if(fileVoList != null && fileVoList.size()>0) {
            for (SJ_Fjfile sjFjfile:fileVoList) {
                ImmovableFile immovableFile=new ImmovableFile();
                immovableFile.setFileAdress(sjFjfile.getFtpPath());
                immovableFile.setFileName(sjFjfile.getFileName());
                immovableFile.setFileType(sjFjfile.getFileExt());
                if (StringUtils.isEmpty(sjFjfile.getFileSize())){
                    immovableFile.setFileSize("0"); //登机平台不允许为空
                }else {
                    immovableFile.setFileSize(sjFjfile.getFileSize());
                }
                immovableFile.setpName(sjFjfile.getLogicPath());
                immovableFileList.add(immovableFile);
            }
//            List<ImmovableFile> fileList = otherComponent.getInnerFileListByOut(fileVoList,dealFile);
            registrationBureau.setFileInfoVoList(immovableFileList);
        }else{
            log.error("附件列表为空");
        }
        log.info("二手房转移业务最终传入不动产数据为：\n"+JSONObject.toJSONString(registrationBureau));
        //发送登记局
        Map<String,Object> map = null;
        try {
            map = immovableFeign.createFlow(registrationBureau);
            log.info("registerNuber"+map.get("slbh"));
        } catch (Exception e){
            log.error(sjsq.getReceiptNumber()+"号二手房转移业务创建失败，失败出现在发送不动产受理阶段，抛出异常：\n"+ ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("不动产创建二手房转移业务流程失败，失败出现在发送不动产受理阶段，抛出异常：\n"+ ErrorDealUtil.getErrorInfo(e));
        }
        if(map==null){
            log.error(sjsq.getReceiptNumber()+"号二手房转移业务创建失败，失败出现在发送不动产受理阶段，响应为空");
            throw new ZtgeoBizException("不动产创建二手房转移业务流程失败，响应为空");
        } else {
            if(!((boolean)map.get("success"))){
                log.error(sjsq.getReceiptNumber()+"号二手房转移业务创建失败，失败出现在发送不动产受理阶段，返回的错误信息为："+map.get("message"));
                throw new ZtgeoBizException("不动产创建二手房转移业务流程失败，响应为空");
            }
        }
        //回填slbh至一窗受理
        Map<String,String> params = new HashMap<String,String>();
        params.put("receiptNumber", sjsq.getReceiptNumber());
        params.put("registerNumber",(String) map.get("slbh"));
        backFeign.dealRecieveFromOuter1(token,params);
        System.out.println("进入不动产处理，受理成功！");
        return "转移登记同步内网办理成功";
    }

    private PaphEntity getBasePaph(JSONObject obj){
        PaphEntity paphEntity = new PaphEntity();
        paphEntity.setBdczh(obj.getString("realEstateId"));
        JSONObject unit = obj.getJSONObject("realEstateUnitInfoVo");
        paphEntity.setBdczl(unit.getString("sit"));
        paphEntity.setBdcdyh(unit.getString("realEstateUnitId"));
        return paphEntity;
    }

    private List<PaphDyxx> getBeforeDyxxs(JSONArray mortArray){
        List<PaphDyxx> dyxxs = new ArrayList<PaphDyxx>();
        for(int i=0;i<mortArray.size();i++){
            JSONObject mortObj = mortArray.getJSONObject(i);
            PaphDyxx dyxx = getBaseDyxx(mortObj);
            JSONArray dyqrArray = mortObj.getJSONArray("mortgageeInfoVoList");
            if(dyqrArray!=null && dyqrArray.size()>0) {
                boolean isBank = false;
                for (int j=0;j<dyqrArray.size();j++){
                    if(dyqrArray.getJSONObject(j).getString("mortgageeName").contains("银行")){
                        dyxx.setDyqr("银行");
                        isBank = true;
                        break;
                    }
                }
                if(!isBank){
                    dyxx.setDyqr("其它");
                }
            }
            dyxxs.add(dyxx);
        }
        return dyxxs;
    }
    private List<PaphDyxx> getAfterDyxxs(JSONArray mortArray,PaphReqEntity paph){
        List<PaphDyxx> dyxxs = new ArrayList<PaphDyxx>();
        for(int i=0;i<mortArray.size();i++){
            JSONObject mortObj = mortArray.getJSONObject(i);
            JSONArray dyqrArray = mortObj.getJSONArray("mortgageeInfoVoList");
            if(dyqrArray!=null && dyqrArray.size()>0) {
                boolean isPaph = false;
                boolean isBank = false;
                for (int j=0;j<dyqrArray.size();j++){
                    JSONObject dyqrObj = dyqrArray.getJSONObject(j);
                    String targetMc = dyqrObj.getString("mortgageeName");
                    String targetZh = dyqrObj.getString("mortgageeId");
                    if(
                        StringUtils.isNotBlank(targetMc)
                        && targetMc.equals(paph.getDyqrmc())
                        && StringUtils.isNotBlank(targetZh)
                        && targetZh.equals(paph.getDyqrzjh())
                    ){
                        isPaph = true;
                        break;
                    }
                    if(targetMc.contains("银行")){
                        isBank = true;
                        break;
                    }
                }
                if(isPaph){
                    continue;
                }
                PaphDyxx dyxx = getBaseDyxx(mortObj);
                if(isBank){
                    dyxx.setDyqr("银行");
                }else {
                    dyxx.setDyqr("其它");
                }
                dyxxs.add(dyxx);
            }
        }
        return dyxxs;
    }

    private List<PaphCfxx> getCfxxs(JSONArray attachArray){
        List<PaphCfxx> cfxxs = new ArrayList<PaphCfxx>();
        for(int k=0;k<attachArray.size();k++) {
            JSONObject attach = attachArray.getJSONObject(k);
            PaphCfxx cfxx = new PaphCfxx();
            cfxx.setCfqxq(attach.getString("attachStartDate"));
            cfxx.setCfqxz(attach.getString("attachEndDate"));
            cfxxs.add(cfxx);
        }
        return cfxxs;
    }

    private PaphDyxx getBaseDyxx(JSONObject mortObj){
        PaphDyxx dyxx = new PaphDyxx();
        dyxx.setDylx(mortObj.getString("mortgageType"));
        dyxx.setZqje(mortObj.getString("creditAmount"));
        String ks = mortObj.getString("mortgageStartDate");
        String js = mortObj.getString("mortgageEndDate");
        dyxx.setZwlxqx((StringUtils.isNotBlank(ks)? ks+" 至 ":"")+(StringUtils.isNotBlank(js)?js:"未定"));
        return dyxx;
    }
}
