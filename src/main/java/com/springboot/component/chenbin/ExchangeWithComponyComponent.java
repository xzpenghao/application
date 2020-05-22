package com.springboot.component.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.chenbin.personnel.OtherEntity.EleWatGasHandle;
import com.springboot.entity.chenbin.personnel.req.DLReqEntity;
import com.springboot.entity.chenbin.personnel.req.ReqSendForWEGEntity;
import com.springboot.entity.chenbin.personnel.resp.DLReturnUnitEntity;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.pub_data.SJ_Exception_Record;
import com.springboot.popj.pub_data.SJ_Sdqg_Send_Result;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.util.SysPubDataDealUtil;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import com.springboot.util.chenbin.ErrorDealUtil;
import feign.RetryableException;
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

    @Autowired
    private HttpCallComponent httpCallComponent;

    /**
     * 描述：
     * 作者：chenb
     * 日期：2020/5/12/012
     * 参数：reqKey 传递的token
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public void handleExchange(String reqKey,ReqSendForWEGEntity sendTransferEntity) {
        log.info("YCSL->SDQG->>：token："+reqKey);
        log.info("YCSL->SDQG->>：参数："+ JSONObject.toJSONString(sendTransferEntity));
        try {
            //线程执行延后1s，为一窗受理侧准备数据留出充足时间
            Thread.sleep(1*1000);
        } catch (InterruptedException e){
            throw new ZtgeoBizException("分支线程出现等待错误");
        }

        /**
         * 获取一窗受理侧的办件数据
         */
        //获取数据
        ObjectRestResponse<String> taskSjsqRv = null;
        for(int i=0;i<5;i++){
            taskSjsqRv = backFeign.DealRecieveFromOuter7(
                    reqKey,
                    sendTransferEntity.getSqbh(),
                    sendTransferEntity.getTaskId()
            );
            if(taskSjsqRv!=null && taskSjsqRv.getStatus()==200 && StringUtils.isNotBlank(taskSjsqRv.getData())) {
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
        SJ_Sjsq sjsq;
        try {
            log.info("YCSL->SDQG->>：取得的原始不动产登簿步骤数据："+JSONObject.toJSONString(taskSjsqRv));
            sjsq = SysPubDataDealUtil.parseReceiptData(taskSjsqRv.getData(),null,null,null);
            log.info("YCSL->SDQG->>：取得处理后的不动产登簿步骤数据："+JSONObject.toJSONString(sjsq));
        } catch (ParseException e) {
            log.error("YCSL->SDQG->>：中间程序转换收件信息时失败，可能引起失败的原因是日期格式的数据转换异常");
            throw new ZtgeoBizException("中间程序转换收件信息时失败，可能引起失败的原因是日期格式的数据转换异常");
        }

        //获取附件数据

        //处理附件


        /**
         * 为处理准备辅助性数据
         */
        EleWatGasHandle handleSign = new EleWatGasHandle().initThis();
        if(sendTransferEntity.isExecAgain()){

            //处理handleSign
            if(StringUtils.isBlank(sendTransferEntity.getHandleKey())){
                throw new ZtgeoBizException("水电气异常产生后执行再次分发时，happenKey(执行关键字)未确定");
            }
            switch (sendTransferEntity.getHandleKey()){
                case EXC_SDQG_HAPPEN_KEY_ALL:
                    handleSign.initThis();
                    break;
                case EXC_SDQG_HAPPEN_KEY_WAT:
                    handleSign.onlyWat();
                    break;
                case EXC_SDQG_HAPPEN_KEY_ELE:
                    handleSign.onlyEle();
                    break;
                case EXC_SDQG_HAPPEN_KEY_GAS:
                    handleSign.onlyGas();
                    break;
                case EXC_SDQG_HAPPEN_KEY_TV:
                    handleSign.allNoExec();
                    break;
                default:
                    throw new ZtgeoBizException("水电气异常产生后执行再次分发时，happenKey(执行关键字)不在目前使用范围");
            }

            //取Rec中记录的TASK异常
            ObjectRestResponse<List<SJ_Exception_Record>> excsrv = backFeign.DealRecieveFromOuter8(
                    reqKey,
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
        log.info("=================YCSL->SDQG->>->>：分割线（分支线程之后的异常将不会再向主线程抛出）=================");
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            //与电进行交互
            FutureTask<String> futureEle = new FutureTask<String>(new Callable<String>() {
                public String call() {//建议抛出异常
                    log.info("YCSL->SDQG->>->>ELE：执行电分发线程");
                    //执行发送电部门过户申请
                    handleExchangeWithEle(reqKey, sendTransferEntity, s);
                    return "电分发执行结束";
                }
            });
            //与水进行交互
            FutureTask<String> futureWater = new FutureTask<String>(new Callable<String>() {
                public String call() {//建议抛出异常
                    log.info("YCSL->SDQG->>->>WAT：执行水分发线程");
                    //执行发送水部门过户申请
                    handleExchangeWithWat(reqKey, sendTransferEntity, s);
                    return "水分发执行结束";
                }
            });
            //与气进行交互
            FutureTask<String> futureGas = new FutureTask<String>(new Callable<String>() {
                public String call() {//建议抛出异常
                    log.info("YCSL->SDQG->>->>GAS：执行气分发线程");
                    //执行发送气部门过户申请
                    handleExchangeWithGas(reqKey, sendTransferEntity, s);
                    return "气分发执行结束";
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

            //处理结果获取
            if(handleSign.isHandleEle())
                log.info("YCSL->SDQG->>->>ELE：电处理结果返回："+futureEle.get());
            if(handleSign.isHandleWat())
                log.info("YCSL->SDQG->>->>WAT：水处理结果返回："+futureWater.get());
            if(handleSign.isHandleGas())
                log.info("YCSL->SDQG->>->>GAS：气处理结果返回："+futureGas.get());

            if(sendTransferEntity.isExecAgain()){   //二次触发时处理
                SJ_Exception_Record exc = sendTransferEntity.getExc();
                if(EXC_SDQG_HAPPEN_KEY_ALL.equals(sendTransferEntity.getHandleKey())){//处理全部时
                    if(StringUtils.isBlank(exc.getHandleStatus())){//没给出明确处理结果为成功
                        exc.setHandleStatus("1");
                        exc.setHandleResult("success");
                    }
                    //修改异常处理状态
                    backFeign.DealRecieveFromOuter10(reqKey,exc);//无论怎么样都会给出处理意见
                }else{//处理独立分支时
                    if(StringUtils.isNotBlank(exc.getHandleStatus()) && "1".equals(exc.getHandleStatus())){//明确给出处理成功为成功
                        exc.setHandleResult("success");
                        //修改异常处理状态
                        backFeign.DealRecieveFromOuter10(reqKey,exc);//成功时，将异常设置为已处理
                    }
                }
            }
        } catch (Exception e){
            log.error("YCSL->SDQG->>->>：水电气数据分发异常，详细信息：" + ErrorDealUtil.getErrorInfo(e));
        } finally {
            executor.shutdown();
        }
    }

    public void handleExchangeWithEle(String reqKey,ReqSendForWEGEntity sendTransferEntity,SJ_Sjsq sjsq){
        log.info("YCSL->SDQG->>->>ELE：开始电分发线程,开始时间："+ TimeUtil.getTimeString(new Date()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e){
            log.error("电分支线程出现等待错误");
        }
        boolean isDealExc;
        String excMsg = "";
        try {
            DLReqEntity dlcs = BusinessDealBaseUtil.dealParamForEle(sjsq);
            Map<String, Object> dlcsm = new HashMap<>();
            dlcsm.put("sign", "");
            dlcsm.put("data", dlcs);
            OtherResponseEntity<DLReturnUnitEntity> dlBaskResult = null;
            try{
                dlBaskResult = otherFeign.sendPowerCompany(dlcsm);
            } catch (RetryableException e){ //超时异常
                if(e.getMessage().contains("timed out")) {
                    log.info("YCSL->SDQG->>->>ELE：访问超时");
                    isDealExc = false;
                    //手动生成电力公司响应
                    dlBaskResult = new OtherResponseEntity<DLReturnUnitEntity>();
                    DLReturnUnitEntity dlreturn = new DLReturnUnitEntity();
                    dlreturn.setResult("1");
                    dlreturn.setMessage("过户数据向电力分发时，请求超时");
                    dlBaskResult.setCode("0000");
                    dlBaskResult.setMsg("成功");
                    dlBaskResult.setData(dlreturn);
                }
            }

            if(dlBaskResult==null){
                log.error("YCSL->SDQG->>->>ELE：共享交换平台捕获了异常，联系处理");
                isDealExc = true;
                excMsg = "共享交换平台捕获了异常，联系处理";
            }else{
                if(!dlBaskResult.getCode().equals("0000")){
                    log.error("YCSL->SDQG->>->>ELE："+dlBaskResult.getMsg());
                    isDealExc = true;
                    excMsg = dlBaskResult.getMsg();
                }else{
                    DLReturnUnitEntity dlrue = dlBaskResult.getData();
                    if(dlrue==null){
                        log.error("YCSL->SDQG->>->>ELE：电力过户返回结果的data接收为null，联系电力公司解决");
                        isDealExc = true;
                        excMsg = "电力过户返回结果的data接收为null，联系电力公司解决";
                    } else if(StringUtils.isBlank(dlrue.getResult())){
                        log.error("YCSL->SDQG->>->>ELE：电力过户返回结果的data已接收但未给出过户result，联系电力公司解决");
                        isDealExc = true;
                        excMsg = "电力过户返回结果的data已接收但未给出过户result，联系电力公司解决";
                    } else {
                        SJ_Sdqg_Send_Result eleResult = new SJ_Sdqg_Send_Result().initSuccessFromWEGEntity(sendTransferEntity,EXC_SDQG_HAPPEN_KEY_ELE);
                        eleResult.setSendCompony(dlcs.getOrgNo());
                        eleResult.setResultJson(JSONObject.toJSONString(dlBaskResult));
                        eleResult.setResult(dlrue.getResult());
                        eleResult.setResultStr(dlrue.getMessage());
                        if ("0".equals(dlrue.getResult())) {
                            log.error("YCSL->SDQG->>->>ELE：电力过户同步失败，电力公司方给出失败原因：" + dlrue.getMessage());
                            isDealExc = true;
                            excMsg = "电力过户同步失败，电力公司方给出失败原因：" + dlrue.getMessage();
                        } else {
                            isDealExc = false;
                            //明确给出处理成功
                            if (sendTransferEntity.isExecAgain() && EXC_SDQG_HAPPEN_KEY_ELE.equals(sendTransferEntity.getHandleKey())) {
                                sendTransferEntity.getExc().setHandleStatus("1");
                            }
                        }
                        //将过户结果回写(有-更新/无-插入)
                        backFeign.DealRecieveFromOuter11(reqKey, eleResult);
                    }
                }
            }
        } catch (ZtgeoBizException e){
            log.error("YCSL->SDQG->>->>ELE：电力过户同步失败，出现可以预知的异常："+e.getMessage());
            isDealExc = true;
            excMsg = "电力过户同步失败，出现可以预知的异常："+e.getMessage();
        } catch (Exception e){
            log.error("YCSL->SDQG->>->>ELE：电力过户同步失败，出现未知的异常："+ErrorDealUtil.getErrorInfo(e));
            isDealExc = true;
            excMsg = "电力过户同步失败，出现未知的异常：" + ErrorDealUtil.getErrorInfo(e);
        }
        if(isDealExc){
            handleDealExc(reqKey,EXC_SDQG_HAPPEN_KEY_ELE,sendTransferEntity,excMsg);
        }
        log.info("YCSL->SDQG->>->>ELE：结束电分发线程,结束时间："+ TimeUtil.getTimeString(new Date()));
    }

    public void handleExchangeWithWat(String reqKey,ReqSendForWEGEntity sendTransferEntity,SJ_Sjsq sjsq){
        log.info("YCSL->SDQG->>->>WAT：开始水分发线程,开始时间："+ TimeUtil.getTimeString(new Date()));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error("YCSL->SDQG->>->>WAT：水分支线程出现等待错误");
        }
        boolean isDealExc;
        String excMsg = "自来水过户失败了";
        try {
            OtherResponseEntity<DLReturnUnitEntity> dlBaskResult = otherFeign.sendPowerCompany(getTestData(sjsq,"water"));
            isDealExc = dealTestResult(reqKey,sendTransferEntity,dlBaskResult,"water",EXC_SDQG_HAPPEN_KEY_WAT);
        } catch (ZtgeoBizException e){
            log.error("YCSL->SDQG->>->>WAT：自来水过户同步失败，出现可以预知的异常："+e.getMessage());
            isDealExc = true;
            excMsg = "自来水过户同步失败，出现可以预知的异常："+e.getMessage();
        } catch (Exception e){
            log.error("YCSL->SDQG->>->>WAT：自来水过户同步失败，出现未知的异常："+ErrorDealUtil.getErrorInfo(e));
            isDealExc = true;
            excMsg = "自来水过户同步失败，出现未知的异常："+ErrorDealUtil.getErrorInfo(e);
        }
        if(isDealExc){
            handleDealExc(reqKey,EXC_SDQG_HAPPEN_KEY_WAT,sendTransferEntity,excMsg);
        }
        log.info("YCSL->SDQG->>->>WAT：结束水分发线程,结束时间："+ TimeUtil.getTimeString(new Date()));
    }

    public void handleExchangeWithGas(String reqKey,ReqSendForWEGEntity sendTransferEntity,SJ_Sjsq sjsq){
        log.info("YCSL->SDQG->>->>GAS：开始气分发线程,开始时间："+ TimeUtil.getTimeString(new Date()));
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e){
            log.error("YCSL->SDQG->>->>GAS：气分支线程出现等待错误");
        }
        boolean isDealExc;
        String excMsg = "燃气过户失败了";
        try {
            OtherResponseEntity<DLReturnUnitEntity> dlBaskResult = otherFeign.sendPowerCompany(getTestData(sjsq,"gas"));
            isDealExc = dealTestResult(reqKey,sendTransferEntity,dlBaskResult,"gas",EXC_SDQG_HAPPEN_KEY_GAS);
        } catch (ZtgeoBizException e){
            log.error("YCSL->SDQG->>->>GAS：燃气过户同步失败，出现可以预知的异常："+e.getMessage());
            isDealExc = true;
            excMsg = "燃气过户同步失败，出现可以预知的异常："+e.getMessage();
        } catch (Exception e){
            log.error("YCSL->SDQG->>->>GAS：燃气过户同步失败，出现未知的异常："+ErrorDealUtil.getErrorInfo(e));
            isDealExc = true;
            excMsg = "燃气过户同步失败，出现未知的异常："+ErrorDealUtil.getErrorInfo(e);
        }
        if(isDealExc){
            handleDealExc(reqKey,EXC_SDQG_HAPPEN_KEY_GAS,sendTransferEntity,excMsg);
        }
        log.info("YCSL->SDQG->>->>GAS：结束气分发线程,结束时间："+ TimeUtil.getTimeString(new Date()));
    }

    public ReqSendForWEGEntity initNewWEGEntityAgin(String handleKey ,ReqSendForWEGEntity example){
        ReqSendForWEGEntity newWEGEntity = new ReqSendForWEGEntity();
        newWEGEntity.setTaskId(example.getTaskId());
        newWEGEntity.setSqbh(example.getSqbh());
        newWEGEntity.setHandleKey(handleKey);
        newWEGEntity.setExcutFeign(example.getExcutFeign());
        newWEGEntity.setExcutMethod(example.getExcutMethod());
        newWEGEntity.setExecAgain(true);
        return newWEGEntity;
    }

    public void handleDealExc(String reqKey,String handleKey,ReqSendForWEGEntity sendTransferEntity,String excMsg){
        ReqSendForWEGEntity newEntity = initNewWEGEntityAgin(handleKey,sendTransferEntity);
        Map<String,String> params = new HashMap<>();
        params.put("token","");
        params.put("transferEntity",JSONObject.toJSONString(newEntity));
        SJ_Exception_Record exceptionRecord;
        if(sendTransferEntity.isExecAgain()){
            exceptionRecord = sendTransferEntity.getExc();
            if(EXC_SDQG_HAPPEN_KEY_ALL.equals(sendTransferEntity.getHandleKey())){
                sendTransferEntity.getExc().setHandleResult("unsuccessful");
                sendTransferEntity.getExc().setHandleStatus("1");
            }
        } else {
            exceptionRecord = new SJ_Exception_Record()
                    .initNewExcp(
                            sendTransferEntity.getTaskId(),
                            excMsg,
                            sendTransferEntity.getSqbh(),
                            "",
                            EXC_DIRECTION_OUTER
                    )
                    .transitThisToFeign(
                            JSONObject.toJSONString(params),
                            handleKey,
                            sendTransferEntity.getExcutFeign(),
                            sendTransferEntity.getExcutMethod()
                    );
        }
        exceptionRecord.setHappenKey(handleKey);
        exceptionRecord.setFeignNoticeExecutParams(JSONObject.toJSONString(params));
        exceptionRecord.setExcMsg(excMsg);
        backFeign.DealRecieveFromOuter9(reqKey,exceptionRecord);
    }

    /**
     * 以下部分属于测试需要
     * @param sjsq
     * @return
     */

    public Map<String,Object> getTestData(SJ_Sjsq sjsq,String type){
        Map<String, Object> dlcsm = new HashMap<>();
        dlcsm.put("sign", "");
        DLReqEntity p = BusinessDealBaseUtil.dealParamForEle(sjsq);
        p.setOrgNo(type);
        dlcsm.put("data", p);
        return dlcsm;
    }

    public boolean dealTestResult(String reqKey,ReqSendForWEGEntity sendTransferEntity,OtherResponseEntity<DLReturnUnitEntity> dlBaskResult,String compony,String handleKey){
        boolean isDealExc;
        if(dlBaskResult==null){
            log.error("共享交换平台捕获了异常，联系处理");
            isDealExc = true;
        }else{
            if(!dlBaskResult.getCode().equals("0000")){
                log.error(dlBaskResult.getMsg());
                isDealExc = true;
            }else{
                DLReturnUnitEntity dlrue = dlBaskResult.getData();
                if(dlrue==null){
                    log.error(handleKey+"过户返回结果的data接收为null，联系相应公司解决");
                    isDealExc = true;
                } else if(StringUtils.isBlank(dlrue.getResult())){
                    log.error(handleKey+"过户返回结果的data已接收但未给出过户result，联系相应公司解决");
                    isDealExc = true;
                } else {
                    SJ_Sdqg_Send_Result testResult = new SJ_Sdqg_Send_Result().initSuccessFromWEGEntity(sendTransferEntity,handleKey);
                    testResult.setSendCompony(compony);
                    testResult.setResultJson(JSONObject.toJSONString(dlBaskResult));
                    testResult.setResult(dlrue.getResult());
                    testResult.setResultStr(dlrue.getMessage());
                    if ("0".equals(dlrue.getResult())) {
                        log.error(handleKey+"过户同步失败，相应公司给出失败原因：" + dlrue.getMessage());
                        isDealExc = true;
                    } else {
                        isDealExc = false;
                        //明确给出处理成功
                        if (sendTransferEntity.isExecAgain() && handleKey.equals(sendTransferEntity.getHandleKey())) {
                            sendTransferEntity.getExc().setHandleStatus("1");
                        }
                    }
                    //将过户结果回写(有-更新/无-插入)
                    backFeign.DealRecieveFromOuter11(reqKey, testResult);
                }
            }
        }
        return isDealExc;
    }
}
