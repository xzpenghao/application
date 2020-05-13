package com.springboot.service.chenbin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.OtherComponent;
import com.springboot.component.chenbin.RequestInterceptComponent;
import com.springboot.config.ZtgeoActivitiException;
import com.springboot.config.ZtgeoBizException;
import com.springboot.constant.chenbin.BusinessConstant;
import com.springboot.entity.chenbin.personnel.pub_use.Biz_Request_Intercept;
import com.springboot.entity.chenbin.personnel.tra.TraParamBody;
import com.springboot.entity.chenbin.personnel.tra.TraRespBody;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.feign.OuterBackFeign;
import com.springboot.mapper.RequestInterceptMapper;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.popj.registration.ImmovableFile;
import com.springboot.service.chenbin.ExchangeToTransactionService;
import com.springboot.util.SysPubDataDealUtil;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import com.springboot.util.chenbin.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
//    @Value("${area.code}")
//    private String areaCode;

    @Autowired
    private ExchangeWithOtherFeign otherFeign;

    @Autowired
    private OuterBackFeign backFeign;

    @Autowired
    private OtherComponent otherComponent;

    @Autowired
    private RequestInterceptMapper requestInterceptMapper;

    @Autowired
    private RequestInterceptComponent requestInterceptComponent;

    @Override
    public String deal2Tra(String commonInterfaceAttributer) throws ParseException {
        log.info("开始发送");
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
        List<ImmovableFile> files = otherComponent.getFileList(sjsq.getReceiptNumber(),token);
        traParamBody.setFJXX(BusinessDealBaseUtil.convertFiles(files));
        System.out.println("进入交易处理，参数转换为:"+ JSONObject.toJSONString(traParamBody));
        //调用Feign
        Map<String,Object> traBody = new HashMap<String,Object>();
        traBody.put("sign","");
        log.info("交易转办传入基带处理数据为："+JSONObject.toJSONString(traParamBody));
        JSONObject jsonObj = BusinessDealBaseUtil.dealJSONForSB(traParamBody);
//        jsonObj.getJSONObject("YCSLXX").put("QYBM",areaCode);
        traBody.put("data",jsonObj);
        System.out.println("发送交易的数据（处理后）："+JSONObject.toJSONString(traBody));
        log.info("发送交易的数据（处理后）："+JSONObject.toJSONString(traBody));
        try {
            ObjectRestResponse<String> rv = otherFeign.testTra(traBody);
            if (rv.getStatus() == 200) {
                result = rv.getData();
            } else {
                log.error("交易通知失败，失败原因：" + rv.getMessage());
                throw new ZtgeoBizException("交易通知失败，失败原因：" + rv.getMessage());
            }
            log.info("交易处理返回，结果为:"+JSONObject.toJSONString(rv));
        } catch (Exception e){
            log.error("交易通知异常，异常产生原因：" + e.getMessage());
            throw new ZtgeoBizException("交易通知异常，异常产生原因：" + e.getMessage());
        } finally {
            //成功后由交易人员签收办件
            String receiptNumber = sjsq.getReceiptNumber();
            Map<String, String> mapParmeter = new HashMap<>();
            mapParmeter.put("receiptNumber", receiptNumber);
            backFeign.dealRecieveFromOuter1(token,mapParmeter);
        }
        System.out.println("交易处理返回，结果为:"+result);
        return result;
    }

    @Override
    public ObjectRestResponse<String> examineSuccess4Tra(TraRespBody traRespBody) {
        System.out.println("(交易)解析传入数据："+JSONObject.toJSONString(traRespBody));
        log.info("解析传入数据："+JSONObject.toJSONString(traRespBody));
        //验证传入数据的处理状态
        Biz_Request_Intercept requestIntercept = requestInterceptMapper.selectByInputIndex(traRespBody.getReceiptNumber(),BusinessConstant.INTERFACE_CODE_EXAMINE_TRA);
        if(requestIntercept!=null){
            //请求的次数拦截限制
            if(requestIntercept.getOperationCount()>=5){
                throw new ZtgeoActivitiException("请求超限，请合理安排请求方式");
            }
            if(requestIntercept.getResultCode()==200){
                throw new ZtgeoActivitiException("流程已被提交,请勿重复操作");
            }
        }
        //处理数据
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        if(traRespBody==null){
            rv.setStatus(20500);
            rv.setMessage("传入的交易备案数据为空");
            return rv;
        }
        System.out.println("正在获取操作用户token");
        log.info("正在获取操作用户token");
        String token = backFeign.getToken(new JwtAuthenticationRequest(username,password)).getData();
        log.info("用户token获取成功");
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
        log.info("交易处理结果数据："+JSONObject.toJSONString(serviceData));
        //调用feign返回处理结果并提交对应流程
        rv = backFeign.DealRecieveFromOuter2(token,mapParmeter);
        log.info("一窗受理处理结果1："+JSONObject.toJSONString(rv));
        if(rv.getStatus()==200){
            rv.data("操作成功");
        }
        requestInterceptComponent.dealSaveThisIntercept(
                requestIntercept,
                rv,
                traRespBody.getReceiptNumber(),
                BusinessConstant.INTERFACE_CODE_EXAMINE_TRA,
                username,
                "本接口执行房管网签合同备案信息结果回推，成功时返回200，失败则是20500，超出限制或已被提交返回20200",
                "宿迁市住建局"
        );
        return rv;
    }
}