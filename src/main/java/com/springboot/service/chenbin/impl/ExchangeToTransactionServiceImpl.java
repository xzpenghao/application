package com.springboot.service.chenbin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.tra.TraParamBody;
import com.springboot.entity.chenbin.personnel.tra.TraRespBody;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.service.chenbin.ExchangeToTransactionService;
import com.springboot.util.SysPubDataDealUtil;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service("exc2Tran")
public class ExchangeToTransactionServiceImpl implements ExchangeToTransactionService {

    @Value("${tra.bsryname}")
    private String username;
    @Value("${tra.bsrypassword}")
    private String password;

    @Autowired
    private ExchangeWithOtherFeign otherFeign;

    @Autowired
    private OuterBackFeign backFeign;

    @Override
    public String deal2Tra(String commonInterfaceAttributer) throws ParseException {
        String result = "处理成功";
        //token
        String token = backFeign.getToken(new JwtAuthenticationRequest(username,password)).getData();
        //处理数据
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        if (sjsq.getTransactionContractInfo() == null) {
            throw new ZtgeoBizException("业务交易系统分发时异常，交易合同信息为空");
        }
        if (sjsq.getImmovableRightInfoVoList() == null) {
            throw new ZtgeoBizException("业务交易系统分发时异常，不动产权利信息为空");
        }
        //准备参数
        TraParamBody traParamBody = BusinessDealBaseUtil.dealParamForTra(sjsq);
        System.out.println("进入交易处理，参数转换为:"+ JSONObject.toJSONString(traParamBody));
        //调用Feign
        Map<String,Object> traBody = new HashMap<String,Object>();
        traBody.put("sign","");
        traBody.put("data",traParamBody);
        log.info("交易转办最终传入数据为："+JSONObject.toJSONString(traBody));
        ObjectRestResponse<String> rv = otherFeign.testTra(traBody);
        if(rv.getStatus()==200){
            result = rv.getData();
            //成功后由交易人员签收办件
            String receiptNumber = sjsq.getReceiptNumber();
            Map<String, String> mapParmeter = new HashMap<>();
            mapParmeter.put("receiptNumber", receiptNumber);
            backFeign.dealRecieveFromOuter1(token,mapParmeter);
        }else{
            throw new ZtgeoBizException("交易通知失败，失败原因："+rv.getMessage());
        }
        System.out.println("交易处理返回，结果为:"+result);
        return result;
    }

    @Override
    public ObjectRestResponse<String> examineSuccess4Tra(TraRespBody traRespBody) {
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        if(traRespBody==null){
            rv.setStatus(20500);
            rv.setMessage("传入的交易备案数据为空");
            return rv;
        }
        System.out.println("正在获取操作用户token");
        String token = backFeign.getToken(new JwtAuthenticationRequest(username,password)).getData();

        Map<String, String> mapParmeter = new HashMap<>();
        mapParmeter.put("receiptNumber",traRespBody.getReceiptNumber());

        List<RespServiceData> serviceDatas = new ArrayList<>();
        RespServiceData serviceData = new RespServiceData();

        List<SJ_Info_Handle_Result> handleResultVoList = new ArrayList<>();
        SJ_Info_Handle_Result handleResult = new SJ_Info_Handle_Result();

        handleResult.setHandleText(traRespBody.getHandleText());
        if(StringUtils.isNotBlank(traRespBody.getHandleResult()) && "0".equals(traRespBody.getHandleResult())) {
            handleResult.setHandleResult("备案失败");
        }else{
            handleResult.setHandleResult("备案成功");
        }
        handleResult.setRemarks(traRespBody.getRemarks());
        handleResult.setDataComeFromMode("接口");
        handleResult.setProvideUnit(traRespBody.getProvideUnit());
        handleResult.setAcceptanceNumber(StringUtils.isNotBlank(traRespBody.getContractRecordNumber())?traRespBody.getContractRecordNumber():traRespBody.getAcceptanceNumber());
        handleResult.setOldNumber(traRespBody.getContractNumber());
        handleResult.setDataJson(JSONObject.toJSONString(traRespBody));

        handleResultVoList.add(handleResult);

        serviceData.setServiceCode("ThansferImmovableHandleResultService");
        serviceData.setServiceDataInfos(handleResultVoList);
        serviceDatas.add(serviceData);
        mapParmeter.put("serviceDatas", JSONArray.toJSONString(serviceDatas));
        System.out.println("交易处理结果数据："+JSONObject.toJSONString(serviceData));
        //调用feign返回处理结果并提交对应流程
        rv = backFeign.DealRecieveFromOuter2(token,mapParmeter);
        return rv;
    }
}