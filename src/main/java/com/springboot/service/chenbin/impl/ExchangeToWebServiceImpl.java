package com.springboot.service.chenbin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.ExchangeToInnerComponent;
import com.springboot.component.chenbin.file.ToFTPUploadComponent;
import com.springboot.component.newPlat.FileInteractComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.constant.penghao.BizOrBizExceptionConstant;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.chenbin.personnel.other.web.*;
import com.springboot.entity.chenbin.personnel.pub_use.SJ_Sjsq_User_Ext;
import com.springboot.entity.newPlat.settingTerm.FtpSettings;
import com.springboot.entity.newPlat.settingTerm.NewPlatSettings;
import com.springboot.entity.newPlat.transInner.req.fromZY.domain.TwoOrNFjxx;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.service.chenbin.ExchangeToWebService;
import com.springboot.service.newPlat.chenbin.BdcQueryService;
import com.springboot.util.DateUtils;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.ErrorDealUtil;
import com.springboot.util.newPlatBizUtil.ParamConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import static com.springboot.constant.chenbin.BusinessConstant.BDC_NEW_PLAT_FLOW_KEY_ZY;
import static com.springboot.constant.chenbin.BusinessConstant.NEWPLAT_TURNINNERS_ESFZY;

@Slf4j
@Service("exc2Web")
public class ExchangeToWebServiceImpl implements ExchangeToWebService {

    @Value("${djj.bsryname}")
    private String username;
    @Value("${djj.bsrypassword}")
    private String password;
    @Autowired
    private NewPlatSettings newPlatSettings;
    @Autowired
    private FtpSettings ftpSettings;

    @Autowired
    private OuterBackFeign outerBackFeign;

    @Autowired
    private BdcQueryService bdcQueryService;

    @Autowired
    private FileInteractComponent fileInteractComponent;


