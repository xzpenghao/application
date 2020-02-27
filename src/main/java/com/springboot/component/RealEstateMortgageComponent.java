package com.springboot.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.HttpCallComponent;
import com.springboot.config.DJJUser;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.*;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.popj.registration.*;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.popj.warrant.RealPropertyCertificate;
import com.springboot.popj.warrant.ZdInfo;
import com.springboot.service.chenbin.impl.ExchangeToInnerServiceImpl;

import com.springboot.util.DateUtils;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import com.springboot.util.HttpClientUtils;
import com.springboot.util.SysPubDataDealUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class
RealEstateMortgageComponent {

    @Autowired
    private HttpCallComponent httpCallComponent;

    @Autowired
    private HttpClientUtils httpClientUtils;
    @Value("${httpclient.ip}")
    private String ip;
    @Value("${httpclient.seam}")
    private String seam;
    @Value("${businessType.areaNo}")
    private String areaNo;
    @Value("${businessType.dealPerson}")
    private String dealPerson;
    @Value("${businessType.bankPerson}")
    private String bankPerson;
    @Value("${glHouseBuyer.obligeeDyqr}")
    private String obligeeDyqr;
    @Value("${glHouseBuyer.obligeeDyr}")
    private String obligeeDyr;
    @Value("${glHouseBuyer.obligeeZwr}")
    private String obligeeZwr;
    @Value("${glHouseBuyer.obligeeQlr}")
    private String obligeeQlr;
    @Value("${glHouseBuyer.obligeeYwr}")
    private String obligeeYwr;
    @Value("${businessType.grMortgageCancellation}")
    private String grMortgageCancellation;//#（抵押注销(个人)）
    @Value("${businessType.clfFmortgage}")
    private String clfFmortgage;
    @Value("${businessType.xjfFmortgage}")
    private String xjfFmortgage;
    @Value("${businessType.grMortgageRegistration}")
    private String grMortgageRegistration;
    @Value("${businessType.registrationOfTransfer}")
    private String registrationOfTransfer;
    @Value("${businessType.registrationOfReplacement}")
    private String registrationOfReplacement;
    @Value("${businessType.registrationOfSupplementary}")
    private String registrationOfSupplementary;
    @Value("${businessType.cancellationOfWarrants}")
    private String cancellationOfWarrants;
    @Value("${businessType.forewarningMortgage}")
    private String forewarningMortgage;
    @Value("${businessType.transferAndMortgage}")
    private String transferAndMortgage;
    @Value("${businessType.vormerkung}")
    private String vormerkung;
    @Value("${httpclient.windowAcceptanceIp}")
    private String windowAcceptanceIp; //一窗受理ip
    @Value("${httpclient.windowAcceptanceSeam}")
    private String windowAcceptanceSeam; //一窗受理接口
    @Value("${penghao.mortgageCancellation.pid}")
    private String mortgagePid;
    @Value("${penghao.transferRegister.pid}")
    private String transferRegisterPid;
    @Value("${penghao.realEstateRenewal.pid}")
    private String realEstateRenewalPid;
    @Value("${penghao.supplementaryEvidence.pid}")
    private String supplementaryEvidencePid;
    @Value("${penghao.transferMortgage.pid}")
    private String transferMortgagePid;
    @Value("${penghao.cancellationOfWarrants.pid}")
    private String cancellationOfWarrantsPid;
    @Value("${penghao.mortgageRegistration.pid}")
    private String registrationPid;
    @Value("${djj.tsryname}")
    private String bsryname;
    @Value("${djj.tsrypaaword}")
    private String bsrypassword;

    @Autowired
    private AnonymousInnerComponent anonymousInnerComponent;
    @Autowired
    private ExchangeToInnerServiceImpl exchangeToInnerService;
    @Autowired
    private OuterBackFeign backFeign;


    /**
     * 发送登记局数据 返回受理编号  (转移登记及抵押）
     *
     * @param commonInterfaceAttributer 接收参数
     * @return
     */
    public ObjectRestResponse sendTransferMortgage(String commonInterfaceAttributer) throws ParseException {
        //获取json数据转成收件申请
        SJ_Sjsq sjSjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        //加载到registrationBureau中
        RegistrationBureau registrationBureau = BusinessDealBaseUtil.dealBaseInfo(sjSjsq, transferMortgagePid, true, registrationOfTransfer, dealPerson, areaNo);
        //加载转移信息
        registrationBureau = getTransferRegister(sjSjsq.getTransactionContractInfo(),sjSjsq.getImmovableRightInfoVoList(),registrationBureau);
        registrationBureau.getTransferBizInfo().setRegisterSubType(sjSjsq.getBusinessType());
        //加载抵押信息
        registrationBureau =ClAutoRealPropertyCertificate(sjSjsq,registrationBureau);
        JSONObject resultObject = httpCallComponent.callRegistrationBureauForRegister(registrationBureau);
        return getObjectRestResponse(sjSjsq, resultObject);
    }



    /**
     * 发送登记局数据 返回受理编号  (转移登记）
     *
     * @param commonInterfaceAttributer 接收参数
     * @return
     */
    public ObjectRestResponse sendTransferRegister(String commonInterfaceAttributer) throws ParseException {
        //获取json数据转成收件申请
        SJ_Sjsq sjSjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        System.out.println("二手房转移sjsq:"+JSONObject.toJSONString(sjSjsq));
        //加载到registrationBureau中
        RegistrationBureau registrationBureau = BusinessDealBaseUtil.dealBaseInfo(sjSjsq, transferRegisterPid, true, registrationOfTransfer, dealPerson, areaNo);
        //加载转移信息
        registrationBureau = getTransferRegister(sjSjsq.getTransactionContractInfo(),sjSjsq.getImmovableRightInfoVoList(),registrationBureau);
        registrationBureau.getTransferBizInfo().setRegisterSubType(sjSjsq.getBusinessType());
        JSONObject resultObject = httpCallComponent.callRegistrationBureauForRegister(registrationBureau);
        return getObjectRestResponse(sjSjsq, resultObject);
    }


    /**
     * 换证业务登记信息
     * @param commonInterfaceAttributer
     * @return
     * @throws ParseException
     */
    public ObjectRestResponse realEstateRenewal(String commonInterfaceAttributer) throws ParseException {
        //获取json数据转成收件申请
        SJ_Sjsq sjSjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        log.info("换证业务sjsq:"+JSONObject.toJSONString(sjSjsq));
        //加载到registrationBureau中
        RegistrationBureau registrationBureau = BusinessDealBaseUtil.dealBaseInfo(sjSjsq, realEstateRenewalPid, true, registrationOfReplacement, dealPerson, areaNo);
        registrationBureau = ClrealEstateRenewal(sjSjsq,registrationBureau);
        registrationBureau.getTransferBizInfo().setRegisterSubType(sjSjsq.getBusinessType());
        JSONObject resultObject = httpCallComponent.callRegistrationBureauForRegister(registrationBureau);
        return getObjectRestResponse(sjSjsq, resultObject);
    }


    /**
     * 补证业务登记信息
     * @param commonInterfaceAttributer
     * @return
     * @throws ParseException
     */
    public ObjectRestResponse supplementaryEvidence(String commonInterfaceAttributer) throws ParseException {
        //获取json数据转成收件申请
        SJ_Sjsq sjSjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        log.info("补证业务sjsq:"+JSONObject.toJSONString(sjSjsq));
        //加载到registrationBureau中
        RegistrationBureau registrationBureau = BusinessDealBaseUtil.dealBaseInfo(sjSjsq, supplementaryEvidencePid, true, registrationOfSupplementary, dealPerson, areaNo);
        registrationBureau = ClSupplementary(sjSjsq,registrationBureau);
        registrationBureau.getTransferBizInfo().setRegisterSubType(sjSjsq.getBusinessType());
        JSONObject resultObject = httpCallComponent.callRegistrationBureauForRegister(registrationBureau);
        return getObjectRestResponse(sjSjsq, resultObject);
    }


    /**
     * 权证注销业务登记信息
     * @param commonInterfaceAttributer
     * @return
     * @throws ParseException
     */
    public ObjectRestResponse cancellationOfWarrants(String commonInterfaceAttributer) throws ParseException {
        //获取json数据转成收件申请
        SJ_Sjsq sjSjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        log.info("补证业务sjsq:"+JSONObject.toJSONString(sjSjsq));
        //加载到registrationBureau中
        RegistrationBureau registrationBureau = BusinessDealBaseUtil.dealBaseInfo(sjSjsq,cancellationOfWarrantsPid, true, cancellationOfWarrants, dealPerson, areaNo);
        registrationBureau = ClcancellationOfWarrants(sjSjsq,registrationBureau);
        registrationBureau.getTransferBizInfo().setRegisterSubType(sjSjsq.getBusinessType());
        JSONObject resultObject = httpCallComponent.callRegistrationBureauForRegister(registrationBureau);
        return getObjectRestResponse(sjSjsq, resultObject);
    }


    /**
     * 处理换证数据进行转内网
     * @param sjSjsq
     * @param registrationBureau
     * @return
     */
    private RegistrationBureau ClrealEstateRenewal(SJ_Sjsq sjSjsq,RegistrationBureau registrationBureau){
        ReplaceBizInfo replaceBizInfo=new ReplaceBizInfo();
        if (null != sjSjsq.getImmovableRightInfoVoList() && sjSjsq.getImmovableRightInfoVoList().size()==1){
            replaceBizInfo.setRealEstateId(sjSjsq.getImmovableRightInfoVoList().get(0).getImmovableCertificateNo());
            replaceBizInfo.setCertificateType("BDCZH");
        }else if (null != sjSjsq.getImmovableRightInfoVoList() && sjSjsq.getImmovableRightInfoVoList().size()>1){
            replaceBizInfo.setCertificateType("BDCZH");
            for (SJ_Info_Bdcqlxgxx sjInfoBdcqlxgxx:
                 sjSjsq.getImmovableRightInfoVoList()) {
                if (StringUtils.isNotEmpty(sjInfoBdcqlxgxx.getImmovableCertificateNo())){
                    replaceBizInfo.setRealEstateId(sjInfoBdcqlxgxx.getImmovableCertificateNo()); //不动产权证号
                }else if (StringUtils.isNotEmpty(sjInfoBdcqlxgxx.getLandCertificateNo())){
                    replaceBizInfo.setLandCertificate(sjInfoBdcqlxgxx.getLandCertificateNo());//土地证号
                }else if (StringUtils.isNotEmpty(sjInfoBdcqlxgxx.getHouseCertificateNo())){
                    replaceBizInfo.setRealEstateId(sjInfoBdcqlxgxx.getHouseCertificateNo());//房屋证号
                }
            }
        }
        registrationBureau.setReplaceBizInfo(replaceBizInfo);
        return  registrationBureau;
    }


    /**
     * 处理补证数据进行转内网
     * @param sjSjsq
     * @param registrationBureau
     * @return
     */
    private RegistrationBureau ClSupplementary(SJ_Sjsq sjSjsq,RegistrationBureau registrationBureau){
        ReissueBizInfo reissueBizInfo=new ReissueBizInfo();
        if (null != sjSjsq.getImmovableRightInfoVoList() && sjSjsq.getImmovableRightInfoVoList().size()==1){
            reissueBizInfo.setRealEstateId(sjSjsq.getImmovableRightInfoVoList().get(0).getImmovableCertificateNo());
            reissueBizInfo.setCertificateType("BDCZH");
        }else if (null != sjSjsq.getImmovableRightInfoVoList() && sjSjsq.getImmovableRightInfoVoList().size()>1){
            reissueBizInfo.setCertificateType("BDCZH");
            for (SJ_Info_Bdcqlxgxx sjInfoBdcqlxgxx:
                    sjSjsq.getImmovableRightInfoVoList()) {
                if (StringUtils.isNotEmpty(sjInfoBdcqlxgxx.getImmovableCertificateNo())){
                    reissueBizInfo.setRealEstateId(sjInfoBdcqlxgxx.getImmovableCertificateNo()); //不动产权证号
                }else if (StringUtils.isNotEmpty(sjInfoBdcqlxgxx.getLandCertificateNo())){
                    reissueBizInfo.setLandCertificate(sjInfoBdcqlxgxx.getLandCertificateNo());//土地证号
                }else if (StringUtils.isNotEmpty(sjInfoBdcqlxgxx.getHouseCertificateNo())){
                    reissueBizInfo.setRealEstateId(sjInfoBdcqlxgxx.getHouseCertificateNo());//房屋证号
                }
            }
        }
        registrationBureau.setReissueBizInfo(reissueBizInfo);
        return  registrationBureau;
    }



    /**
     * 处理权证注销数据进行转内网
     * @param sjSjsq
     * @param registrationBureau
     * @return
     */
    private RegistrationBureau ClcancellationOfWarrants(SJ_Sjsq sjSjsq,RegistrationBureau registrationBureau){
        RevokeBizInfo revokeBizInfo=new RevokeBizInfo();
        revokeBizInfo.setRegisterSubType(sjSjsq.getRegistrationSubclass());//登记小类
        revokeBizInfo.setRevokeReason(sjSjsq.getRegistrationReason());//登记原因
        List<RealEstateInfo> realEstateInfoVoList=new ArrayList<>();
        if (null != sjSjsq.getImmovableRightInfoVoList()){
            for (SJ_Info_Bdcqlxgxx sjInfoBdcqlxgxx:
                    sjSjsq.getImmovableRightInfoVoList()) {
                RealEstateInfo realEstateInfo=new RealEstateInfo();
                if (StringUtils.isNotEmpty(sjInfoBdcqlxgxx.getImmovableCertificateNo())){
                    realEstateInfo.setRealEstateId(sjInfoBdcqlxgxx.getImmovableCertificateNo()); //不动产权证号
                }else if (StringUtils.isNotEmpty(sjInfoBdcqlxgxx.getLandCertificateNo())){
                    realEstateInfo.setLandCertificate(sjInfoBdcqlxgxx.getLandCertificateNo());//土地证号
                }else if (StringUtils.isNotEmpty(sjInfoBdcqlxgxx.getHouseCertificateNo())){
                    realEstateInfo.setRealEstateId(sjInfoBdcqlxgxx.getHouseCertificateNo());//房屋证号
                }
                realEstateInfoVoList.add(realEstateInfo);
            }
        }
        revokeBizInfo.setRealEstateInfoVoList(realEstateInfoVoList);
        registrationBureau.setRevokeBizInfo(revokeBizInfo);
        return  registrationBureau;
    }



    /**
     * 发送登记局数据 返回受理编号 (抵押注销登记)
     *
     * @param commonInterfaceAttributer
     * @return
     */
    public String sendRegistrationMortgageRevocation(String commonInterfaceAttributer) throws ParseException {
        RegistrationBureau registrationBureauVo = null;
        //获取json数据转成收件申请
        SJ_Sjsq sjSjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        RegistrationBureau registrationBureau = BusinessDealBaseUtil.dealBaseInfo(sjSjsq, mortgagePid, false, grMortgageCancellation, bankPerson, areaNo);
        switch (sjSjsq.getBusinessType()) {
            case Msgagger.CANCELLATION_REGISTRATION:
                registrationBureau.setBizType(grMortgageCancellation);
                if (null != sjSjsq.getImmovableCurrentMortgageInfoVoList() || sjSjsq.getImmovableCurrentMortgageInfoVoList().size() != 0) {
                    registrationBureauVo = getRevokeBizInfo(sjSjsq.getImmovableCurrentMortgageInfoVoList(), registrationBureau);
                }
                break;
        }
        String token = backFeign.getToken(new JwtAuthenticationRequest(bsryname,bsrypassword)).getData();
        //做ftp操作
        return exchangeToInnerService.handleCreateFlow(token,sjSjsq,registrationBureauVo,false);
    }


    private ObjectRestResponse getObjectRestResponse(SJ_Sjsq sjSjsq, JSONObject resultObject) {
        ObjectRestResponse resultRV = new ObjectRestResponse();
        if (null == resultObject){
            resultRV.setStatus(20500);
            return resultRV.data(Msgagger.DJPT_P_BAD);
        }
        String message = resultObject.getString("message");
        boolean success = (boolean) resultObject.get("success");
        if (success == false) {
            log.error("登记局受理失败,原因:" + message);
            resultRV.setStatus(20500);
            return resultRV.data(message);
        }
        System.out.println("json数据:" + resultObject.toJSONString());
        Map<String, String> mapParmeter = new HashMap<>();
        mapParmeter.put("receiptNumber", sjSjsq.getReceiptNumber());
        mapParmeter.put("registerNumber", resultObject.getString("slbh"));
        //登记局登录
        System.out.println("jsonobject:" + JSON.toJSONString(mapParmeter));
        com.alibaba.fastjson.JSONObject tokenObject = httpCallComponent.getTokenYcsl(DJJUser.USERNAME, DJJUser.PASSWORD);//获得token
        String token = anonymousInnerComponent.getToken(tokenObject, "sendRegistrationMortgageRevocation", sjSjsq.getReceiptNumber(),
                "登记局受理", sjSjsq.getRegisterNumber());
        if (token == null) {
            resultRV.setMessage(Msgagger.USER_LOGIN_BAD);
            return resultRV;
        }
        //返回数据到一窗受理平台保存受理编号和登记编号
        String resultJson = httpCallComponent.preservationRegistryData(mapParmeter, token);
        resultRV = httpCallComponent.adaptationPreservationReturn(resultJson);
        return resultRV;
    }


//    private String preservationRegistryData(Map<String,String> map,String token){
//        Map<String,String> header = new HashMap<String,String>();
//        header.put("Authorization",token);
//        String json = ParamHttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost,
//                "application/json",
//                "http://" + windowAcceptanceIp + ":" + windowAcceptanceSeam + "/api/biz/RecService/DealRecieveFromOuter1",
//                        map,header);
//        JSONObject jsonObject=(JSONObject) JSONObject.parse(json);
//        System.out.println("chenbin返回信息为："+jsonObject);
//        return json;
//    }
//
//
//    public String getToken(String username,String password){
//        Map<String,String> map=new HashMap<>();
//        map.put("username",username);
//        map.put("password",password);
//        Map<String,String> header = new HashMap<String,String>();
//        String json = ParamHttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost,
//                "application/json",
//                "http://" + windowAcceptanceIp + ":" + windowAcceptanceSeam + "/jwt/token", map,header);
//        JSONObject jsonObject=(JSONObject) JSONObject.parse(json);
//        Integer status=jsonObject.getInteger("status");
//        if (status!=200){
//            log.error("用户名或密码错误,找不到对应用户");
//            return null;
//        }
//        String data=jsonObject.getString("data");
//        return data;
//    }


    private RegistrationBureau getImmovableRightInfo(List<SJ_Info_Bdcqlxgxx> sjInfoBdcqlxgxxList, RegistrationBureau registrationBureau) {
//        //不动产权利信息集合
//        for (SJ_Info_Bdcqlxgxx info:sjInfoBdcqlxgxxList) {
//            TransferBizInfo transferBizInfo=new TransferBizInfo();
//            transferBizInfo.setRealEstateId(info.getImmovableCertificateNo());//不动产权证号
//            transferBizInfo.setLandCertificate(info.getLandCertificateNo());
//        }
        return null;
    }

    //返回抵押注销数据
    private RegistrationBureau getRevokeBizInfo(List<Sj_Info_Bdcdyxgxx> sj_info_bdcdyxgxxes, RegistrationBureau registrationBureau) {
        for (Sj_Info_Bdcdyxgxx info : sj_info_bdcdyxgxxes) {
            RevokeBizInfo revokeBizInfo = new RevokeBizInfo();
            revokeBizInfo.setRevokeApplyDate(info.getRegistrationDate());//业务登记时间
            revokeBizInfo.setWarrantId(info.getMortgageCertificateNo());
            revokeBizInfo.setRevokeReason(Msgagger.MORTGAGE_CANCELLATION);
            registrationBureau.setRevokeBizInfo(revokeBizInfo);
        }
        return registrationBureau;
    }

    /**
     * 交易合同数据发送登记局
     * @param sjInfoJyhtxx
     * @param registrationBureau
     * @return
     */
    private RegistrationBureau getTransferRegister(Sj_Info_Jyhtxx sjInfoJyhtxx,List<SJ_Info_Bdcqlxgxx> immovableRightInfoVoList,RegistrationBureau registrationBureau){
        TransferBizInfo transferBizInfo=new TransferBizInfo();
        for (SJ_Info_Bdcqlxgxx sj_info_bdcqlxgxx:immovableRightInfoVoList) {
            transferBizInfo.setRealEstateId(sj_info_bdcqlxgxx.getImmovableCertificateNo());  //不动产权证号
        }
        transferBizInfo.setHtbh(sjInfoJyhtxx.getContractRecordNumber());
        transferBizInfo.setTransferReason(Msgagger.BUSINESS);
        transferBizInfo.setObligeeInfoVoList(Qlrxx(sjInfoJyhtxx.getGlHouseBuyerVoList()));
        registrationBureau.setTransferBizInfo(transferBizInfo);
        return registrationBureau;
    }

    /**
     * 处理权利人信息
     * @param glHouseSellerVoList
     * @return
     */
    private List<QlrGlMortgator>  Qlrxx(List<SJ_Qlr_Gl> glHouseSellerVoList){
        List<QlrGlMortgator> obligeeInfoVoList=new ArrayList<>();
        for (SJ_Qlr_Gl sjQlrGl:glHouseSellerVoList) {
            QlrGlMortgator qlrGlMortgator=new QlrGlMortgator();
            qlrGlMortgator.setObligeeName(sjQlrGl.getObligeeName());
            qlrGlMortgator.setObligeeId(sjQlrGl.getRelatedPerson().getObligeeDocumentNumber());
            qlrGlMortgator.setObligeeIdType(sjQlrGl.getRelatedPerson().getObligeeDocumentType());
            obligeeInfoVoList.add(qlrGlMortgator);
        }
        return obligeeInfoVoList;
    }


    /**
     * 获取不动产抵押数据
     *
     * @param sj_info_bdcdyxgxxes
     * @param registrationBureau
     * @return
     */
    private RegistrationBureau getImmovableCurrentMortgageInfo(List<Sj_Info_Bdcdyxgxx> sj_info_bdcdyxgxxes, RegistrationBureau registrationBureau) {
        for (Sj_Info_Bdcdyxgxx info : sj_info_bdcdyxgxxes) {
            MortgageBizInfo mortgageBizInfo = new MortgageBizInfo();
            mortgageBizInfo.setMortgageApplyDate(info.getRegistrationDate());//登记日期
            mortgageBizInfo.setMortgageWay(info.getMortgageMode());//抵押方式
            mortgageBizInfo.setCreditAmount(info.getCreditAmount());//
            mortgageBizInfo.setEvaluationValue(info.getValuationValue());
            mortgageBizInfo.setMortgageTerm(info.getMortgagePeriod());
            mortgageBizInfo.setMortgageStartDate(info.getMortgageStartingDate());
            mortgageBizInfo.setMortgageEndDate(info.getMortgageEndingDate());
            mortgageBizInfo.setMortgageReason(info.getMortgageReason());
            if (registrationBureau.getBizType().equals(grMortgageCancellation)) {
                RevokeBizInfo revokeBizInfo = new RevokeBizInfo();
                revokeBizInfo.setRevokeApplyDate(mortgageBizInfo.getMortgageApplyDate());//业务登记时间
                revokeBizInfo.setWarrantId(info.getMortgageCertificateNo());
                registrationBureau.setRevokeBizInfo(revokeBizInfo);
            }
            if (info.getGlImmovableVoList() != null && info.getGlImmovableVoList().size() != 0) {
                //调用不动产
                getRealEstateUnitInfo(info.getGlImmovableVoList());
            }
            //抵押权人信息
            if (info.getGlMortgageHolderVoList() != null && info.getGlMortgageHolderVoList().size() != 0) {
                //调用抵押权人
                mortgageBizInfo.setMortgageeInfoVoList(getDyqrGlMortgator(info.getGlMortgageHolderVoList()));
            }
            if (info.getGlMortgagorVoList() != null && info.getGlMortgagorVoList().size() != 0) {
                //调用抵押人
                mortgageBizInfo.setMortgagorInfoVoList(getDyrGlMortgator(info.getGlMortgagorVoList()));
            }
            registrationBureau.setMortgageBizInfo(mortgageBizInfo);
        }
        return registrationBureau;
    }


    //抵押权人
    public List getDyqrGlMortgator(List<SJ_Qlr_Gl> sjQlrGlList) {
        List<DyqrGlMortgator> dyqrGlMortgators = new ArrayList<>();
        for (SJ_Qlr_Gl holder : sjQlrGlList) {
            DyqrGlMortgator dyqrGlMortgator = new DyqrGlMortgator();
            dyqrGlMortgator.setMortgageeId(holder.getRelatedPerson().getObligeeDocumentNumber());
            dyqrGlMortgator.setMortgageeIdType(holder.getRelatedPerson().getObligeeDocumentType());
            dyqrGlMortgator.setMortgageeName(holder.getObligeeName());
            dyqrGlMortgators.add(dyqrGlMortgator);
        }
        return dyqrGlMortgators;
    }

    //不动产单元信息列表
    public List<RealEstateUnitInfo> getRealEstateUnitInfo(List<SJ_Bdc_Gl> sjQlrGlList) {
        List<RealEstateUnitInfo> realEstateUnitInfoList = new ArrayList<>();
        for (SJ_Bdc_Gl sjQlrGl : sjQlrGlList) {
            RealEstateUnitInfo realEstateUnitInfo = new RealEstateUnitInfo();
            realEstateUnitInfo.setRealEstateUnitId(sjQlrGl.getFwInfo().getImmovableUnitNumber());//不动产单元号
            realEstateUnitInfo.setHouseholdId(sjQlrGl.getFwInfo().getHouseNumber());//户编号
            realEstateUnitInfoList.add(realEstateUnitInfo);
        }
        return realEstateUnitInfoList;
    }

    //抵押人
    public List<DyrGlMortgator> getDyrGlMortgator(List<SJ_Qlr_Gl> sjQlrGlList) {
        List<DyrGlMortgator> dyqrGlMortgators = new ArrayList<>();
        for (SJ_Qlr_Gl sjQlrGl : sjQlrGlList) {
            DyrGlMortgator dyrGlMortgator = new DyrGlMortgator();
            dyrGlMortgator.setMortgagorId(sjQlrGl.getRelatedPerson().getObligeeDocumentNumber());
            dyrGlMortgator.setMortgagorIdType(sjQlrGl.getRelatedPerson().getObligeeDocumentType());
            dyrGlMortgator.setMortgagorName(sjQlrGl.getObligeeName());
            dyqrGlMortgators.add(dyrGlMortgator);
        }
        return dyqrGlMortgators;
    }

    /**
     * 不动产预告证明号查询
     *
     * @param ygCancellcation
     * @return
     */
    public ObjectRestResponse getMortgageCancellation(String ygCancellcation) throws Exception {
        ObjectRestResponse resultRV = new ObjectRestResponse();
        if (StringUtils.isEmpty(ygCancellcation)) {
            throw new ZtgeoBizException(Msgagger.MORTGAGE_CERTIFICATE_NULL);
        }
//        String json = httpClientUtils.doGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetBdcInfoByYGZMH", map, null);
        String json = httpClientUtils.paramGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetBdcInfoByYGZMH"+ "?YGZMH=" + ygCancellcation);
        System.out.println("aa" + getRealPropertySj(json, ygCancellcation));
        return resultRV.data(getRealPropertySj(json, ygCancellcation));
    }

    /**
     * 二手房水电气获取权属信息
     *
     * @param jsonObject
     * @return
     */
    public RealPropertyCertificate getRealPropertyCertificatexx(JSONObject jsonObject, JSONObject obj1) {
        return getRealProperty(jsonObject, obj1);
    }


    private RealPropertyCertificate getRealProperty(JSONObject jsonObject, JSONObject obj1) {
        RealPropertyCertificate realPropertyCertificate = new RealPropertyCertificate();
        if (StringUtils.isEmpty(jsonObject.getString("realEstateId"))) {
            realPropertyCertificate.setForecastCertificateNos(jsonObject.getString("vormerkungId").split(","));
        } else {
            realPropertyCertificate.setImmovableCertificateNo(jsonObject.getString("realEstateId"));
        }
        if (StringUtils.isEmpty(jsonObject.getString("certificateType"))) {
            realPropertyCertificate.setCertificateType("不动产权证");
        } else {
            if (jsonObject.getString("certificateType").equals("房屋不动产证")) {
                realPropertyCertificate.setCertificateType("不动产权证");
            } else if (jsonObject.getString("certificateType").equals("房产证")) {
                realPropertyCertificate.setCertificateType("房产证");
            } else if (jsonObject.getString("certificateType").equals("土地证")) {
                realPropertyCertificate.setCertificateType("土地证");
            }
        }
        realPropertyCertificate.setRegistrationDate(jsonObject.getString("registerDate"));
        JSONArray glImmovablejsonArray = (JSONArray) jsonObject.get("realEstateUnitInfoVoList");//房屋信息
        if (null != glImmovablejsonArray) {
            for (int z = 0; z < glImmovablejsonArray.size(); z++) {
                //房屋信息
                JSONObject glImmovableObject = glImmovablejsonArray.getJSONObject(z);
                GlImmovable glImmovable = new GlImmovable();
                glImmovable.setImmovableType(Msgagger.FANGDI);
                // glImmovable.setImmovableId(glImmovableObject.getString("householdId"));
                FwInfo fwInfo = getFwInfo(glImmovableObject, jsonObject, "无");
                if (fwInfo.getArchitecturalArea() != null){
                    realPropertyCertificate.setArchitecturalArea(fwInfo.getArchitecturalArea());//建筑面积
                }
                if (fwInfo.getHouseArchitecturalArea() != null){
                    realPropertyCertificate.setHouseArchitecturalArea(fwInfo.getHouseArchitecturalArea());////套内建筑面积
                }
                if (fwInfo.getApportionmentArchitecturalArea()!=null){
                    realPropertyCertificate.setApportionmentArchitecturalArea(fwInfo.getApportionmentArchitecturalArea());//分摊建筑面积
                }
                realPropertyCertificate.setHouseObtainingWays(glImmovableObject.getString("acquireWay"));//房屋取得方式
                if (glImmovableObject.getString("acquirePrice") != null){
                    realPropertyCertificate.setApportionmentArchitecturalArea(new BigDecimal(glImmovableObject.getString("acquirePrice")));//房屋获取价格
                }
                realPropertyCertificate.setHousePlanningPurpose(glImmovableObject.getString("plannedUsage"));//房屋规划用途
                realPropertyCertificate.setHouseType(glImmovableObject.getString("houseType"));//房屋类型
                realPropertyCertificate.setHouseRightType(glImmovableObject.getString("houseRightType"));//房屋权利类型
                realPropertyCertificate.setHouseRightNature(glImmovableObject.getString("houseRightNature"));//房屋性质
                realPropertyCertificate.setLandRightNature(glImmovableObject.getString("landRightNature"));//土地权利性质
                realPropertyCertificate.setLandUseRightStartingDate(glImmovableObject.getString("landRightStartDate"));//土地起始日期
                realPropertyCertificate.setLandUseRightEndingDate(glImmovableObject.getString("landRightEndDate"));//土地终止日期
                realPropertyCertificate.setLandUseRightOwner(glImmovableObject.getString("landRightUser"));//土地使用权人
                realPropertyCertificate.setLandUseTimeLimit(glImmovableObject.getString("landRightTerm"));//土地使用期限
                realPropertyCertificate.setLandPurpose(glImmovableObject.getString("landUsage"));//土地用途
                if (glImmovableObject.getString("commonLandArea")!=null){
                    realPropertyCertificate.setCommonLandArea(new BigDecimal(glImmovableObject.getString("commonLandArea")));//分摊建筑面积
                }
                if (glImmovableObject.getString("sharedLandArea")!=null){
                    realPropertyCertificate.setShareLandArea(new BigDecimal(glImmovableObject.getString("sharedLandArea")));//分摊建筑面积
                }
                if (glImmovableObject.getString("singleLandArea")!=null){
                    realPropertyCertificate.setSingleLandArea(new BigDecimal(glImmovableObject.getString("singleLandArea")));//分摊建筑面积
                }
                if (glImmovableObject.getString("architectureLandArea")!=null){
                    realPropertyCertificate.setBuildingParcelArea(new BigDecimal(glImmovableObject.getString("architectureLandArea")));//分摊建筑面积
                }
                glImmovable.setFwInfo(fwInfo);
                realPropertyCertificate.getGlImmovableVoList().add(glImmovable);
            }
        }
        //预告证明号
        JSONArray ygInfojsonArray = (JSONArray) jsonObject.get("advanceInfoVoList");
        if (null != ygInfojsonArray) {
            String[] kung = new String[ygInfojsonArray.size()];
            if (null != ygInfojsonArray) {
                for (int j = 0; j < ygInfojsonArray.size(); j++) {
                    JSONObject ygObject = ygInfojsonArray.getJSONObject(j);
                    kung[j] = ygObject.getString("vormerkungId");
                }
                realPropertyCertificate.setForecastCertificateNos(kung);
            }
        }
        //抵押证明号
        JSONArray dyInfojsonArray = (JSONArray) jsonObject.get("mortgageInfoVoList");
        if (null != dyInfojsonArray) {
            String[] dyzmh = new String[dyInfojsonArray.size()];
            if (null != dyInfojsonArray) {
                for (int j = 0; j < dyInfojsonArray.size(); j++) {
                    JSONObject dyObject = dyInfojsonArray.getJSONObject(j);
                    dyzmh[j] = dyObject.getString("warrantId");
                }
                realPropertyCertificate.setWarrantNos(dyzmh);
            }
        }
        //宗地信息
        JSONArray zdInfojsonArray = (JSONArray) jsonObject.get("landUnitInfoVoList");
        if (null != zdInfojsonArray) {
            for (int j = 0; j < zdInfojsonArray.size(); j++) {
                JSONObject zdObject = zdInfojsonArray.getJSONObject(j);
                GlImmovable glImmovable = getZdInfo(zdObject, jsonObject,realPropertyCertificate);
                realPropertyCertificate.getGlImmovableVoList().add(glImmovable);
            }
        }
        //水电气信息
//        JSONObject sdqObject=(JSONObject) JSONObject.parse(jsonObject.getString("sdqInfo"));
        if (null != obj1) {
            if (obj1.getString("shhh") != null) {
                realPropertyCertificate.setWaterNumber(obj1.getString("shhh"));
            } else {
                realPropertyCertificate.setWaterNumber(obj1.getString("xshhh"));
            }
            if (obj1.getString("dhhh") != null) {
                realPropertyCertificate.setElectricNumber(obj1.getString("dhhh"));
            } else {
                realPropertyCertificate.setElectricNumber(obj1.getString("xdhhh"));
            }
            if (obj1.getString("qhhh") != null) {
                realPropertyCertificate.setGasNumber(obj1.getString("qhhh"));
            } else {
                realPropertyCertificate.setGasNumber(obj1.getString("xqhhh"));
            }
            if (obj1.getString("thhh")!= null) {
                realPropertyCertificate.setWiredNumber(obj1.getString("thhh"));
            } else {
                realPropertyCertificate.setWiredNumber(obj1.getString("xthh"));
            }
        }
        //权利人信息
        JSONArray obligeeInfoArray = (JSONArray) jsonObject.get("obligeeInfoVoList");
        if (null != obligeeInfoArray) {
            for (int j = 0; j < obligeeInfoArray.size(); j++) {
                JSONObject glMortgagorObject = obligeeInfoArray.getJSONObject(j);
                GlMortgagor glMortgagor = getGlMortgageQlr(glMortgagorObject);
                realPropertyCertificate.getGlObligeeVoList().add(glMortgagor);
            }
        }
        //义务人(暂时不需要)
//        JSONArray selerInfoVoArray = (JSONArray) jsonObject.get("salerInfoVoList");
//        if (null != selerInfoVoArray) {
//            for (int j = 0; j < selerInfoVoArray.size(); j++) {
//                JSONObject selerInfoVoObject = selerInfoVoArray.getJSONObject(j);
//                GlMortgagor glMortgagor = getGlMortgageYwr(selerInfoVoObject);
//                realPropertyCertificate.getGlObligorVoList().add(glMortgagor);
//            }
//        }
        return realPropertyCertificate;
    }


    /**
     * 获取不动产权属信息
     *
     * @param json
     * @param ygCancellcation
     * @return
     */
    public List<RealPropertyCertificate> getRealPropertySj(String json, String ygCancellcation) {
        List<RealPropertyCertificate> realPropertyCertificateList = new ArrayList<>();
        //判断预告证明号
        if (StringUtils.isNotEmpty(ygCancellcation)) {
            JSONObject jsonObject = (JSONObject) JSONObject.parse(json);
            RealPropertyCertificate realPropertyCertificate = getRealProperty(jsonObject, null);
            realPropertyCertificateList.add(realPropertyCertificate);
            return realPropertyCertificateList;
        }
        //不动产证号和不动产单元号 (返回list)
        JSONArray jsonArray = JSONArray.parseArray(json);
        for (int i = 0; i < jsonArray.size(); i++) {
            realPropertyCertificateList.add(getRealProperty(jsonArray.getJSONObject(i), null));
        }
        return realPropertyCertificateList;
    }


    public String getAutoRealPropertyCertificateTwo(String commonInterfaceAttributer) throws ParseException {
        log.info("转JSON前：" + commonInterfaceAttributer);
        //获取json数据转成收件申请
        SJ_Sjsq sjSjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        RegistrationBureau registrationBureau = BusinessDealBaseUtil.dealBaseInfo(sjSjsq, registrationPid, true, grMortgageRegistration, bankPerson, areaNo);
        registrationBureau = ClAutoRealPropertyCertificate(sjSjsq, registrationBureau);
        String token = backFeign.getToken(new JwtAuthenticationRequest(bsryname,bsrypassword)).getData();
        //做ftp操作
        return exchangeToInnerService.handleCreateFlow(token,sjSjsq,registrationBureau,false);
    }

    private RegistrationBureau ClAutoRealPropertyCertificate(SJ_Sjsq sj_sjsq, RegistrationBureau registrationBureau) {
        Sj_Info_Dyhtxx mortgageContractInfo = sj_sjsq.getMortgageContractInfo();
        //抵押业务信息
        MortgageBizInfo mortgageBizInfo = new MortgageBizInfo();
        mortgageBizInfo.setMortgageApplyDate(mortgageContractInfo.getApplyTime());
        if (StringUtils.isNotBlank(mortgageContractInfo.getRegistrationSubclass())){
            mortgageBizInfo.setRegisterSubType(mortgageContractInfo.getRegistrationSubclass());
        }else{
            mortgageBizInfo.setRegisterSubType("一般抵押权");
        }
        mortgageBizInfo.setMortgageWay(BusinessDealBaseUtil.getModeBySub(mortgageBizInfo.getRegisterSubType()));
        mortgageBizInfo.setMortgageTerm(mortgageContractInfo.getMortgagePeriod());
        if ( null != mortgageContractInfo.getMaximumClaimAmount() ) {
            mortgageBizInfo.setHighestClaimAmount(mortgageContractInfo.getMaximumClaimAmount().toString());
        }
        mortgageBizInfo.setMortgageReason(
                StringUtils.isBlank(mortgageContractInfo.getMortgageReason())?
                        (StringUtils.isBlank(mortgageContractInfo.getRegistrationReason())?"借贷":mortgageContractInfo.getRegistrationReason())
                        :mortgageContractInfo.getMortgageReason()
        );
        mortgageBizInfo.setMortgageStartDate(DateUtils.strToDate(mortgageContractInfo.getMortgageStartingDate()));
        mortgageBizInfo.setMortgageEndDate(DateUtils.strToDate(mortgageContractInfo.getMortgageEndingDate()));
        if (null != mortgageContractInfo.getCreditAmount()) {
            mortgageBizInfo.setCreditAmount(mortgageContractInfo.getCreditAmount().toString());
        }
        //抵押人
        List<DyrGlMortgator> mortgagorInfoVoList = new ArrayList<>();
        if (mortgageContractInfo.getGlMortgagorVoList() != null && mortgageContractInfo.getGlMortgagorVoList().size() != 0) {
            for (SJ_Qlr_Gl dyr : mortgageContractInfo.getGlMortgagorVoList()) {
                DyrGlMortgator dyrGlMortgator = new DyrGlMortgator();
                SJ_Qlr_Info relatedPerson = dyr.getRelatedPerson();
                dyrGlMortgator.setMortgagorName(dyr.getObligeeName());
                dyrGlMortgator.setMortgagorId(relatedPerson.getObligeeDocumentNumber());
                dyrGlMortgator.setMortgagorIdType(getZjlbx(relatedPerson.getObligeeDocumentType()));
                mortgagorInfoVoList.add(dyrGlMortgator);
            }
            mortgageBizInfo.setMortgagorInfoVoList(mortgagorInfoVoList);
        }
        //抵押权人
        List<DyqrGlMortgator> dyqrGlMortgatorList = new ArrayList<>();
        if (mortgageContractInfo.getGlMortgageHolderVoList() != null && mortgageContractInfo.getGlMortgageHolderVoList().size() != 0) {
            for (SJ_Qlr_Gl dyqr : mortgageContractInfo.getGlMortgageHolderVoList()) {
                DyqrGlMortgator dyrGlMortgator = new DyqrGlMortgator();
                SJ_Qlr_Info relatedPerson = dyqr.getRelatedPerson();
                dyrGlMortgator.setMortgageeId(relatedPerson.getObligeeDocumentNumber());
                dyrGlMortgator.setMortgageeIdType(getZjlbx(relatedPerson.getObligeeDocumentType()));
                dyrGlMortgator.setMortgageeName(dyqr.getObligeeName());
                dyqrGlMortgatorList.add(dyrGlMortgator);
            }
            mortgageBizInfo.setMortgageeInfoVoList(dyqrGlMortgatorList);
        }
        //抵押权人委托代理人
        List<WtdlrGlMortgator> dyqrWtdlrList=new ArrayList<>();
        if (mortgageContractInfo.getGlMortgageeAgentInfoVoList() != null && mortgageContractInfo.getGlMortgageeAgentInfoVoList().size() != 0) {
            for (SJ_Qlr_Gl dyqr : mortgageContractInfo.getGlMortgageeAgentInfoVoList()) {
                WtdlrGlMortgator dyqrWtdlr = new WtdlrGlMortgator();
                SJ_Qlr_Info relatedPerson = dyqr.getRelatedPerson();
                dyqrWtdlr.setAgentId(relatedPerson.getObligeeDocumentNumber());
                dyqrWtdlr.setAgentIdType(getZjlbx(relatedPerson.getObligeeDocumentType()));
                dyqrWtdlr.setAgentName(dyqr.getObligeeName());
                dyqrWtdlrList.add(dyqrWtdlr);
            }
            mortgageBizInfo.setMortgageeAgentInfoVoList(dyqrWtdlrList);
        }
        //抵押债务人信息
        List<DyzwrGlMortgator> obligorInfoVoList=new ArrayList<>();
        if (mortgageContractInfo.getGlObligorInfoVoList() != null && mortgageContractInfo.getGlObligorInfoVoList().size() != 0) {
            for (SJ_Qlr_Gl zwr : mortgageContractInfo.getGlObligorInfoVoList()) {
                DyzwrGlMortgator dyzwrGlMortgator = new DyzwrGlMortgator();
                SJ_Qlr_Info relatedPerson = zwr.getRelatedPerson();
                dyzwrGlMortgator.setObligorId(relatedPerson.getObligeeDocumentNumber());
                dyzwrGlMortgator.setObligorIdType(getZjlbx(relatedPerson.getObligeeDocumentType()));
                dyzwrGlMortgator.setObligorName(relatedPerson.getObligeeName());
                obligorInfoVoList.add(dyzwrGlMortgator);
            }
            mortgageBizInfo.setObligorInfoVoList(obligorInfoVoList);
        }

        //抵押人委托代理人
        List<WtdlrGlMortgator> dyrWtdlrList=new ArrayList<>();
        if (mortgageContractInfo.getGlMortgagorAgentInfoVoList() != null && mortgageContractInfo.getGlMortgagorAgentInfoVoList().size() != 0) {
            for (SJ_Qlr_Gl dyqr : mortgageContractInfo.getGlMortgagorAgentInfoVoList()) {
                WtdlrGlMortgator dyrWtdlr = new WtdlrGlMortgator();
                SJ_Qlr_Info relatedPerson = dyqr.getRelatedPerson();
                dyrWtdlr.setAgentId(relatedPerson.getObligeeDocumentNumber());
                dyrWtdlr.setAgentIdType(getZjlbx(relatedPerson.getObligeeDocumentType()));
                dyrWtdlr.setAgentName(dyqr.getObligeeName());
                dyrWtdlrList.add(dyrWtdlr);
            }
            mortgageBizInfo.setMortgagorAgentInfoVoList(dyrWtdlrList);
        }

        //待抵押不动产信息列表
        List<RealEstateInfo> realEstateInfoList = new ArrayList<>();
        if (sj_sjsq.getImmovableRightInfoVoList() != null && sj_sjsq.getImmovableRightInfoVoList().size() != 0) {
            for (SJ_Info_Bdcqlxgxx bdcqlxgxx : sj_sjsq.getImmovableRightInfoVoList()) {
                //房屋
                RealEstateInfo realEstateInfo = new RealEstateInfo();
                if (bdcqlxgxx.getCertificateType().equals(Msgagger.FCZ) || bdcqlxgxx.getCertificateType().equals(Msgagger.BDCQZ)) {
                    realEstateInfo.setRealEstateId(bdcqlxgxx.getImmovableCertificateNo());
                    List<RealEstateUnitInfo> realEstateUnitInfoList = new ArrayList<>();
                    //不动产单元信息列表
                    if (bdcqlxgxx.getGlImmovableVoList() != null && bdcqlxgxx.getGlImmovableVoList().size() != 0) {
                        for (SJ_Bdc_Gl bdc_gl : bdcqlxgxx.getGlImmovableVoList()) {
                            if (bdc_gl.getImmovableType().equals(Msgagger.FANGDI)) {
                                RealEstateUnitInfo realEstateUnitInfo = new RealEstateUnitInfo();
                                realEstateUnitInfo.setHouseholdId(bdc_gl.getFwInfo().getHouseholdId());
                                realEstateUnitInfo.setRealEstateUnitId(bdc_gl.getFwInfo().getImmovableUnitNumber());
                                log.info("不动产单元号" + realEstateUnitInfo.getRealEstateUnitId());
                                realEstateUnitInfo.setSit(bdc_gl.getFwInfo().getHouseLocation());
                                realEstateUnitInfoList.add(realEstateUnitInfo);
                            }
                        }
                    }
                    realEstateInfo.setRealEstateUnitInfoVoList(realEstateUnitInfoList);
//                    realEstateInfo.setObligeeInfoVoList(ClQlr(bdcqlxgxx));
                    realEstateInfoList.add(realEstateInfo);
                } else if (bdcqlxgxx.getCertificateType().equals(Msgagger.TDZ)){
//                realEstateInfo.set
                    //不动产权证号
                        RealEstateInfo tdRealEstateInfo=new RealEstateInfo();
                        tdRealEstateInfo.setLandCertificate(bdcqlxgxx.getImmovableCertificateNo());
                        realEstateInfoList.add(tdRealEstateInfo);
                }
            }
        }
        String landCertificateInfo="";
        if (realEstateInfoList.size()>1){
            Iterator<RealEstateInfo> iterator = realEstateInfoList.iterator();
            while (iterator.hasNext()) {
                RealEstateInfo realEstateInfo = iterator.next();
                    //判断是否为
                    if (null != realEstateInfo.getLandCertificate()) {
                        landCertificateInfo = realEstateInfo.getLandCertificate();
                        iterator.remove();
                    }
                }
            realEstateInfoList.get(0).setLandCertificate(landCertificateInfo);
            }
        mortgageBizInfo.setRealEstateInfoVoList(realEstateInfoList);
        registrationBureau.setMortgageBizInfo(mortgageBizInfo);
        return registrationBureau;
    }

    private List<QlrGlMortgator> ClQlr(SJ_Info_Bdcqlxgxx bdcqlxgxx) {
        List<QlrGlMortgator> qlrGlMortgators = new ArrayList<>();
        if (bdcqlxgxx.getGlObligeeVoList() != null && bdcqlxgxx.getGlObligeeVoList().size() != 0) {
            for (SJ_Qlr_Gl qlrGl : bdcqlxgxx.getGlObligeeVoList()) {
                QlrGlMortgator qlrGlMortgator = new QlrGlMortgator();
                qlrGlMortgator.setObligeeName(qlrGl.getObligeeName());
                SJ_Qlr_Info relatedPerson = qlrGl.getRelatedPerson();
                qlrGlMortgator.setObligeeId(relatedPerson.getObligeeDocumentNumber());
                qlrGlMortgator.setObligeeIdType(relatedPerson.getObligeeDocumentType());
                qlrGlMortgators.add(qlrGlMortgator);
            }
        }
        return qlrGlMortgators;
    }

    /**
     * 不动产权属信息查询(不动产单元号,不动产证号)
     *
     * @param parametricData
     * @return
     * @throws IOException
     */
    public ObjectRestResponse getRealPropertyCertificate(ParametricData parametricData) throws Exception {
        String json = "";
        if (StringUtils.isNotEmpty(parametricData.getBdcdyh()) && StringUtils.isNotEmpty(parametricData.getQlrmc())){
            json = httpClientUtils.paramGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetBdcInfoByBDCDYH"+"?BDCDYH"+parametricData.getBdcdyh()+"&obligeeName"+parametricData.getQlrmc());
        }else if (StringUtils.isNotEmpty(parametricData.getBdcdyh())){
            json = httpClientUtils.paramGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetBdcInfoByBDCDYH"+"?BDCDYH"+parametricData.getBdcdyh());
        } else if (StringUtils.isNotEmpty(parametricData.getBdczh()) && StringUtils.isNotEmpty(parametricData.getQlrmc())){
            json = httpClientUtils.paramGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetBdcInfoByBDCZH"+ "?BDCZH=" + parametricData.getBdczh()+"&obligeeName="+parametricData.getQlrmc());
        }else if (StringUtils.isNotEmpty(parametricData.getBdczh())){
            json = httpClientUtils.paramGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetBdcInfoByBDCZH"+ "?BDCZH=" + parametricData.getBdczh());
        }
        ObjectRestResponse resultRV = new ObjectRestResponse();
        return resultRV.data(getRealPropertySj(json, null));
    }


    /**
     * 抵押证明号获取
     *
     * @param dyzmh
     * @return
     * @throws IOException
     */
    public ObjectRestResponse getRealEstateMortgage(String dyzmh, String mortgagorName, boolean containHistroy) throws Exception {
        ObjectRestResponse resultRV = new ObjectRestResponse();
        String json = "";
        if (StringUtils.isNotEmpty(mortgagorName) && StringUtils.isNotEmpty(dyzmh)) {
           json="http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetBdcInfoByDYZMH"+"?mortgagorName="+mortgagorName+"&DYZMH="+dyzmh+"&containHistory="+containHistroy;
        }
        if (StringUtils.isNotEmpty(dyzmh) && StringUtils.isEmpty(mortgagorName)) {
            json="http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetBdcInfoByDYZMH"+"?DYZMH="+dyzmh+"&containHistory="+containHistroy;
        }
        json=httpClientUtils.paramGet(json);
        log.info("抵押证明号查询数据返回:"+json);
        JSONArray jsonArray = JSONArray.parseArray(json);
        List<MortgageService> mortgageServiceList = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray mortgageInfojsonArray = (JSONArray) jsonObject.get("mortgageInfoVoList");
                MortgageService mortgageService = new MortgageService();
                for (int a = 0; a < mortgageInfojsonArray.size(); a++) {
                    //抵押信息
                    JSONObject mortgageInfo = mortgageInfojsonArray.getJSONObject(a);
                    mortgageService.setImmovableCertificateNo(jsonObject.getString("realEstateId"));
                    mortgageService.setAcceptanceNumber(mortgageInfo.getString("dySLBH"));
                    if (null == mortgageInfo.getString("mortgageWay") ){ //没有的话取小类信息
                        mortgageService.setMortgageMode(mortgageInfo.getString("mortgageType"));
                    }else {
                        mortgageService.setMortgageMode(mortgageInfo.getString("mortgageWay"));
                    }
                    mortgageService.setMortgageCertificateNo(mortgageInfo.getString("warrantId"));//抵押证明号
                    if (null != mortgageInfo.getDouble("creditAmount")){
                        mortgageService.setCreditAmount(mortgageInfo.getDouble("creditAmount"));//债权数额
                        mortgageService.setMortgageAmount(mortgageInfo.getDouble("creditAmount"));//抵押金额
                    }
                    mortgageService.setMortgageArea(mortgageInfo.getDouble("mortgageArea"));//抵押面积
                    mortgageService.setMortgageStartingDate(mortgageInfo.getDate("mortgageStartDate"));//权利开始时间
                    mortgageService.setMortgageEndingDate(mortgageInfo.getDate("mortgageEndDate"));//权利结束时间
                    mortgageService.setMortgageReason(mortgageInfo.getString("mortgageReason"));//抵押原因
                    mortgageService.setValuationValue(mortgageInfo.getDouble("evaluationValue"));//评估价值
                    mortgageService.setRegistrationDate(mortgageInfo.getDate("registerDate"));
                    mortgageService.setDataComeFromMode("接口获取");
                    //抵押权利人
                    JSONArray GlMortgagorjsonArray = (JSONArray) mortgageInfo.get("mortgagorInfoVoList");//
                    if (null != GlMortgagorjsonArray && GlMortgagorjsonArray.size() != 0) {
                        for (int j = 0; j < GlMortgagorjsonArray.size(); j++) {
                            JSONObject glMortgagorObject = GlMortgagorjsonArray.getJSONObject(j);
                            GlMortgagor glMortgagor = getGlMortgage(glMortgagorObject,obligeeDyr);
                            mortgageService.getGlMortgagorVoList().add(glMortgagor);
                        }
                    }
                    //抵押人
                    JSONArray GlMortgageHolderjsonArray = (JSONArray) mortgageInfo.get("mortgageeInfoVoList");
                    if (null != GlMortgageHolderjsonArray && GlMortgageHolderjsonArray.size() != 0) {
                        for (int z = 0; z < GlMortgageHolderjsonArray.size(); z++) {
                            JSONObject glMortgageHolderObject = GlMortgageHolderjsonArray.getJSONObject(z);
                            GlMortgageHolder glMortgageHolder = getGlMortgageHolder(glMortgageHolderObject, obligeeDyqr);
                            mortgageService.getGlMortgageHolderVoList().add(glMortgageHolder);
                        }
                    }
                    //债务人
                    JSONArray ObligorInfoVojsonArray = (JSONArray) mortgageInfo.get("obligorInfoVoList");
                    if (null != ObligorInfoVojsonArray && ObligorInfoVojsonArray.size() != 0) {
                        for (int x = 0; x < ObligorInfoVojsonArray.size(); x++) {
                            JSONObject obligorInfoVojsonArrayJSONObject = ObligorInfoVojsonArray.getJSONObject(x);
                            ObligorInfoVo obligorInfoVo = getObligorInfoVo(obligorInfoVojsonArrayJSONObject, obligeeZwr);
                            mortgageService.getGlObligorInfoVoList().add(obligorInfoVo);
                        }
                    }
                }
                JSONArray GlImmovablejsonArray = (JSONArray) jsonObject.get("realEstateUnitInfoVoList");
                if (null != GlImmovablejsonArray) {
                    for (int j = 0; j < GlImmovablejsonArray.size(); j++) {
                        //房屋信息
                        JSONObject glImmovableObject = GlImmovablejsonArray.getJSONObject(j);
                        GlImmovable glImmovable = new GlImmovable();
                        glImmovable.setImmovableType(Msgagger.FANGDI);
                        FwInfo fwInfo = getFwInfo(glImmovableObject, jsonObject, "是");
                        glImmovable.setFwInfo(fwInfo);
                        mortgageService.getGlImmovableVoList().add(glImmovable);
                    }
                }
                //宗地信息
                JSONArray zdInfojsonArray = (JSONArray) jsonObject.get("landUnitInfoVoList");
                if (null != zdInfojsonArray) {
                    for (int z = 0; z < zdInfojsonArray.size(); z++) {
                        JSONObject zdObject = zdInfojsonArray.getJSONObject(z);
                        GlImmovable glImmovable = getZdInfo(zdObject, jsonObject,null);
                        mortgageService.getGlImmovableVoList().add(glImmovable);
                    }
                }
                mortgageServiceList.add(mortgageService);
            }
        }
        return resultRV.data(mortgageServiceList);
    }


    public String getZjlb(String zjlx) {
        String zjlb = "";
        switch (zjlx) {
            case "1":
                zjlb = "身份证";
                break;
            case "8":
                zjlb = "00000000000000000000";
                break;
            default:
                zjlb = "其它证件";
                break;
        }
        return zjlb;
    }

    public String getZjlbx(String zjlx) {
        String zjlb = "";
        switch (zjlx) {
            case "身份证":
                zjlb = "1";
                break;
            case "统一社会信用代码":
                zjlb = "8";
                break;
            case "港澳台身份证":
                zjlb = "2";
                break;
            case "护照":
                zjlb = "3";
                break;
            case "户口簿":
                zjlb = "4";
                break;
            case "军官证(士兵证)":
                zjlb = "5";
                break;
            case "组织机构代码":
                zjlb = "6";
                break;
            case "营业执照":
                zjlb = "7";
                break;
            case "其他":
                zjlb = "99";
                break;
        }
        return zjlb;
    }



    private FwInfo getFwInfo(JSONObject glImmovableObject, JSONObject jsonObject, String dyqk) {
        FwInfo fwInfo = new FwInfo();
        fwInfo.setHouseholdId(glImmovableObject.getString("householdId"));
        fwInfo.setHouseLocation(glImmovableObject.getString("sit"));//坐落
        fwInfo.setHouseholdMark(glImmovableObject.getString("accountId"));//户号
        fwInfo.setRoomMark(glImmovableObject.getString("roomId"));//房间号
        fwInfo.setUnitMark(glImmovableObject.getString("unitId"));//单元号
        fwInfo.setProjectName(glImmovableObject.getString("projectName"));//项目名称
        if (glImmovableObject.getString("architectureArea") != null){
            fwInfo.setArchitecturalArea(new BigDecimal(glImmovableObject.getString("architectureArea")));//建筑面积
        }
        if (glImmovableObject.getString("innerArchitectureArea")!=null){
            fwInfo.setHouseArchitecturalArea(new BigDecimal(glImmovableObject.getString("innerArchitectureArea")));////套内建筑面积
        }
        if (glImmovableObject.getString("sharedArchitectureArea") != null){
            fwInfo.setApportionmentArchitecturalArea(new BigDecimal(glImmovableObject.getString("sharedArchitectureArea")));//分摊建筑面积
        }
        fwInfo.setImmovableUnitNumber(glImmovableObject.getString("realEstateUnitId"));//不动产单元号
        fwInfo.setSeatNumber(glImmovableObject.getString("buildingId"));//幢编号
        fwInfo.setMortgageSituation(dyqk);//不动产抵押情况
        JSONArray attachmentInfoVoList = jsonObject.getJSONArray("attachmentInfoVoList");
        if (attachmentInfoVoList == null || attachmentInfoVoList.size() == 0) {
            fwInfo.setClosureSituation("无");
        } else {
            fwInfo.setClosureSituation("是");
        }
        JSONArray dissentInfoVoList = jsonObject.getJSONArray("dissentInfoVoList");
        if (dissentInfoVoList == null || dissentInfoVoList.size() == 0) {
            fwInfo.setObjectionSituation("无");
        } else {
            fwInfo.setObjectionSituation("是");
        }
        return fwInfo;
    }

    private GlImmovable getZdInfo(JSONObject zdObject, JSONObject jsonObject,RealPropertyCertificate realPropertyCertificate) {
        GlImmovable glImmovable = new GlImmovable();
        glImmovable.setImmovableType(Msgagger.ZONGDI);
//        glImmovable.setImmovableId(zdObject.getString("landId"));
        ZdInfo zdInfo = new ZdInfo();
        zdInfo.setParcelType(zdObject.getString("landType"));//宗地类型
        zdInfo.setParcelUnicode(zdObject.getString("landUnicode"));//宗地统一编码
        zdInfo.setImmovableUnitNumber(zdObject.getString("realEstateUnitId"));//不动产单元号
        zdInfo.setCadastralNumber(zdObject.getString("cadastralNumber"));//地籍号
        zdInfo.setParcelLocation(zdObject.getString("landSit"));//土地坐落
        zdInfo.setLandUse(zdObject.getString("landUsage"));//土地用途
        zdInfo.setPrivateLandArea(zdObject.getBigDecimal("singleLandArea"));//独用土地面积
        zdInfo.setApportionmentLandArea(zdObject.getBigDecimal("sharedLandArea"));//分摊土地面积
        if (zdObject.getString("landRightEndDate") != null) {
            if (null != realPropertyCertificate) {
                realPropertyCertificate.setLandUseRightEndingDate(zdObject.getString("landRightEndDate"));
            }
        }
        JSONArray attachmentInfoVoList = jsonObject.getJSONArray("attachmentInfoVoList");//查封的不动产单元号
        if (attachmentInfoVoList == null || attachmentInfoVoList.size() == 0) {
            zdInfo.setClosureSituation("无");
        } else {
            zdInfo.setClosureSituation("是");
        }
        JSONArray dissentInfoVoList = jsonObject.getJSONArray("dissentInfoVoList");//存在异议的不动产单元号
        if (dissentInfoVoList == null || dissentInfoVoList.size() == 0) {
            zdInfo.setObjectionSituation("无");
        } else {
            zdInfo.setObjectionSituation("是");
        }
        glImmovable.setZdInfo(zdInfo);
        return glImmovable;
    }


    private GlMortgagor getGlMortgage(JSONObject glMortgageHolderObject, String obligeeType) {
        GlMortgagor glMortgagor = new GlMortgagor();
        glMortgagor.setObligeeName(glMortgageHolderObject.getString("mortgagorName"));
        glMortgagor.setObligeeType(obligeeType);
        RelatedPerson relatedPerson = new RelatedPerson();
        System.out.print(glMortgageHolderObject.getString("mortgagorIdType"));
        relatedPerson.setObligeeDocumentType(glMortgageHolderObject.getString("mortgagorIdType"));
        relatedPerson.setObligeeName(glMortgageHolderObject.getString("mortgagorName"));
        relatedPerson.setObligeeDocumentNumber(glMortgageHolderObject.getString("mortgagorId"));
        glMortgagor.setRelatedPerson(relatedPerson);
        return glMortgagor;
    }

    private GlMortgagor getGlMortgageQlr(JSONObject glMortgageHolderObject) {
        GlMortgagor glMortgagor = new GlMortgagor();
        glMortgagor.setObligeeName(glMortgageHolderObject.getString("obligeeName"));
        glMortgagor.setObligeeType(obligeeQlr);
        RelatedPerson relatedPerson = new RelatedPerson();
        System.out.print(glMortgageHolderObject.getString("obligeeIdType"));
        if (glMortgageHolderObject.getString("obligeeIdType") !=null) {
            relatedPerson.setObligeeDocumentType(glMortgageHolderObject.getString("obligeeIdType"));
        }
        relatedPerson.setObligeeName(glMortgageHolderObject.getString("obligeeName"));
        relatedPerson.setObligeeDocumentNumber(glMortgageHolderObject.getString("obligeeId"));
        glMortgagor.setRelatedPerson(relatedPerson);
        return glMortgagor;
    }

    private GlMortgagor getGlMortgageYwr(JSONObject glMortgageHolderObject) {
        GlMortgagor glMortgagor = new GlMortgagor();
        glMortgagor.setObligeeName(glMortgageHolderObject.getString("salerName"));
        glMortgagor.setObligeeType(obligeeYwr);
        RelatedPerson relatedPerson = new RelatedPerson();
        if (glMortgageHolderObject.getString("salerIdType") != null) {
            relatedPerson.setObligeeDocumentType(glMortgageHolderObject.getString("salerIdType"));
        }
        relatedPerson.setObligeeName(glMortgageHolderObject.getString("salerName"));
        relatedPerson.setObligeeDocumentNumber(glMortgageHolderObject.getString("salerId"));
        glMortgagor.setRelatedPerson(relatedPerson);
        return glMortgagor;
    }


    private GlMortgageHolder getGlMortgageHolder(JSONObject glMortgageHolderObject, String obligeeType) {
        GlMortgageHolder glMortgageHolder = new GlMortgageHolder();
        glMortgageHolder.setObligeeName(glMortgageHolderObject.getString("mortgageeName"));
        glMortgageHolder.setObligeeType(obligeeType);
        RelatedPerson relatedPerson = new RelatedPerson();
        System.out.print(glMortgageHolderObject.getString("mortgageeIdType"));
        relatedPerson.setObligeeDocumentType(glMortgageHolderObject.getString("mortgageeIdType"));
        relatedPerson.setObligeeName(glMortgageHolderObject.getString("mortgageeName"));
        relatedPerson.setObligeeDocumentNumber(glMortgageHolderObject.getString("mortgageeId"));
        glMortgageHolder.setRelatedPerson(relatedPerson);
        return glMortgageHolder;
    }

    private ObligorInfoVo getObligorInfoVo(JSONObject jsonObject, String obligeeType) {
        ObligorInfoVo obligorInfoVo = new ObligorInfoVo();
        obligorInfoVo.setObligeeName(jsonObject.getString("obligorName"));
        obligorInfoVo.setObligeeType(obligeeType);
        RelatedPerson relatedPerson = new RelatedPerson();
        relatedPerson.setObligeeDocumentType(jsonObject.getString("obligorIdType"));
        relatedPerson.setObligeeName(jsonObject.getString("obligorName"));
        relatedPerson.setObligeeDocumentNumber(jsonObject.getString("obligorId"));
        obligorInfoVo.setRelatedPerson(relatedPerson);
        return obligorInfoVo;
    }



}
