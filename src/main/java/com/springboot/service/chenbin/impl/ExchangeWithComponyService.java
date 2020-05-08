package com.springboot.service.chenbin.impl;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.Msgagger;
import com.springboot.entity.chenbin.personnel.req.DLReqEntity;
import com.springboot.entity.chenbin.personnel.req.ReqSendForWEGEntity;
import com.springboot.entity.chenbin.personnel.resp.DLReturnUnitEntity;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.ReturnVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private OuterBackFeign backFeign;

    public Object exchangeWithWEGComponies(ReqSendForWEGEntity sendTransferEntity, OutputStream outputStream) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ReturnVo returnVo = new ReturnVo();
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            public String call() {//建议抛出异常
                //执行发送电部门过户申请
                System.out.println("1");
                return null;
            }
        });
        executor.execute(future);
        long t = System.currentTimeMillis();
        ObjectRestResponse rv=new ObjectRestResponse();
        JSONObject object= null;
        try {
            returnVo.setCode(200);
            returnVo.setMessage(Msgagger.CG);
            object = JSONObject.fromObject(returnVo);
//            outputStream.write(object.toString().getBytes("UTF-8"));
//            outputStream.flush();
//            outputStream.close();
            System.out.println(JSONObject.fromObject(returnVo));
            // 创建数据
            String result = future.get(); //取得结果，同时设置超时执行时间为5秒。
            System.out.println(result);
            System.err.println("result is " + JSONObject.fromObject(returnVo) + ", time is " + (System.currentTimeMillis() - t));
            executor.shutdown();
        } catch (Exception e) {
            log.error("e"+e);
            e.getStackTrace();
        }
        return  object ;
    }

    public OtherResponseEntity<DLReturnUnitEntity> exchangeWithPowerCompany(DLReqEntity dlcs){
        Map<String,Object> dlcsm = new HashMap<>();
        dlcsm.put("sign","");
        dlcsm.put("data",dlcs);
        return otherFeign.sendPowerCompany(dlcsm);
    }
}
