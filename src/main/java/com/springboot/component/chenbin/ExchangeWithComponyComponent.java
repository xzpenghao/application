package com.springboot.component.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.OtherEntity.EleWatGasHandle;
import com.springboot.entity.chenbin.personnel.req.ReqSendForWEGEntity;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.pub_data.SJ_Sjsq;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

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

    public void handleExchange(String reqKey,ReqSendForWEGEntity sendTransferEntity) {
        try {
            //线程执行延后1s，为一窗受理侧准备数据留出充足时间
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new ZtgeoBizException("分支线程出现等待错误");
        }
        /**
         * 获取一窗受理侧的办件数据
         */
        ObjectRestResponse<SJ_Sjsq> taskSjsqRv;
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
                        throw new ZtgeoBizException("拉取一窗受理数据失败，响应状态正常，但响应数据为“"+taskSjsqRv.getMessage()+"”");
                    }
                }
            }
        }

        /**
         * 执行水电气通知接口
         */
        EleWatGasHandle handleSign = new EleWatGasHandle().initThis();
        ExecutorService executor = Executors.newCachedThreadPool();
        if(sendTransferEntity.isExecAgain()){
            //取Rec中记录的TASK异常

        }
        if(handleSign.isHandleEle()){

        }
        if(handleSign.isHandleWat()){
            //与水进行交互

        }
        if(handleSign.isHandleGas()){
            //与气进行交互

        }
    }
}
