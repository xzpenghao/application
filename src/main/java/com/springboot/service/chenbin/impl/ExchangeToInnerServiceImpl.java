package com.springboot.service.chenbin.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.HttpCallComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.popj.pub_data.Sj_Info_Dyhtxx;
import com.springboot.popj.pub_data.Sj_Info_Jyhtxx;
import com.springboot.popj.registration.MortgageBizInfo;
import com.springboot.popj.registration.RegistrationBureau;
import com.springboot.service.chenbin.ExchangeToInnerService;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import com.springboot.util.HttpClientUtils;
import com.springboot.util.SysPubDataDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("exchangeToInnerService")
public class ExchangeToInnerServiceImpl implements ExchangeToInnerService {

    @Autowired
    private HttpCallComponent httpCallComponent;

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
    @Override
    public String dealYGYD2Inner(String commonInterfaceAttributer) throws ParseException {
        String result = "处理成功";
        System.out.println("转JSON前："+commonInterfaceAttributer);
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer,null,null,null);
        System.out.println("转JSON后："+JSONObject.toJSONString(sjsq));
        Sj_Info_Jyhtxx jyht = sjsq.getTransactionContractInfo();
        Sj_Info_Dyhtxx dyht = sjsq.getMortgageContractInfo();
        if(jyht==null){
            throw new ZtgeoBizException("预告预抵业务转内网办件时异常，交易合同信息为空");
        }
        if(dyht==null){
            throw new ZtgeoBizException("预告预抵业务转内网办件时异常，交易合同信息为空");
        }
        RegistrationBureau registrationBureau= BusinessDealBaseUtil.dealBaseInfo(sjsq,pid,isSubmit,bizType,dealPerson,areaNo);
        MortgageBizInfo mortgageBizInfo = BusinessDealBaseUtil.getMortgageBizInfoByContract(jyht,dyht,idType);
        registrationBureau.setMortgageBizInfo(mortgageBizInfo);

//        System.out.println("传入："+JSONObject.toJSONString(registrationBureau));

        String token = httpCallComponent.getToken(username,password);
        //操作FTP上传附件
//        List<SJ_Fjfile> fileVoList = httpCallComponent.getFileVoList(sjsq.getReceiptNumber(),token);

        JSONObject resultObject= httpCallComponent.callRegistrationBureauForRegister(registrationBureau);
        if(resultObject!=null){
            if(!(boolean) resultObject.get("success")){
                log.error("登记局受理失败,原因:"+resultObject.getString("message"));
                throw new ZtgeoBizException("登记局受理失败,原因:"+resultObject.getString("message"));
            }
        } else {
            throw new ZtgeoBizException("内网接口请求异常，返回数据为空，错误无法判定");
        }
        Map<String,String> mapParmeter=new HashMap<>();
        mapParmeter.put("receiptNumber",sjsq.getReceiptNumber());
        mapParmeter.put("registerNumber",resultObject.getString("slbh"));
        //返回数据到一窗受理平台保存受理编号和登记编号
        String resultJson = httpCallComponent.preservationRegistryData(mapParmeter,token);
        ObjectRestResponse<Object> resultRV = httpCallComponent.adaptationPreservationReturn(resultJson);
        if(resultRV.getStatus()==200){
            result = (String)resultRV.getData();
        }else{
            log.error((String)resultRV.getData());
            throw new ZtgeoBizException((String)resultRV.getData());
        }
        return result;
    }
}