    @Override
    public Object initWebSecReg(RequestParamBody paramBody) {
        try {
            return dealYcslCreateProcess(initData(paramBody));
        } catch (ParseException e) {
            log.error("JSON转译异常，原始异常信息："+ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("JSON转译异常");
        }
    }

    @Override
    public Object initWebSecRegForNewPlat(RequestParamBody paramBody) {
        try {
            return dealYcslCreateProcess(initData(paramBody));
        } catch (ParseException e) {
            log.error("JSON转译异常，原始异常信息："+ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("JSON转译异常");
        }
    }

    public Map<String,String> initData(RequestParamBody paramBody) throws ParseException {
        log.info("接收互联网加申请");
        //数据自检
        paramBody.checkSelfStandard();
        //声明交互map
        Map<String,String> mapParmeter= new HashMap<>();
        //取关键数据
        String returnSlbh = paramBody.getReturnSlbh();
        RequestParamSjsq sjsq_req = paramBody.getRecEntity();
        //关键数据自检
        sjsq_req.checkSelfStandard();

        System.out.println("申请收件信息详情："+JSONObject.toJSONString(sjsq_req));
        List<ImmovableFile> fileVoList = paramBody.getFileVoList();

        SJ_Sjsq sjsq = getSjsqFromBaseParams(sjsq_req);
        List<String> bdcDatas = sjsq_req.getBdcDatas();
        log.info("本次办件使用的权证号为："+JSONArray.toJSONString(bdcDatas));

        List<RespServiceData> serviceDatas = new ArrayList<RespServiceData>();

        /**
         * 不动产权属服务数据
         */
        RespServiceData serviceData1 = new RespServiceData();
        List<SJ_Info_Bdcqlxgxx> bdcqls_final = new ArrayList<SJ_Info_Bdcqlxgxx>();
        if(bdcDatas!=null && bdcDatas.size()>0){
            for(String bdcData:bdcDatas){
                ParametricData parametricData = new ParametricData();
                parametricData.setBdczh(bdcData);
                //使用新的权属数据获取
                List<SJ_Info_Bdcqlxgxx> bdcqls_temp = bdcQueryService.queryQzxxWithItsRights(parametricData);
                for(SJ_Info_Bdcqlxgxx bdcql_temp : bdcqls_temp){
                    bdcqls_final.add(bdcql_temp);
                }
            }
        }else{
            throw new ZtgeoBizException("传入不动产权证集合为空，检查传入数据");
        }
        if(bdcqls_final.size()<1){
            throw new ZtgeoBizException("未能查询到传入的不动产权证信息，检查传入不动产证号是否有误");
        }
        serviceData1.setServiceCode("OwnershipCertificateServiceWithItsRight");
        serviceData1.setServiceDataInfos(bdcqls_final);
        serviceDatas.add(serviceData1);
        //坐落信息补全
        if(StringUtils.isBlank(sjsq.getImmovableSite())){
            sjsq.setImmovableSite(bdcqls_final.get(0).getImmovableSite());
        }

        /**
         * 交易服务数据组织
         */
        RespServiceData serviceData2 = new RespServiceData();
        RequestParamJyht htdata = sjsq_req.getHtdata();
        List<Sj_Info_Jyhtxx> jyhtxxes = new ArrayList<Sj_Info_Jyhtxx>();
        Sj_Info_Jyhtxx jyhtxx = new Sj_Info_Jyhtxx();
        if(htdata!=null){//交易信息
            List<SJ_Bdc_Gl> jyfcgl = JSONArray.parseArray(
                    JSONArray.toJSONString(bdcqls_final.get(0).getGlImmovableVoList()),SJ_Bdc_Gl.class);
            jyhtxx.setGlImmovableVoList(jyfcgl);

            jyhtxx.setRegistrationSubclass(sjsq_req.getRegistrationSubclass());
            jyhtxx.setRegistrationReason(sjsq_req.getRegistrationReason());
            jyhtxx.setContractType(htdata.getContractType());
            String jyje = getNotNullData(htdata.getContractAmount());
            if(StringUtils.isNotBlank(jyje)) {
                jyhtxx.setContractAmount(new BigDecimal(jyje));
            } else {
                throw new ZtgeoBizException("交易信息必填交易金额为空，检查传入数据");
            }
            jyhtxx.setHoldingDifferent(htdata.getHoldingDifferent());
            jyhtxx.setFundTrusteeship(htdata.getFundTrusteeship());
            jyhtxx.setOldHouseCode(htdata.getOldHouseCode());
            jyhtxx.setPaymentMethod(StringUtils.isNotBlank(htdata.getPaymentMethod())?htdata.getPaymentMethod():"1");
            jyhtxx.setTaxBurdenParty(htdata.getTaxBurdenParty());
            jyhtxx.setDeliveryDays(htdata.getDeliveryDays());
            if(StringUtils.isNotBlank(htdata.getDeliveryDays())){
                jyhtxx.setDeliveryMode("天数交付");
            } else {
                if (StringUtils.isNotBlank(htdata.getDeliveryDate())) {
                    try {
                        jyhtxx.setDeliveryDate(TimeUtil.getDateFromString(htdata.getDeliveryDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new ZtgeoBizException("传入的交付日期格式不正确");
                    }
                }
                if (StringUtils.isNotBlank(jyhtxx.getDeliveryDate())) {
                    jyhtxx.setDeliveryMode("日期交付");
                }
            }
            log.info("合同细节数据"+JSONObject.toJSONString(htdata.getHtDetail()));
            if(htdata.getHtDetail()!=null) {
                if(StringUtils.isBlank(htdata.getHtDetail().getDoesIncludeHouseProperties())){//当没有给定附属设施的明确说明自己造
                    htdata.getHtDetail().setDoesIncludeHouseProperties("0");
                }
                jyhtxx.setHtDetail(htdata.getHtDetail());
            }else{
                throw new ZtgeoBizException("请传入正确的合同细节数据");
            }
            List<RequestParamQlr> houseBuyerVoList = htdata.getHouseBuyerVoList();
            List<SJ_Qlr_Gl> buyergls = getQlrFromHtMan(houseBuyerVoList, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_GFZ);
            if(buyergls!=null && buyergls.size()>0) {
                jyhtxx.setGlHouseBuyerVoList(buyergls);
            }else{
                throw new ZtgeoBizException("购房人信息缺失，检查传入数据");
            }
            List<RequestParamQlr> houseSellerVoList = htdata.getHouseSellerVoList();
            List<SJ_Qlr_Gl> sellergls = getQlrFromHtMan(houseSellerVoList, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_SFZ);
            if(sellergls!=null && sellergls.size()>0) {
                jyhtxx.setGlHouseSellerVoList(sellergls);
            }else{
                List<SJ_Qlr_Gl> yqlrgls = bdcqls_final.get(0).getGlObligeeVoList();
                sellergls = copyQlrGl(yqlrgls,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_SFZ);
                jyhtxx.setGlHouseSellerVoList(sellergls);
            }
        } else {
            throw new ZtgeoBizException("传入交易信息为空，检查传入数据");
        }
        jyhtxxes.add(jyhtxx);
        serviceData2.setServiceCode("SecondHouseReceiptService");
        serviceData2.setServiceDataInfos(jyhtxxes);
        serviceDatas.add(serviceData2);
        sjsq.setServiceDatas(serviceDatas);

        /**
         * 处理收件人
         */
        if(StringUtils.isNotBlank(sjsq_req.getReceiverId())) {
            List<SJ_Sjsq_User_Ext> userExtVoList = new ArrayList<>();
            SJ_Sjsq_User_Ext userExt = new SJ_Sjsq_User_Ext();
            userExt.setAdaptSys("01");
            userExt.setDataInitMethod("字典");
            userExt.setUserCode(sjsq_req.getReceiverId());
            userExt.setUserName(sjsq_req.getReceiverName());
            userExtVoList.add(userExt);
            sjsq.setUserExtVoList(userExtVoList);
        }

        /**
         * 异步附件处理
         */
        if(fileVoList!=null) {
            List<SJ_Fjfile> files = dealAsynchFile(fileVoList);
            mapParmeter.put("fileVoList",JSONArray.toJSONString(files));
        }

        /**
         * 收尾数据
         */
        mapParmeter.put("returnSlbh",returnSlbh);
        mapParmeter.put("modelId",newPlatSettings.gainTermByKey(NEWPLAT_TURNINNERS_ESFZY).getMid());
        mapParmeter.put("subControl","0");//0是提交，1是不提交,2是仅创建办件不保存数据
        mapParmeter.put("SJ_Sjsq", JSONObject.toJSONString(sjsq));
        return mapParmeter;
    }

    public Object dealYcslCreateProcess(Map<String,String> mapParmeter){
        String token = outerBackFeign.getToken(new JwtAuthenticationRequest(username, password)).getData();
        ObjectRestResponse<Object> ourv = outerBackFeign.DealRecieveFromOuter5(token,mapParmeter);
        if(ourv.getStatus()!=200){
            throw new ZtgeoBizException("转一窗生成办件失败，返回失败原因："+ourv.getMessage());
        }
        return ourv.getData();
    }

    private SJ_Sjsq getSjsqFromBaseParams(RequestParamSjsq sjsq_req){
        SJ_Sjsq sjsq = new SJ_Sjsq();
        sjsq.setBusinessType(sjsq_req.getBusinessType());
        sjsq.setRegistrationCategory(sjsq_req.getRegistrationCategory());
        sjsq.setRegistrationSubclass(sjsq_req.getRegistrationSubclass());
        sjsq.setRegistrationReason(sjsq_req.getRegistrationReason());
        sjsq.setImmovableType(sjsq_req.getImmovableType());
        sjsq.setImmovableSite(sjsq_req.getImmovableSite());
        sjsq.setNotifiedPersonName(sjsq_req.getNotifiedPersonName());
        sjsq.setNotifiedPersonTelephone(sjsq_req.getNotifiedPersonTelephone());
        sjsq.setNotifiedPersonAddress(sjsq_req.getNotifiedPersonAddress());
        sjsq.setPlatform(sjsq_req.getPlatform());
        sjsq.setDistrictCode(sjsq_req.getDistrictCode());
        return sjsq;
    }

    private String getNotNullData(String param){
        return (StringUtils.isBlank(param) || param.equals("null"))?null:param;
    }

    private List<SJ_Qlr_Gl> getQlrFromHtMan(List<RequestParamQlr> manVoList,String obligeeType){
        List<SJ_Qlr_Gl> sjQlrGls = new ArrayList<SJ_Qlr_Gl>();
        if(manVoList!=null){
            for(RequestParamQlr man:manVoList){
                SJ_Qlr_Gl sjQlrGl = new SJ_Qlr_Gl();
                SJ_Qlr_Info qlrInfo = new SJ_Qlr_Info();
                qlrInfo.setObligeeName(man.getObligeeName());
                qlrInfo.setObligeeDocumentType(man.getObligeeDocumentType());
                qlrInfo.setObligeeDocumentNumber(man.getObligeeDocumentNumber());
                qlrInfo.setDh(man.getDh());
                sjQlrGl.setObligeeName(man.getObligeeName());
                sjQlrGl.setObligeeType(obligeeType);
                sjQlrGl.setObligeeOrder(StringUtils.isNotBlank(man.getObligeeOrder())?Integer.parseInt(man.getObligeeOrder()):null);
                sjQlrGl.setSharedMode(man.getSharedMode());
                sjQlrGl.setSharedValue(StringUtils.isNotBlank(man.getSharedValue())?man.getSharedValue().replaceAll("%",""):null);
                sjQlrGl.setRelatedPerson(qlrInfo);
                sjQlrGls.add(sjQlrGl);
            }
        }
        return sjQlrGls;
    }

    private List<SJ_Qlr_Gl> copyQlrGl(List<SJ_Qlr_Gl> yqlrgls,String qlrlx){
        List<SJ_Qlr_Gl> sellergls = JSONArray.parseArray(JSONArray.toJSONString(yqlrgls),SJ_Qlr_Gl.class);
        if(sellergls!=null) {
            for (SJ_Qlr_Gl sellergl : sellergls) {
                sellergl.setObligeeType(qlrlx);
            }
        }
        return sellergls;
    }

    private List<SJ_Fjfile> dealAsynchFile(List<ImmovableFile> fileVoList){
        List<SJ_Fjfile> files = new ArrayList<>();
        List<TwoOrNFjxx> willAsynFiles = new ArrayList<>();
        for(ImmovableFile f:fileVoList){
            SJ_Fjfile file = new SJ_Fjfile();
            if(f.getFileType().contains(".")){
                f.setFileType(f.getFileType().replaceAll(".",""));
            }
            file.setFileName(f.getFileName().contains(f.getFileType())?f.getFileName():(f.getFileName()+'.'+f.getFileType()));
            file.setFileExt(f.getFileType());
            file.setFileSize(f.getFileSize());
            file.setXh(f.getFileSequence());
            file.setLogicPath(f.getPName());
            file.setSaveType(ftpSettings.getYcSaveSetting());

            String actualFileName = UUID.randomUUID().toString().replaceAll("-","").toUpperCase()
                    +"."+f.getFileType();

            if(CommonSetYcBdcFjdz(file,actualFileName)){
                //设置原路径、去向路径
                TwoOrNFjxx treeFjxx = new TwoOrNFjxx()
                        .initYcAndBdcByBase64(f.getFileBase64(),ftpSettings.getYcSaveSetting(),"1",file);
                willAsynFiles.add(treeFjxx);
            }else{
                //设置原路径、去向路径
                TwoOrNFjxx treeFjxx = new TwoOrNFjxx()
                        .initYcAndBdcByBase64(f.getFileBase64(),ftpSettings.getYcSaveSetting(),null,file);
                willAsynFiles.add(treeFjxx);
            }
            files.add(file);
        }
        if(willAsynFiles.size()>0) { //启动异步传输
            log.info("web+办件附件三方异步处理开始！");
            fileInteractComponent.AsynchTrans(willAsynFiles);
        }
        return files;
    }

    /**
     * 描述：通用一窗和不动产附件地址处理
     * 作者：chenb
     * 日期：2020/8/19
     * 参数：[file, actualFileName]
     * 返回：需要不动产异步上传时返回true；仅上传一窗返回false
     * 更新记录：更新人：{}，更新日期：{}
     */
    public boolean CommonSetYcBdcFjdz(SJ_Fjfile file,String actualFileName){
        //声明是否使用异步上传附件标识
        boolean isUseAsyn4Bdc = ftpSettings.getIsDealFtp().getBdc();
        //设置不操作不动产ftp但yc使用本地存储，仍然设置异步上传不动产FTP
        if(!isUseAsyn4Bdc){
            isUseAsyn4Bdc = "0".equals(ftpSettings.getYcSaveSetting());
        }
        //一窗FTP路径生成
        String ycPath = ParamConvertUtil.initYcslFtpPath(ftpSettings,actualFileName);
        //不动产FTP路径生成
        String bdcMappingPath = "";
        if(isUseAsyn4Bdc) {
            bdcMappingPath = ParamConvertUtil.initNeedFtpPath(
                    "BDC",
                    BDC_NEW_PLAT_FLOW_KEY_ZY,
                    actualFileName,
                    "999",
                    "YCSL-HLW-" + TimeUtil.getDateString(new Date()).replaceAll("-", "")
            );
        }else{
            bdcMappingPath = ycPath;
        }
        file.setFtpPath(ycPath);
        file.setBdcMappingPath(bdcMappingPath);

        return isUseAsyn4Bdc;
    }
}
