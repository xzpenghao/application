package com.springboot.service.chenbin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.tax.TaxParamBody;
import com.springboot.entity.chenbin.personnel.tax.TaxRespBody;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.service.chenbin.ExchangeToTaxService;
import com.springboot.util.SysPubDataDealUtil;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("exc2Tax")
public class ExchangeToTaxServiceImpl implements ExchangeToTaxService {

    @Value("${tax.bsryname}")
    private String username;
    @Value("${tax.bsrypassword}")
    private String password;

    @Autowired
    private ExchangeWithOtherFeign otherFeign;

    @Autowired
    private OuterBackFeign backFeign;

    @Override
    public String deal2Tax(String commonInterfaceAttributer) throws ParseException {
        String result = "处理成功";
        //token
        String token = backFeign.getToken(new JwtAuthenticationRequest(username,password)).getData();
        //处理数据
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        if (sjsq.getTransactionContractInfo() == null) {
            throw new ZtgeoBizException("业务税务系统分发时异常，交易合同信息为空");
        }
        if (sjsq.getImmovableRightInfoVoList() == null) {
            throw new ZtgeoBizException("业务税务系统分发时异常，不动产权利信息为空");
        }
        //整理参数
        TaxParamBody taxParamBody = BusinessDealBaseUtil.dealParamForTax(sjsq);
        System.out.println("进入税务处理，参数转换为:"+JSONObject.toJSONString(taxParamBody));
        //调用Feign,暂时取消
//        ObjectRestResponse<String> rv = otherFeign.testTax(taxParamBody);
//        if(rv.getStatus()==200){
//            result = rv.getData();
            //成功后由税务人员签收办件
            String receiptNumber = sjsq.getReceiptNumber();
            Map<String, String> mapParmeter = new HashMap<>();
            mapParmeter.put("receiptNumber", receiptNumber);
            backFeign.dealRecieveFromOuter1(token,mapParmeter);
//        }else{
//            throw new ZtgeoBizException("税务通知失败，失败原因："+rv.getMessage());
//        }
        System.out.println("税务处理返回，结果为:"+result);
        return result;
    }

    @Override
    public ObjectRestResponse<String> examineSuccess4Tax(TaxRespBody taxRespBody) {
        String result = "";
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        if(taxRespBody==null){
            rv.setStatus(20500);
            rv.setMessage("传入的税务数据为空");
            return rv;
        }
        System.out.println("正在获取操作用户token");
        String token = backFeign.getToken(new JwtAuthenticationRequest(username,password)).getData();

        List<RespServiceData> serviceDatas = new ArrayList<>();
        RespServiceData serviceData = new RespServiceData();

        Map<String, String> mapParmeter = new HashMap<>();
        if(StringUtils.isBlank(taxRespBody.getReceiptNumber())){
            rv.setStatus(20500);
            rv.setMessage("税务数据与一窗受理相关联的收件编号为空");
            return rv;
        }
        mapParmeter.put("receiptNumber",taxRespBody.getReceiptNumber());

        List<Sj_Info_Qsxx> sfxxList = taxRespBody.getSfxxList();

        if(sfxxList==null && sfxxList.size()<=0){
            rv.setStatus(20500);
            rv.setMessage("传入的税务核税/缴税信息集合为空");
            return rv;
        }
        for(Sj_Info_Qsxx sfxx:sfxxList){
            sfxx.setReceiptNumber(taxRespBody.getReceiptNumber());
            sfxx.setDataComeFromMode("接口");
            sfxx.setDataJson(JSONObject.toJSONString(sfxx));
        }
        serviceData.setServiceCode("PayingTaxService");
        serviceData.setServiceDataInfos(sfxxList);
        serviceDatas.add(serviceData);
        mapParmeter.put("serviceDatas", JSONArray.toJSONString(serviceDatas));
        System.out.println("契税处理结果："+JSONObject.toJSONString(mapParmeter));
        rv = backFeign.DealRecieveFromOuter2(token,mapParmeter);
        return rv;
    }
}
