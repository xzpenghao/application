package com.springboot.service.chenbin.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.ExchangeWithComponyComponent;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.req.DLReqEntity;
import com.springboot.entity.chenbin.personnel.req.ReqSendForWEGEntity;
import com.springboot.entity.chenbin.personnel.resp.DLReturnUnitEntity;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.ReturnVo;
import com.springboot.util.chenbin.ErrorDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author chenb
 * @version 2020/4/29/029
 * description：
 */
@Slf4j
@Service("exc2Comp")
public class ExchangeWithComponyService {
    @Autowired
    private ExchangeWithOtherFeign otherFeign;
    @Autowired
    private ExchangeWithComponyComponent exchangeWithComponyComponent;

    public void exchangeWithWEGComponies(
            String reqKey,
            ReqSendForWEGEntity sendTransferEntity,
            HttpServletResponse resp
    ){
        log.info("进入主线程");
        /**
         * 实例化分支线程的执行容器，
         * 为防止finally中shutdown抛null异常，
         * 必须定义在最上方
         */
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            resp.setContentType("application/json;charset=UTF-8");
            OutputStream out = resp.getOutputStream();
            out.write(JSONObject.toJSONString(new ObjectRestResponse<String>().data("水电气数据交换请求发送成功！")).getBytes("UTF-8"));
            out.flush();
            out.close();
        } catch (IOException e){
            log.error("主线程执行发生IO异常请处理," + ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("IO异常请处理");
        }

        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            public String call() {//建议抛出异常
                log.info("执行分支线程");
                //执行发送电部门过户申请
                exchangeWithComponyComponent.handleExchange(reqKey,sendTransferEntity);
                return "执行成功";
            }
        });
        executor.execute(future);
        try {
            // 创建数据
            String result = future.get(); //取得结果，同时设置超时执行时间为5秒。
            log.debug("线程执行结果：" + result);
        } catch (Exception e) {
            log.info("捕获到执行异常");
            //这里做异常处理(分支线程产生的)
            log.error("水电气广同步出现未知异常："+ErrorDealUtil.getErrorInfo(e));
            e = ErrorDealUtil.OnlineErrorTrans(e);
            //调用回写异常信息进Rec

        } finally {
            executor.shutdown();
        }
    }

    public OtherResponseEntity<DLReturnUnitEntity> exchangeWithPowerCompany(DLReqEntity dlcs){
        Map<String,Object> dlcsm = new HashMap<>();
        dlcsm.put("sign","");
        dlcsm.put("data",dlcs);
        return otherFeign.sendPowerCompany(dlcsm);
    }
}
