package com.springboot.component.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.OtherEntity.EleWatGasHandle;
import com.springboot.entity.chenbin.personnel.req.DLReqEntity;
import com.springboot.entity.chenbin.personnel.req.ReqSendForWEGEntity;
import com.springboot.entity.chenbin.personnel.resp.DLReturnUnitEntity;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.pub_data.SJ_Exception_Record;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.util.SysPubDataDealUtil;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.ErrorDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static com.springboot.constant.chenbin.KeywordConstant.*;

/**
 * @author chenb
 * @version 2020/5/11/011
 * description：
 */
@Component
@Slf4j
public class ExchangeWithComponyComponent {

    @Autowired
    private OuterBackFeign backFeign;

    @Autowired
    private ExchangeWithOtherFeign otherFeign;

    /**
     * 描述：
     * 作者：chenb
     * 日期：2020/5/12/012
     * 参数：reqKey 传递的token
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public void handleExchange(String reqKey,ReqSendForWEGEntity sendTransferEntity) {
        log.info("token："+reqKey);
        log.info("参数："+ JSONObject.toJSONString(sendTransferEntity));
        try {
            //线程执行延后1s，为一窗受理侧准备数据留出充足时间
            Thread.sleep(5000);
        } catch (InterruptedException e){
            throw new ZtgeoBizException("分支线程出现等待错误");
        }

        /**
         * 获取一窗受理侧的办件数据
         */
        //获取数据
        ObjectRestResponse<SJ_Sjsq> taskSjsqRv = null;
        for(int i=0;i<5;i++){
            taskSjsqRv = backFeign.DealRecieveFromOuter7(
                    reqKey,
                    sendTransferEntity.getSqbh(),
                    sendTransferEntity.getTaskId()
            );
            if(taskSjsqRv!=null && taskSjsqRv.getStatus()==200 && taskSjsqRv.getData()!=null) {
                break;
            } else {
                if (i == 4) {
                    if(taskSjsqRv==null) {
                        throw new ZtgeoBizException("拉取一窗受理数据失败，响应为“null”");
                    } else if (taskSjsqRv.getStatus()!=200) {
                        throw new ZtgeoBizException("拉取一窗受理数据失败，响应状态异常，问题描述为“"+taskSjsqRv.getMessage()+"”");
                    } else {
                        throw new ZtgeoBizException("拉取一窗受理数据失败，响应状态正常，但响应数据为“"+JSONObject.toJSONString(taskSjsqRv.getData())+"”");
                    }
                }
            }
        }

        //数据转换
        SJ_Sjsq sjsq = null;
        try {
            sjsq = SysPubDataDealUtil.parseReceiptData(JSONObject.toJSONString(taskSjsqRv.getData()),null,null,null);
        } catch (ParseException e) {
            log.error("中间程序转换收件信息时失败，可能引起失败的原因是日期格式的数据转换异常");
            throw new ZtgeoBizException("中间程序转换收件信息时失败，可能引起失败的原因是日期格式的数据转换异常");
        }

        /**
         * 处理标志位
         */
        EleWatGasHandle handleSign = new EleWatGasHandle().initThis();
        if(sendTransferEntity.isExecAgain()){
            //取Rec中记录的TASK异常
            ObjectRestResponse<List<SJ_Exception_Record>> excsrv = backFeign.DealRecieveFromOuter8(
                    new SJ_Exception_Record().valuationTaskAndType(sendTransferEntity.getTaskId(),EXC_TYPE_FEIGNERROR,sendTransferEntity.getHandleKey())
            );
            if(excsrv!=null && excsrv.getStatus()==200 && excsrv.getData()!=null){
                List<SJ_Exception_Record> excs = excsrv.getData();
                if(excs.size()!=1)
                    throw new ZtgeoBizException(
                            "针对水电气广业务在做异常(任务ID：“"+sendTransferEntity.getTaskId()+"”，关键字：“"+sendTransferEntity.getHandleKey()+"”)后的第二次分发时，拉取到的异常信息数目异常，应为：1个，实际为："+excs.size()+"个"
                    );
                sendTransferEntity.setExc(excs.get(0));
            }else{
                throw new ZtgeoBizException(
                        "针对水电气广业务在做异常(任务ID：“"+sendTransferEntity.getTaskId()+"”，关键字：“"+sendTransferEntity.getHandleKey()+"”)后的第二次分发时，尝试拉取异常信息失败，失败原因可能为：返回为空/获取时异常/数据未给出等"
                );
            }
        }

