package com.springboot.service.chenbin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.component.chenbin.ExchangeToInnerComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.constant.penghao.BizOrBizExceptionConstant;
import com.springboot.entity.chenbin.personnel.other.web.*;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.service.chenbin.ExchangeToWebService;
import com.springboot.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Service("exc2Web")
public class ExchangeToWebServiceImpl implements ExchangeToWebService {

    @Value("${djj.bsryname}")
    private String username;
    @Value("${djj.bsrypassword}")
    private String password;
    @Value("${chenbin.registrationBureau.secRec.transfer.wholeMoney.mid}")
    private String mouldId;

    @Autowired
    private OuterBackFeign outerBackFeign;

    @Autowired
    private ExchangeToInnerComponent exchangeToInnerComponent;

    @Override
    public Object initWebSecReg(RequestParamBody paramBody) {
        Map<String,String> mapParmeter= new HashMap<>();
        String returnSlbh = paramBody.getReturnSlbh();
        RequestParamSjsq sjsq_req = paramBody.getRecEntity();
        System.out.println(JSONObject.toJSONString(sjsq_req));
        List<ImmovableFile> fileVoList = paramBody.getFileVoList();

        SJ_Sjsq sjsq = getSjsqFromBaseParams(sjsq_req);
        List<String> bdcDatas = sjsq_req.getBdcDatas();

        List<RespServiceData> serviceDatas = new ArrayList<RespServiceData>();
        //不动产权属服务数据
        RespServiceData serviceData1 = new RespServiceData();
        List<SJ_Info_Bdcqlxgxx> bdcqls_final = new ArrayList<SJ_Info_Bdcqlxgxx>();
        if(bdcDatas!=null && bdcDatas.size()>0){
            for(String bdcData:bdcDatas){
                ParametricData parametricData = new ParametricData();
                parametricData.setBdczh(bdcData);
                List<SJ_Info_Bdcqlxgxx> bdcqls_temp = exchangeToInnerComponent.getBdcQlInfoWithItsRights(parametricData);
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

        //交易服务数据组织
        RespServiceData serviceData2 = new RespServiceData();
        RequestParamJyht htdata = sjsq_req.getHtdata();
        List<Sj_Info_Jyhtxx> jyhtxxes = new ArrayList<Sj_Info_Jyhtxx>();
        Sj_Info_Jyhtxx jyhtxx = new Sj_Info_Jyhtxx();
        if(htdata!=null){//交易信息
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
            jyhtxx.setPaymentMethod(htdata.getPaymentMethod());
            jyhtxx.setTaxBurdenParty(htdata.getTaxBurdenParty());
            jyhtxx.setDeliveryDays(htdata.getDeliveryDays());
            if(StringUtils.isNotBlank(htdata.getDeliveryDate())) {
                try {
                    jyhtxx.setDeliveryDate(TimeUtil.getDateFromString(htdata.getDeliveryDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new ZtgeoBizException("传入的交付日期格式不正确");
                }
            }
            jyhtxx.setHtDetail(htdata.getHtDetail());
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

        mapParmeter.put("returnSlbh",returnSlbh);
        if(fileVoList!=null) {
            //mapParmeter.put("fileVoList", JSONArray.toJSONString(fileVoList));
        }
        mapParmeter.put("modelId",mouldId);
        mapParmeter.put("subControl","1");
        mapParmeter.put("SJ_Sjsq", JSONObject.toJSONString(sjsq));
        String token = outerBackFeign.getToken(new JwtAuthenticationRequest(username, password)).getData();
        return outerBackFeign.DealRecieveFromOuter5(token,mapParmeter).getData();
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
                sjQlrGl.setSharedValue(StringUtils.isNotBlank(man.getSharedValue())?Integer.parseInt(man.getSharedValue()):null);
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
}
