package com.springboot.component.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjinst;
import com.springboot.entity.chenbin.personnel.OtherEntity.EleWatGasHandle;
import com.springboot.entity.chenbin.personnel.req.*;
import com.springboot.entity.chenbin.personnel.resp.BooleanWithMsg;
import com.springboot.entity.chenbin.personnel.resp.SDQReturnUnitEntity;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.pub_data.SJ_Exception_Record;
import com.springboot.popj.pub_data.SJ_Sdqg_Send_Result;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.util.SysPubDataDealUtil;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import com.springboot.util.chenbin.ErrorDealUtil;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
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

    @Value("${chenbin.waitSec}")
    private int waitSec;
    @Value("${chenbin.maxConnCount}")
    private int maxConnCount;
    @Value("${penghao.highestAuthority.username}")
    private String adminName;
    @Value("${penghao.highestAuthority.password}")
    private String adminPass;

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
        log.info("YCSL->SDQG->>：token："+reqKey);
        log.info("YCSL->SDQG->>：参数："+ JSONObject.toJSONString(sendTransferEntity));
        log.info("YCSL->SDQG->>：【"+sendTransferEntity.getTaskId()+"】开始等待登簿步骤完结。。。");
        try {
            //线程执行延后1s，为一窗受理侧准备数据留出充足时间
            Thread.sleep(waitSec*1000);
        } catch (InterruptedException e){
            throw new ZtgeoBizException("分支线程出现等待错误");
        }

        int count=1;
        while (true){
            if(count>maxConnCount){
                throw new ZtgeoBizException("YCSL->SDQG->>：【"+sendTransferEntity.getTaskId()+"】尝试获知登簿步骤完结状态失败，尝试次数超限，中断执行线程");
            }
            ObjectRestResponse<Boolean> taskDone = backFeign.DealRecieveFromOuter16(reqKey,sendTransferEntity.getTaskId());
            log.info("YCSL->SDQG->>：【"+sendTransferEntity.getTaskId()+"】第"+count+"次尝试获知登簿步骤完结状态结果："+JSONObject.toJSONString(taskDone));
            if(
                    taskDone!=null &&
                    taskDone.getStatus()==200 &&
                    taskDone.getData()
            ) {
                log.info("YCSL->SDQG->>：【"+sendTransferEntity.getTaskId()+"】已获知登簿步骤完结");
                break;
            }
            count++;
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
        Map<String , SJ_Fjinst> fjinstMap = new HashMap<>();
        ObjectRestResponse<Map<String , SJ_Fjinst>> fjrv = backFeign.DealRecieveFromOuter13(reqKey,sendTransferEntity.getTaskId());
        if(fjrv.getStatus()==200)
            fjinstMap = fjrv.getData();

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
        handleExchange(reqKey,sendTransferEntity,handleSign,sjsq,fjinstMap);

    }

    public void handleExchange(
            String reqKey,
            ReqSendForWEGEntity sendTransferEntity,
            EleWatGasHandle handleSign,
            SJ_Sjsq s,
            Map<String , SJ_Fjinst> fjinstMap
    ){
        log.info("=================YCSL->SDQG->>->>：分割线（分支线程之后的异常将不会再向主线程抛出）=================");
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            //与电进行交互
            FutureTask<String> futureEle = new FutureTask<String>(new Callable<String>() {
                public String call() {//建议抛出异常
                    log.info("YCSL->SDQG->>->>ELE：执行电分发线程");
                    //执行发送电部门过户申请
                    handleExchangeWithEle(reqKey, sendTransferEntity, s, fjinstMap);
                    return "电分发执行结束";
                }
            });
            //与水进行交互
            FutureTask<String> futureWater = new FutureTask<String>(new Callable<String>() {
                public String call() {//建议抛出异常
                    log.info("YCSL->SDQG->>->>WAT：执行水分发线程");
                    //执行发送水部门过户申请
                    handleExchangeWithWat(reqKey, sendTransferEntity, s, fjinstMap);
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
            if(handleSign.isHandleEle()) {
                try {
                    log.info("YCSL->SDQG->>->>ELE：电处理结果返回：" + futureEle.get());
                } catch (Exception e) {
                    log.error("YCSL->SDQG->>->>ELE：电处理出现不受监控的异常：" + ErrorDealUtil.getErrorInfo(e));
                }
            }
            if(handleSign.isHandleWat()) {
                try {
                    log.info("YCSL->SDQG->>->>WAT：水处理结果返回：" + futureWater.get());
                } catch (Exception e) {
                    log.error("YCSL->SDQG->>->>WAT：水处理出现不受监控的异常：" + ErrorDealUtil.getErrorInfo(e));
                }
            }
            if(handleSign.isHandleGas()) {
                try {
                    log.info("YCSL->SDQG->>->>GAS：气处理结果返回：" + futureGas.get());
                } catch (Exception e){
                    log.error("YCSL->SDQG->>->>GAS：气处理出现不受监控的异常：" + ErrorDealUtil.getErrorInfo(e));
                }
            }

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

    /**
     * 描述：电力同步
     * 作者：chenb
     * 日期：2020/6/12/012
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public void handleExchangeWithEle(
            String reqKey,
            ReqSendForWEGEntity sendTransferEntity,
            SJ_Sjsq sjsq,
            Map<String , SJ_Fjinst> fjinstMap
    ){
        log.info("YCSL->SDQG->>->>ELE：开始电分发线程,开始时间："+ TimeUtil.getTimeString(new Date()));

        boolean isDealExc;
        String excMsg = "";
        try {
            DLReqEntity dlcs = BusinessDealBaseUtil.dealParamForEle(sjsq);
            List<SDQFile> datas = BusinessDealBaseUtil.dealFjForSDQ(sjsq.getTransactionContractInfo(),fjinstMap);
            datas = getEleNeedFiles(reqKey,datas);
            dlcs.setData(datas);
            Map<String, Object> dlcsm = new HashMap<>();
            dlcsm.put("sign", "");
            dlcsm.put("data", dlcs);
            OtherResponseEntity<SDQReturnUnitEntity> dlBaskResult = null;
            try{
                dlBaskResult = otherFeign.sendPowerCompany(dlcsm);
            } catch (RetryableException e){ //超时异常
                if(e.getMessage().contains("timed out")) {
                    log.info("YCSL->SDQG->>->>ELE：访问超时");
                    isDealExc = false;
                    //手动生成电力公司响应
                    dlBaskResult = new OtherResponseEntity<SDQReturnUnitEntity>();
                    SDQReturnUnitEntity dlreturn = new SDQReturnUnitEntity();
                    dlreturn.setResult("1");
                    dlreturn.setMessage("过户数据向电力分发时，请求超时");
                    dlBaskResult.setCode("0000");
                    dlBaskResult.setMsg("成功");
                    dlBaskResult.setData(dlreturn);
                }else{
                    throw e;
                }
            }

            if(dlBaskResult==null){
                log.error("YCSL->SDQG->>->>ELE：共享交换平台捕获了异常，联系处理");
                isDealExc = true;
                excMsg = "共享交换平台捕获了异常，联系处理";
            }else{
                log.info("电力返回同步结果："+JSONObject.toJSONString(dlBaskResult));
                if(!dlBaskResult.getCode().equals("0000")){
                    log.error("YCSL->SDQG->>->>ELE："+dlBaskResult.getMsg());
                    isDealExc = true;
                    excMsg = dlBaskResult.getMsg();
                }else{
                    SDQReturnUnitEntity dlrue = dlBaskResult.getData();
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

    /**
     * 描述：自来水同步
     * 作者：chenb
     * 日期：2020/6/12/012
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public void handleExchangeWithWat(
            String reqKey,
            ReqSendForWEGEntity sendTransferEntity,
            SJ_Sjsq sjsq,
            Map<String , SJ_Fjinst> fjinstMap
    ){
        log.info("YCSL->SDQG->>->>WAT：开始水分发线程,开始时间："+ TimeUtil.getTimeString(new Date()));

        //定义异常描述变量（两个）
        boolean isDealExc;
        String excMsg;

        try {
            //通用数据参数处理，参数来自收件申请数据
            Map<String,Object> waterBody = new HashMap<>();
            ZLSReqEntity zlscs = BusinessDealBaseUtil.dealParamForWat(sjsq);

            //声明一个自来水的返回结果对象
            ObjectRestResponse<SDQReturnUnitEntity> swBaskResult;
            //获取必要的操作token
            String backDealToken = backFeign.getToken(new JwtAuthenticationRequest(adminName,adminPass)).getData();
            //根据对接的自来水单位不同采用不同的同步方法，但必须要求响应报文格式一致
            switch (zlscs.getOrgNo()){
                case WATER_NAME_YK:
                    //处理yk自来水公司需要的附件数据
                    List<SDQFile> datas_yk = BusinessDealBaseUtil.dealFjForSDQ(sjsq.getTransactionContractInfo(),fjinstMap);
                    zlscs.setData(datas_yk);
                    waterBody.put("sign", "");
                    waterBody.put("data", zlscs);
                    //请求同步地址，获取响应数据
                    swBaskResult = backFeign.sendWaterCompany(backDealToken,waterBody);
                    break;
                case WATER_NAME_SY:
                    //处理sy自来水公司需要的附件数据
                    List<SDQFile> datas_sy = BusinessDealBaseUtil.dealFjForSDQ(sjsq.getTransactionContractInfo(),fjinstMap);
                    zlscs.setData(datas_sy);
                    waterBody.put("sign", "");
                    waterBody.put("data", zlscs);
                    //请求同步地址，获取响应数据
                    swBaskResult = backFeign.sendWaterCompany(backDealToken,waterBody);
                    break;
                case WATER_NAME_SS:
                    //处理sy自来水公司需要的附件数据
                    List<SDQFile> datas_ss = BusinessDealBaseUtil.dealFjForSDQ(sjsq.getTransactionContractInfo(),fjinstMap);
                    zlscs.setData(datas_ss);
                    waterBody.put("sign", "");
                    waterBody.put("data", zlscs);
                    //请求同步地址，获取响应数据
                    swBaskResult = backFeign.sendWaterCompany(backDealToken,waterBody);
                    break;
                default:
                    throw new ZtgeoBizException("自来水公司标识超出设置");
            }
            BooleanWithMsg bws = dealWaterResult(reqKey,sendTransferEntity,swBaskResult,zlscs.getOrgNo(),EXC_SDQG_HAPPEN_KEY_WAT);
            isDealExc = bws.isBol();
            excMsg = bws.getMsg();
        } catch (ZtgeoBizException e) {
            log.error("YCSL->SDQG->>->>WAT：自来水过户同步失败，出现可以预知的异常："+e.getMessage());
            isDealExc = true;
            excMsg = "自来水过户同步失败，出现可以预知的异常："+e.getMessage();
        } catch (Exception e) {
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
//        try {
//            Thread.sleep(15000);
//        } catch (InterruptedException e){
//            log.error("YCSL->SDQG->>->>GAS：气分支线程出现等待错误");
//        }
        boolean isDealExc;
        String excMsg = "燃气过户失败了";
        try {
            OtherResponseEntity<SDQReturnUnitEntity> dlBaskResult = otherFeign.sendPowerCompany(getTestData(sjsq,"gas"));
            BooleanWithMsg bws = dealTestResult(reqKey,sendTransferEntity,dlBaskResult,"gas",EXC_SDQG_HAPPEN_KEY_GAS);
            isDealExc = bws.isBol();
            excMsg = bws.getMsg();
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
            exceptionRecord = sendTransferEntity.getExc().giveAnExample();
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
     * 描述：水同步结果解析与处理
     * 作者：chenb
     * 日期：2020/6/12/012
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public BooleanWithMsg dealWaterResult(String reqKey, ReqSendForWEGEntity sendTransferEntity, ObjectRestResponse<SDQReturnUnitEntity> swBaskResult, String compony, String handleKey){
        log.info("水给出处理结果为："+JSONObject.toJSONString(swBaskResult));
        BooleanWithMsg isDealExc = new BooleanWithMsg();
        if(swBaskResult==null){
            log.error("异常！当前自来水数据发送响应为null");
            isDealExc.setBol(true);
            isDealExc.setMsg("异常！当前自来水数据发送响应为null");
        }else{
            if(swBaskResult.getStatus()!=200){
                //逻辑异常的处理
                log.error(swBaskResult.getMessage());
                isDealExc.setBol(true);
                isDealExc.setMsg(swBaskResult.getMessage());
            }else{
                //针对水具体的响应结果的处理
                isDealExc = dealResult(reqKey,sendTransferEntity,handleKey,compony,swBaskResult.getData(),swBaskResult);
            }
        }
        return isDealExc;
    }

    /**
     * 描述：针对水电气具体的响应结果的处理
     * 作者：chenb
     * 日期：2020/6/12/012
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public BooleanWithMsg dealResult(String reqKey, ReqSendForWEGEntity sendTransferEntity, String handleKey,String compony,SDQReturnUnitEntity sdqrue,Object sourceResult){
        BooleanWithMsg isDealExc = new BooleanWithMsg();
        if(sdqrue==null){
            isDealExc.setBol(true);
            isDealExc.setMsg(handleKey+"过户返回结果的data接收为null，联系相应公司解决");
        } else if(StringUtils.isBlank(sdqrue.getResult())){
            isDealExc.setBol(true);
            isDealExc.setMsg(handleKey+"过户返回结果的data已接收但未给出过户result，联系相应公司解决");
        } else {
            SJ_Sdqg_Send_Result sdqgResult = new SJ_Sdqg_Send_Result().initSuccessFromWEGEntity(sendTransferEntity,handleKey);
            sdqgResult.setSendCompony(compony);
            sdqgResult.setResultJson(JSONObject.toJSONString(sourceResult));
            sdqgResult.setResult(sdqrue.getResult());
            sdqgResult.setResultStr(sdqrue.getMessage());
            if ("0".equals(sdqrue.getResult())) {
                isDealExc.setBol(true);
                isDealExc.setMsg(handleKey+"过户同步失败，相应公司给出失败原因：" + sdqrue.getMessage());
            } else {
                isDealExc.setBol(false);
                //明确给出处理成功
                if (sendTransferEntity.isExecAgain() && handleKey.equals(sendTransferEntity.getHandleKey())) {
                    sendTransferEntity.getExc().setHandleStatus("1");
                }
            }
            //将过户结果回写(有-更新/无-插入)
            backFeign.DealRecieveFromOuter11(reqKey, sdqgResult);
        }
        return isDealExc;
    }

    /**
     * 以下部分属于测试需要
     * @param sjsq
     * @return
     */

    public Map<String,Object> getTestData(SJ_Sjsq sjsq,String type){
        Map<String, Object> dlcsm = new HashMap<>();
        dlcsm.put("sign", "");
        SDQReqEntity p = BusinessDealBaseUtil.dealParamForGas(sjsq);
        p.setOrgNo(type);
        dlcsm.put("data", p);
        return dlcsm;
    }

    public BooleanWithMsg dealTestResult(String reqKey, ReqSendForWEGEntity sendTransferEntity, OtherResponseEntity<SDQReturnUnitEntity> dlBaskResult, String compony, String handleKey){
        BooleanWithMsg isDealExc = new BooleanWithMsg();
        if(dlBaskResult==null){
            log.info("共享交换平台捕获了异常，联系处理");
            isDealExc.setBol(true);
            isDealExc.setMsg("共享交换平台捕获了异常，联系处理");
        }else{
            if(!dlBaskResult.getCode().equals("0000")){
                log.error(dlBaskResult.getMsg());
                isDealExc.setBol(true);
                isDealExc.setMsg(dlBaskResult.getMsg());
            }else{
                isDealExc = dealResult(reqKey,sendTransferEntity,handleKey,compony,dlBaskResult.getData(),dlBaskResult);
            }
        }
        return isDealExc;
    }

    public List<SDQFile> getEleNeedFiles(String reqKey, List<SDQFile> datas){
        List<Integer> removeIndexs = new ArrayList<>();
        for(int i=0;i<datas.size();i++){
            SDQFile data = datas.get(i);
            log.info("YCSL->SDQG->>->>ELE：执行第"+i+"/"+datas.size()+"个附件拉取任务，当前拉取的附件对象是："+data.getFileName());
            ObjectRestResponse<String> fileRv = backFeign.DealRecieveFromOuter15(reqKey,data.getFileData());
            if(fileRv!=null && fileRv.getStatus()==200){
                data.setFileData(fileRv.getData());
            }else{
                removeIndexs.add(i);
            }
        }
        for(Integer index:removeIndexs){
            datas.remove(datas.get(index));
        }
        return datas;
    }
}
