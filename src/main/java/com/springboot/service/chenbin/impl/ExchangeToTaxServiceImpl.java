package com.springboot.service.chenbin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.OtherComponent;
import com.springboot.component.chenbin.RequestInterceptComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.constant.chenbin.BusinessConstant;
import com.springboot.entity.chenbin.personnel.pub_use.Biz_Request_Intercept;
import com.springboot.entity.chenbin.personnel.tax.TaxParamBody;
import com.springboot.entity.chenbin.personnel.tax.TaxRespBody;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.feign.OuterBackFeign;
import com.springboot.mapper.RequestInterceptMapper;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.popj.registration.ImmovableFile;
import com.springboot.service.chenbin.ExchangeToTaxService;
import com.springboot.util.SysPubDataDealUtil;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import com.springboot.util.chenbin.ErrorDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

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

    @Autowired
    private OtherComponent otherComponent;

    @Autowired
    private RequestInterceptMapper requestInterceptMapper;

    @Autowired
    private RequestInterceptComponent requestInterceptComponent;

    @Override
    public String deal2Tax(String commonInterfaceAttributer) throws ParseException {
        String result = "处理成功";
        log.info("开始发送");
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
        List<ImmovableFile> files = otherComponent.getFileList(sjsq.getReceiptNumber(),token);
        taxParamBody.setFJXX(BusinessDealBaseUtil.convertFiles(files));
        System.out.println("进入税务处理，参数转换为:"+JSONObject.toJSONString(taxParamBody));
        log.info("税务转办传入基带处理数据为："+JSONObject.toJSONString(taxParamBody));
        //调用Feign,暂时取消
        Map<String,Object> taxBody = new HashMap<String,Object>();
        taxBody.put("sign","");
        JSONObject jsonObj = BusinessDealBaseUtil.dealJSONForSB(taxParamBody);
        taxBody.put("data",jsonObj);
        log.info("发送税务的数据："+JSONObject.toJSONString(taxBody));
        try {
            ObjectRestResponse<String> rv = otherFeign.testTax(taxBody);
            if(rv.getStatus()==200){
                result = rv.getData();
            }else{
                log.error("税务通知失败，失败原因："+rv.getMessage());
                throw new ZtgeoBizException("税务通知失败，失败原因："+rv.getMessage());
            }
            log.info("税务处理返回，结果为:"+JSONObject.toJSONString(rv));
        } catch (Exception e){
            log.error("税务通知异常，异常产生原因：" + ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("税务通知异常，异常产生原因：" + e.getMessage());
        } finally {
            //成功后由税务人员签收办件
            String receiptNumber = sjsq.getReceiptNumber();
            Map<String, String> mapParmeter = new HashMap<>();
            mapParmeter.put("receiptNumber", receiptNumber);
            backFeign.dealRecieveFromOuter1(token,mapParmeter);
        }
        return result;
    }

    @Override
    public ObjectRestResponse<String> examineSuccess4Tax(TaxRespBody taxRespBody) {
        System.out.println("解析传入数据："+JSONObject.toJSONString(taxRespBody));
        log.info("解析传入数据："+JSONObject.toJSONString(taxRespBody));
        String result = "";
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        if(taxRespBody==null){
            rv.setStatus(20500);
            rv.setMessage("传入的税务数据为空");
            return rv;
        }
        System.out.println("正在获取操作用户token");
        log.info("正在获取操作用户token");
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
            if(StringUtils.isNotBlank(sfxx.getDzspBase64())){
                //处理电子税票

            }
        }
        serviceData.setServiceCode("PayingTaxService");
        serviceData.setServiceDataInfos(sfxxList);
        serviceDatas.add(serviceData);
        mapParmeter.put("serviceDatas", JSONArray.toJSONString(serviceDatas));
        System.out.println("契税处理结果："+JSONObject.toJSONString(mapParmeter));
        log.info("契税处理结果："+JSONObject.toJSONString(mapParmeter));
        rv = backFeign.DealRecieveFromOuter2(token,mapParmeter);
        log.info("一窗受理处理结果："+JSONObject.toJSONString(rv));
        if(rv.getStatus()==200){
            rv.data("操作成功");
        }
        return rv;
    }

    @Override
    public ObjectRestResponse<List<ObjectRestResponse>> examineSuccess4TaxBatch(String data_str) {
        //设置返回
        ObjectRestResponse<List<ObjectRestResponse>> rv = new ObjectRestResponse<List<ObjectRestResponse>>();
        List<ObjectRestResponse> rvBeans = new ArrayList<ObjectRestResponse>();
        //执行
        JSONObject data = JSONObject.parseObject(data_str);
        JSONArray data_array = data.getJSONArray("wsxxList");
        List<TaxRespBody> data_jBean = data_array.toJavaList(TaxRespBody.class);
        try {
            for(TaxRespBody taxRespBody:data_jBean){
                ObjectRestResponse<String> rvBean = new ObjectRestResponse<String>();
                //验证传入数据的处理状态
                Biz_Request_Intercept requestIntercept = requestInterceptMapper.selectByInputIndex(taxRespBody.getReceiptNumber(), BusinessConstant.INTERFACE_CODE_EXAMINE_TAX);
                if(requestIntercept!=null){
                    //请求的次数拦截限制
                    if(requestIntercept.getOperationCount()>=5){
                        rvBean.setStatus(20200);
                        rvBean.setMessage("请求超限，请合理安排请求方式");
                        rvBean.setData(taxRespBody.getReceiptNumber());
                        rvBeans.add(rvBean);
                        continue;
                    }
                    if(requestIntercept.getResultCode()==200){
                        rvBean.setStatus(20200);
                        rvBean.setMessage("流程已被提交,请勿重复操作");
                        rvBean.setData(taxRespBody.getReceiptNumber());
                        rvBeans.add(rvBean);
                        continue;
                    }
                }
                ObjectRestResponse<String> resultResp = examineSuccess4Tax(taxRespBody);
                if (resultResp.getStatus() == 200) {
                    rvBean.setStatus(200);
                    rvBean.setMessage("操作成功");
                    rvBean.setData(taxRespBody.getReceiptNumber());
                    rvBeans.add(rvBean);
                    log.info("‘"+taxRespBody.getReceiptNumber()+"’号办件完税结果回推成功");
                } else if(resultResp.getStatus() == 20500){
                    rvBean.setStatus(20500);
                    rvBean.setMessage(resultResp.getMessage());
                    rvBean.setData(taxRespBody.getReceiptNumber());
                    rvBeans.add(rvBean);
                    log.warn("‘"+taxRespBody.getReceiptNumber()+"’税务完税结果回推时，出现可以预见的逻辑性异常，异常说明："+resultResp.getMessage());
                }
                requestInterceptComponent.dealSaveThisIntercept(
                        requestIntercept,
                        rvBean,
                        taxRespBody.getReceiptNumber(),
                        BusinessConstant.INTERFACE_CODE_EXAMINE_TAX,
                        username,
                        "本接口执行税务完税信息结果回推，成功时返回200，失败则是20500，超出限制或已被提交返回20200",
                        "宿迁市税务局"
                );
            }
        } catch (ZtgeoBizException e){
            log.error(ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException(e.getMessage());
        }
        rv.data(rvBeans);
        log.info("本次完税处理结果为："+JSONArray.toJSONString(rvBeans));
        return rv;
    }

}
