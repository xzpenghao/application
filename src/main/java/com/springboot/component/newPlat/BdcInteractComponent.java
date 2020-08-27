package com.springboot.component.newPlat;

import com.alibaba.fastjson.JSONArray;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.entity.newPlat.query.bizData.Dbjgxx;
import com.springboot.entity.newPlat.query.resp.Djdbxx;
import com.springboot.entity.newPlat.query.resp.Djshxx;
import com.springboot.entity.newPlat.transInner.req.BdcNoticeReq;
import com.springboot.feign.OuterBackFeign;
import com.springboot.feign.newPlat.BdcQueryFeign;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.popj.warrant.ParametricData2;
import com.springboot.service.newPlat.chenbin.BdcQueryService;
import com.springboot.util.chenbin.ErrorDealUtil;
import com.springboot.util.newPlatBizUtil.ResultConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.springboot.constant.chenbin.KeywordConstant.*;

/**
 * @author sk
 * @version 2020/8/3
 * description：不动产登簿结果通知
 */
@Slf4j
@Component
public class BdcInteractComponent {
    @Autowired
    private BdcQueryFeign bdcQueryFeign;
    @Autowired
    private OuterBackFeign backFeign;
    @Autowired
    private BdcQueryService bdcQueryService;

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
            //废弃(预想的逻辑实现是，调用主程序发websocket通知和站内信)
            case BDC_NOTICE_JD_FQ:

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
            djshxxs.checkSelfIfBdc("不动产审核结果获取接口");
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
        //不动产一窗挂接数组
//        boolean isUpdateBdcMap = false;
//        List<Sj_Sjsq_Bdc_Mapping> bdcMappings = new ArrayList<>();
        //创建服务数据集合
        List<RespServiceData> serviceDatas = new ArrayList<>();
        //写入固定成功处理服务数据
        serviceDatas.add(initBookSuccessService(ywhlb));
        //分业务拉取组织服务数据
        for(String ywh:ywhlb){
            //组织结果并形成标准服务数据
            RespServiceData serviceData;
            //获取核验不动产一窗挂接结果
            Sj_Sjsq_Bdc_Mapping bdcMapping = getCheckedBdcMappingForDB(token,wsywh,ywh);
            //获取登簿结果
            List<Dbjgxx> dbjglb = getAndBaseCheckDbjg(ywh);
            //组织登簿需要使用的服务数据
            switch (bdcMapping.getBdcywlx()){
                //不动产权利信息服务数据生成
                case BDC_YWLX_NAME_QS:
                    serviceData = initBdcqlxxService(ywh,dbjglb);
                    break;
                //不动产抵押信息服务数据生成
                case BDC_YWLX_NAME_DY:
                    serviceData = initBdcdyxxService(ywh,dbjglb);
                    break;
                case BDC_YWLX_NAME_YG:
                    serviceData = initBdcygxxService(ywh,dbjglb);
                    break;
                case BDC_YWLX_NAME_QSZX:
                    serviceData = initBdcqlzxxxService(token,ywh,wsywh,dbjglb);
                    break;
                case BDC_YWLX_NAME_DYZX:
                    serviceData = initBdcdyzxxxService(token,ywh,wsywh,dbjglb);
                    break;
                case BDC_YWLX_NAME_YGZX:
                    serviceData = initBdcygzxxxService(ywh,wsywh,dbjglb);
                    break;
                case BDC_YWLX_NAME_CF:
                    throw new ZtgeoBizException("一窗受理暂不支持的登簿返回业务【"+BDC_YWLX_NAME_CF+"】");
                case BDC_YWLX_NAME_CFZX:
                    throw new ZtgeoBizException("一窗受理暂不支持的登簿返回业务【"+BDC_YWLX_NAME_CFZX+"】");
                case BDC_YWLX_NAME_YY:
                    throw new ZtgeoBizException("一窗受理暂不支持的登簿返回业务【"+BDC_YWLX_NAME_YY+"】");
                case BDC_YWLX_NAME_YYZX:
                    throw new ZtgeoBizException("一窗受理暂不支持的登簿返回业务【"+BDC_YWLX_NAME_YYZX+"】");
                default:
                    throw new ZtgeoBizException("不合法的业务类型描述，加载到的业务类型为："+bdcMapping.getBdcywlx());
            }
            if(serviceData!=null) {//追加服务数据
                serviceDatas.add(serviceData);
            }
        }
        sjsqMap.put("serviceDatas", JSONArray.toJSONString(serviceDatas));
//        if(isUpdateBdcMap) {
//            sjsqMap.put("bdcMappingVoList", JSONArray.toJSONString(bdcMappings));
//        }
        //调用rec程序保存对应登簿结果服务数据并提交对应流程
        ObjectRestResponse<String> submitResp = backFeign.DealRecieveFromOuter2(token,sjsqMap);
        ResultConvertUtil.checkYcResult(submitResp,"外部保存提交一窗任务接口");
    }

    /**
     * 描述：初始化登簿成功结果服务数据
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[ywhlb]
     * 返回：RespServiceData
     * 更新记录：更新人：{}，更新日期：{}
     */
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

    /**
     * 描述：初始化不动产权利信息服务数据
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[ywh, dbjglb]
     * 返回：RespServiceData
     * 更新记录：更新人：{}，更新日期：{}
     */
    public RespServiceData initBdcqlxxService(String ywh,List<Dbjgxx> dbjglb){
        ResultConvertUtil.checkDbjgNonNull(ywh,dbjglb);
        //组织结果并形成标准服务数据
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCQZJG);
        List<SJ_Info_Bdcqlxgxx> qzVoList = new ArrayList<>();
        for(Dbjgxx dbjg:dbjglb){
            ParametricData parametricData = new ParametricData();
            parametricData.setBdczh(dbjg.getBdczh());
            try {
                List<SJ_Info_Bdcqlxgxx> qzxxs = bdcQueryService.queryQzxxWithItsRights(parametricData);
                if(qzxxs.size()!=1)
                    throw new ZtgeoBizException("不动产权证号全证号【"+dbjg.getBdczh()+"】精确匹配结果不唯一，请联系管理员解决");
                //登簿回推时，去除他项权验证
                qzxxs.get(0).setItsRightVoList(null);
                qzVoList.add(qzxxs.get(0));
            } catch (ParseException e) {
                log.error("【不动产权属登簿】结果转换异常，json数据格式异常，异常详情："+ ErrorDealUtil.getErrorInfo(e));
                throw new ZtgeoBizException("结果转换异常，数据格式异常");
            }
        }
        serviceData.setServiceDataInfos(qzVoList);
        return serviceData;
    }

    /**
     * 描述：初始化不动产抵押信息服务数据
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[ywh, dbjglb]
     * 返回：RespServiceData
     * 更新记录：更新人：{}，更新日期：{}
     */
    public RespServiceData initBdcdyxxService(String ywh,List<Dbjgxx> dbjglb){
        ResultConvertUtil.checkDbjgNonNull(ywh,dbjglb);
        //组织结果并形成标准服务数据
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCDYJG);
        List<Sj_Info_Bdcdyxgxx> dyVoList = new ArrayList<>();
        for(Dbjgxx dbjg:dbjglb){
            ParametricData2 parametricData = new ParametricData2();
            parametricData.setDyzmh(dbjg.getBdczh());
            try {
                List<Sj_Info_Bdcdyxgxx> dyxxs = bdcQueryService.queryDyxxByZmhAndDyr(parametricData);
                if(dyxxs.size()!=1)
                    throw new ZtgeoBizException("不动产抵押证明号全证号【"+dbjg.getBdczh()+"】精确匹配结果不唯一，请联系管理员解决");
                dyVoList.add(dyxxs.get(0));
            } catch (ParseException e) {
                log.error("【不动产抵押登簿】结果转换异常，json数据格式异常，异常详情："+ ErrorDealUtil.getErrorInfo(e));
                throw new ZtgeoBizException("结果转换异常，数据格式异常");
            }
        }
        serviceData.setServiceDataInfos(dyVoList);

        return serviceData;
    }

    /**
     * 描述：初始化不动产预告信息服务数据
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[ywh, dbjglb]
     * 返回：RespServiceData
     * 更新记录：更新人：{}，更新日期：{}
     */
    public RespServiceData initBdcygxxService(String ywh,List<Dbjgxx> dbjglb){
        ResultConvertUtil.checkDbjgNonNull(ywh,dbjglb);
        //组织结果并形成标准服务数据
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCYGJG);
        List<SJ_Info_Bdcqlxgxx> ygVoList = new ArrayList<>();

        serviceData.setServiceDataInfos(ygVoList);
        return serviceData;
    }

    /**
     * 描述：初始化不动产权证注销信息服务数据
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[ywh, dbjglb]
     * 返回：RespServiceData
     * 更新记录：更新人：{}，更新日期：{}
     */
    public RespServiceData initBdcqlzxxxService(String token,String ywh,String wsywh,List<Dbjgxx> dbjglb){
        //注销类登簿结果检查，可填充统一校验的内容
        ResultConvertUtil.checkZxldbjg(dbjglb);
        //组织结果并形成标准服务数据
        return initZxlService(token,wsywh,YCSL_SERVICE_CODE_BDCQZZXJG,YCSL_SERVICE_CODE_BDCQLSJ);
    }

    /**
     * 描述：初始化不动产抵押注销信息服务数据
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[ywh, dbjglb]
     * 返回：RespServiceData
     * 更新记录：更新人：{}，更新日期：{}
     */
    public RespServiceData initBdcdyzxxxService(String token,String ywh,String wsywh,List<Dbjgxx> dbjglb){
        //注销类登簿结果检查，可填充统一校验的内容
        ResultConvertUtil.checkZxldbjg(dbjglb);
        //组织结果并形成标准服务数据
        return initZxlService(token,wsywh,YCSL_SERVICE_CODE_BDCDYZXJG,YCSL_SERVICE_CODE_BDCDYSJ);
    }

    /**
     * 描述：初始化不动产预告注销信息服务数据
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[ywh, dbjglb]
     * 返回：RespServiceData
     * 更新记录：更新人：{}，更新日期：{}
     */
    public RespServiceData initBdcygzxxxService(String ywh,String wsywh,List<Dbjgxx> dbjglb){
        //注销类登簿结果检查，可填充统一校验的内容
        ResultConvertUtil.checkZxldbjg(dbjglb);
        //组织结果并形成标准服务数据
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode(YCSL_SERVICE_CODE_BDCYGZXJG);
        List<SJ_Info_Bdcqlxgxx> ygzxVoList = new ArrayList<>();

        serviceData.setServiceDataInfos(ygzxVoList);
        return serviceData;
    }

    /**
     * 描述：查询不动产与一窗受理业务挂接结果
     *          分析检查返回结果是否满足登簿要求
     *          数据规范时返回挂接结果数据
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[token, sqbh, ywh]
     * 返回：Sj_Sjsq_Bdc_Mapping
     * 更新记录：更新人：{}，更新日期：{}
     */
    public Sj_Sjsq_Bdc_Mapping getCheckedBdcMappingForDB(String token,String sqbh,String ywh){
        ObjectRestResponse<Sj_Sjsq_Bdc_Mapping> bdcMapResp = backFeign.DealRecieveFromOuter17(
                token,new Sj_Sjsq_Bdc_Mapping(null,sqbh,null,ywh));
        //核验挂接关系
        ResultConvertUtil.checkYcResult(bdcMapResp,"取一窗-不动产业务映射信息接口");
        Sj_Sjsq_Bdc_Mapping bdcMapping = bdcMapResp.getData();
        if(bdcMapping==null){
            throw new ZtgeoBizException("通知不动产登簿数据时，查询不动产业务与一窗业务号挂接关系失败，" +
                    "未查询到存在【"+sqbh+"】与【"+ywh+"】的挂接关系");
        }
        if(StringUtils.isBlank(bdcMapping.getBdcywlx())){
            throw new ZtgeoBizException("不明确的不动产登记业务类型，【"+ywh+"】的一窗不动产业务挂接关系缺失关键的业务类型描述");
        }
        return bdcMapping;
    }

    /**
     * 描述：登簿结果的基础检查
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[ywh]
     * 返回：List<Dbjgxx>
     * 更新记录：更新人：{}，更新日期：{}
     */
    public List<Dbjgxx> getAndBaseCheckDbjg(String ywh){
        OtherResponseEntity<Djdbxx> dbxxResp = bdcQueryFeign.dbsjcx(ywh);
        //检查登簿结果并返回主要信息
        dbxxResp.checkSelfIfBdc("不动产登簿结果获取接口");
        Djdbxx djdbxx = dbxxResp.getData();
        if(djdbxx==null){
            throw new ZtgeoBizException("【"+ywh+"】号不动产办件未查询到对应登簿结果信息");
        }
        return djdbxx.getDbjglb();
    }

    /**
     * 描述：注销类业务服务数据输出
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[wsywh, serviceCode, sourceSvrCode]
     * 返回：RespServiceData
     * 更新记录：更新人：{}，更新日期：{}
     */
    public RespServiceData initZxlService(String token,String wsywh,String serviceCode,String sourceSvrCode){
        //组织结果并形成标准服务数据
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode(serviceCode);
        ObjectRestResponse<SJ_Sjsq> sjsqResp = backFeign.getReceiptData(token,wsywh,null,sourceSvrCode,null);
        ResultConvertUtil.checkYcResult(sjsqResp,"取收件数据接口");
        List<RespServiceData> sjSvrs = sjsqResp.getData().getServiceDatas();
        if(sjSvrs.size()!=1){
            throw new ZtgeoBizException("注销服务数据条数返回异常");
        }
        serviceData.setServiceDataInfos(sjSvrs.get(0).getServiceDataInfos());
        return serviceData;
    }
}