        /**
         * 执行分发
         */
        handleExchange(reqKey,sendTransferEntity,handleSign,sjsq);
    }

    public void handleExchange(String reqKey,ReqSendForWEGEntity sendTransferEntity,EleWatGasHandle handleSign,SJ_Sjsq s){
        log.info("=================分割线（分支线程之后的异常将不会再向主线程抛出）=================");
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            //与电进行交互
            FutureTask<String> futureEle = new FutureTask<String>(new Callable<String>() {
                public String call() {//建议抛出异常
                    log.info("执行电分发线程");
                    //执行发送电部门过户申请
                    handleExchangeWithEle(reqKey, sendTransferEntity, s);
                    return "电分发执行成功";
                }
            });
            //与水进行交互
            FutureTask<String> futureWater = new FutureTask<String>(new Callable<String>() {
                public String call() {//建议抛出异常
                    log.info("执行水分发线程");
                    //执行发送水部门过户申请
                    handleExchangeWithWat(reqKey, sendTransferEntity, s);
                    return "水分发执行成功";
                }
            });
            //与气进行交互
            FutureTask<String> futureGas = new FutureTask<String>(new Callable<String>() {
                public String call() {//建议抛出异常
                    log.info("执行气分发线程");
                    //执行发送气部门过户申请
                    handleExchangeWithGas(reqKey, sendTransferEntity, s);
                    return "气分发执行成功";
                }
            });
            if (handleSign.isHandleEle()) {
                //执行电分发线程任务
                executor.execute(futureEle);
            }
            if (handleSign.isHandleWat()) {
                //执行水分发线程任务
                executor.execute(futureWater);
            }
            if (handleSign.isHandleGas()) {
                //执行气分发线程任务
                executor.execute(futureGas);
            }
        } catch (Exception e){
            log.error("水电气数据分发异常，详细信息：" + ErrorDealUtil.getErrorInfo(e));
        } finally {
            executor.shutdown();
        }
    }

    public void handleExchangeWithEle(String reqKey,ReqSendForWEGEntity sendTransferEntity,SJ_Sjsq sjsq){
        log.info("开始电分发线程,开始时间："+ TimeUtil.getTimeString(new Date()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e){
            throw new ZtgeoBizException("电分支线程出现等待错误");
        }

        DLReqEntity dlcs = new DLReqEntity();
        Map<String,Object> dlcsm = new HashMap<>();
        dlcsm.put("sign","");
        dlcsm.put("data",dlcs);
        OtherResponseEntity<DLReturnUnitEntity> dlBaskResult = otherFeign.sendPowerCompany(dlcsm);
        log.info("结束电分发线程,结束时间："+ TimeUtil.getTimeString(new Date()));
    }

    public void handleExchangeWithWat(String reqKey,ReqSendForWEGEntity sendTransferEntity,SJ_Sjsq sjsq){
        log.info("开始水分发线程,开始时间："+ TimeUtil.getTimeString(new Date()));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e){
            throw new ZtgeoBizException("水分支线程出现等待错误");
        }
        log.info("结束水分发线程,结束时间："+ TimeUtil.getTimeString(new Date()));
    }

    public void handleExchangeWithGas(String reqKey,ReqSendForWEGEntity sendTransferEntity,SJ_Sjsq sjsq){
        log.info("开始气分发线程,开始时间："+ TimeUtil.getTimeString(new Date()));
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e){
            throw new ZtgeoBizException("气分支线程出现等待错误");
        }
        log.info("结束气分发线程,结束时间："+ TimeUtil.getTimeString(new Date()));
    }
}
