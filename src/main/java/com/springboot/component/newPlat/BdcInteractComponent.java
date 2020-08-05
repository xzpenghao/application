package com.springboot.component.newPlat;

import com.alibaba.fastjson.JSONArray;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.entity.newPlat.query.resp.Djshxx;
import com.springboot.entity.newPlat.transInner.req.BdcNoticeReq;
import com.springboot.feign.OuterBackFeign;
import com.springboot.feign.newPlat.BdcQueryFeign;
import com.springboot.popj.pub_data.RespServiceData;
import com.springboot.popj.pub_data.SJ_Info_Handle_Result;
import com.springboot.popj.pub_data.Sj_Sjsq_Bdc_Mapping;
import com.springboot.util.newPlatBizUtil.ResultConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.springboot.constant.chenbin.KeywordConstant.*;

/**
 * @author sk
 * @version 2020/8/3
 * description：
 */
@Component
public class BdcInteractComponent {
    @Autowired
    private BdcQueryFeign bdcQueryFeign;
    @Autowired
    private OuterBackFeign backFeign;

    /**
     * 描述：执行通知接口
     *          switch选择执行
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：[token, noticeBody]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void handleNotice(String token,BdcNoticeReq noticeBody){
        switch(noticeBody.getJdbs()){
            //受理
            case BDC_NOTICE_JD_SL:
                handleNoticeAccept(token,noticeBody.getWsywh());
                break;
            //审核
            case BDC_NOTICE_JD_SH:
                handleNoticeVerify(token,noticeBody.getWsywh(),noticeBody.getYwhlb());
                break;
            //登簿
            case BDC_NOTICE_JD_DB:
                handleNoticeResult(token,noticeBody.getWsywh(),noticeBody.getYwhlb());
                break;
            default:
                throw new ZtgeoBizException("Unexpected value【节点标识】: " + noticeBody.getJdbs());
        }
    }

    /**
     * 描述：受理执行
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：[wsywh]
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void handleNoticeAccept(String token,String wsywh){
        Map<String, String> sjsqMap = new HashMap<>();
        sjsqMap.put("receiptNumber",wsywh);
        backFeign.DealRecieveFromOuter2(token,sjsqMap);
    }

    /**
     * 描述：审核执行
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：[wsywh, ywhlb]
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void handleNoticeVerify(String token,String wsywh, List<String> ywhlb){
        //定义返回rec项目的参数map
        Map<String, String> sjsqMap = new HashMap<>();
        //写入收件申请编号
        sjsqMap.put("receiptNumber",wsywh);
        List<RespServiceData> serviceDatas = new ArrayList<>();
        //组织结果并形成标准服务数据
        RespServiceData serviceData = new RespServiceData();
        List<SJ_Info_Handle_Result> handleResultVoList = new ArrayList<>();
        for(String ywh:ywhlb) {
            //查询审核结果
            OtherResponseEntity<Djshxx> djshxxs = bdcQueryFeign.shsjcx(ywh);
            //检查接口返回结果(基础检查)
            ResultConvertUtil.checkQueryResult("不动产审核结果获取接口",djshxxs);
            Djshxx djshxx = djshxxs.getData();
            SJ_Info_Handle_Result handleResult = new SJ_Info_Handle_Result();
            handleResult.setDataComeFromMode("接口");
            handleResult.setProvideUnit(DATA_SOURCE_DEPART_BDC);
            handleResult.setHandleResult(YCSL_HANDLE_RESULT_PASS);
            handleResult.setHandleText(ywh+"号不动产登记办件"+YCSL_HANDLE_RESULT_PASS);
            handleResult.setRemarks(ResultConvertUtil.initBdcShjgByInterfceGet(ywh,djshxx));
            handleResultVoList.add(handleResult);
        }
        serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCSHCLJG);
        serviceData.setServiceDataInfos(handleResultVoList);
        serviceDatas.add(serviceData);
        sjsqMap.put("serviceDatas", JSONArray.toJSONString(serviceDatas));
        backFeign.DealRecieveFromOuter2(token,sjsqMap);
    }

    /**
     * 描述：登簿执行
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：[wsywh, ywhlb]
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void handleNoticeResult(String token,String wsywh, List<String> ywhlb){
        //声明保存主体
        Map<String, String> sjsqMap = new HashMap<>();
        //写入申请编号
        sjsqMap.put("receiptNumber",wsywh);
        //创建服务数据集合
        List<RespServiceData> serviceDatas = new ArrayList<>();
        //写入固定成功处理服务数据
        serviceDatas.add(initBookSuccessService(ywhlb));
        //分业务拉取组织服务数据
        for(String ywh:ywhlb){
            //组织结果并形成标准服务数据
            RespServiceData serviceData = new RespServiceData();
            ObjectRestResponse<Sj_Sjsq_Bdc_Mapping> bdcMapResp = backFeign.DealRecieveFromOuter17(
                    token,new Sj_Sjsq_Bdc_Mapping(null,wsywh,null,ywh));
            //核验挂接关系
            ResultConvertUtil.checkYcResult(bdcMapResp);
            Sj_Sjsq_Bdc_Mapping bdcMapping = bdcMapResp.getData();
            if(bdcMapping==null){
                throw new ZtgeoBizException("通知不动产登簿数据时，查询不动产业务与一窗业务号挂接关系失败，" +
                        "未查询到存在【"+wsywh+"】与【"+ywh+"】的挂接关系");
            }
            if(StringUtils.isBlank(bdcMapping.getBdcywlx())){
                throw new ZtgeoBizException("不明确的不动产登记业务类型，【"+ywh+"】的一窗不动产业务挂接关系缺失关键的业务类型描述");
            }
            switch (bdcMapping.getBdcywlx()){
                case BDC_YWLX_NAME_QS:

                    serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCSHCLJG);
//                    serviceData.setServiceDataInfos(handleResultVoList);
                    serviceDatas.add(serviceData);
                    break;
                case BDC_YWLX_NAME_DY:

                    serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCSHCLJG);
//                    serviceData.setServiceDataInfos(handleResultVoList);
                    serviceDatas.add(serviceData);
                    break;
                case BDC_YWLX_NAME_YG:

                    serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCSHCLJG);
//                    serviceData.setServiceDataInfos(handleResultVoList);
                    serviceDatas.add(serviceData);
                    break;
                case BDC_YWLX_NAME_QSZX:

                    serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCSHCLJG);
//                    serviceData.setServiceDataInfos(handleResultVoList);
                    serviceDatas.add(serviceData);
                    break;
                case BDC_YWLX_NAME_DYZX:

                    serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCSHCLJG);
//                    serviceData.setServiceDataInfos(handleResultVoList);
                    serviceDatas.add(serviceData);
                    break;
                case BDC_YWLX_NAME_YGZX:

                    serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCSHCLJG);
//                    serviceData.setServiceDataInfos(handleResultVoList);
                    serviceDatas.add(serviceData);
                    break;
                case BDC_YWLX_NAME_CF:
                    break;
                case BDC_YWLX_NAME_CFZX:
                    break;
                case BDC_YWLX_NAME_YY:
                    break;
                case BDC_YWLX_NAME_YYZX:
                    break;
                default:
                    throw new ZtgeoBizException("不合法的业务类型描述，加载到的业务类型为："+bdcMapping.getBdcywlx());
            }
        }
        sjsqMap.put("serviceDatas", JSONArray.toJSONString(serviceDatas));
        backFeign.DealRecieveFromOuter2(token,sjsqMap);
    }

    public RespServiceData initBookSuccessService(List<String> ywhlb){
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCDBCLJG);

        List<SJ_Info_Handle_Result> handleResultVoList = new ArrayList<>();
        SJ_Info_Handle_Result handleResult = new SJ_Info_Handle_Result();
        handleResult.setDataComeFromMode("接口");
        handleResult.setProvideUnit(DATA_SOURCE_DEPART_BDC);
        handleResult.setHandleResult(BDC_DB_JG_SUCCESS);
        handleResult.setHandleText(BDC_DB_JG_SUCCESS);
        handleResult.setRemarks("【"+StringUtils.join(ywhlb, "】,【")+"】号业务登簿成功！");

        handleResultVoList.add(handleResult);
        serviceData.setServiceDataInfos(handleResultVoList);
        return serviceData;
    }
}
