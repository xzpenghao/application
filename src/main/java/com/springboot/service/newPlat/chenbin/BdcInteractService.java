package com.springboot.service.newPlat.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.newPlat.BdcInteractComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.newPlat.transInner.req.BdcNoticeReq;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.pub_data.SJ_Exception_Record;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.util.chenbin.ErrorDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static com.springboot.constant.chenbin.KeywordConstant.EXC_DIRECTION_OUTER;

/**
 * @author chenb
 * @version 2020/7/30/030
 * description：不动产交互服务
 */
@Slf4j
@Service
public class BdcInteractService {

    @Value("${djj.bsryname}")
    private String bsryname;
    @Value("${djj.bsrypassword}")
    private String bsrypassword;

    @Autowired
    private OuterBackFeign backFeign;

    @Autowired
    private BdcInteractComponent bdcInteractComponent;

    /**
     * 描述：通知的异步处理模块
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：[bizStamp, noticeBody, resp]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void noticeMe(BdcNoticeReq noticeBody, HttpServletResponse resp){
        log.info("BDC->YCSL：【"+noticeBody.getWsywh()+"】接入不动产办件节点通知模块,主线程(节点通知响应线程)执行！");
        try {
            resp.setContentType("application/json;charset=UTF-8");
            OutputStream out = resp.getOutputStream();
            out.write(JSONObject.toJSONString(new ObjectRestResponse<String>().data("通知接收成功！")).getBytes("UTF-8"));
            out.flush();
            out.close();
        } catch (IOException e){
            log.error("BDC->YCSL：【"+noticeBody.getWsywh()+"】接入不动产办件节点通知模块,通知接收返回异常，导致异常原因：" + ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("通知失败，出现IO异常，请知悉");
        }

        log.info("=================BDC->YCSL：【"+noticeBody.getWsywh()+"】接入不动产办件节点通知模块：分割线（之后的异常将不会再抛出）=================");
        //声明token信息
        String token = null;
        try {
            token = backFeign.getToken(new JwtAuthenticationRequest(bsryname, bsrypassword)).getData();
        } catch (Exception e){
            log.error("BDC->YCSL：【"+noticeBody.getWsywh()+"】接入不动产办件节点通知模块,获取通知处理用户时出现错误，错误信息为："
                    +ErrorDealUtil.getErrorInfo(e));
        }
        if(StringUtils.isNotBlank(token)) {
            String useToken = token;
            //实例化分支线程的执行容器
            ExecutorService executor = Executors.newCachedThreadPool();
            //定义分支线程任务
            FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
                public String call() {//建议抛出异常
                    log.info("BDC->YCSL：【" + noticeBody.getWsywh() + "】接入不动产办件节点通知模块：执行分支线程(节点通知处理线程)");
                    //执行节点通知
                    bdcInteractComponent.handleNotice(useToken,noticeBody);
                    return "分支线程执行结束";
                }
            });
            //执行分支线程
            executor.execute(future);
            //处理分支返回结果
            try {
                // 创建数据
                String result = future.get(); //取得结果，同时设置超时执行时间为5秒。
                log.info("BDC->YCSL：【" + noticeBody.getWsywh() + "】接入不动产办件节点通知模块,通知执行结果：" + result);
            } catch (Exception e) {
                log.info("BDC->YCSL：【" + noticeBody.getWsywh() + "】接入不动产办件节点通知模块,“节点通知处理线程”捕获到执行异常");
                log.error("BDC->YCSL：【" + noticeBody.getWsywh() + "】接入不动产办件节点通知模块,“节点通知处理线程”出现未知异常：" + ErrorDealUtil.getErrorInfo(e));
                //这里做异常处理(分支线程产生的)
                e = ErrorDealUtil.OnlineErrorTrans(e);
                //调用回写异常信息(通知类异常)进Rec（暂未完成）

            } finally {
                executor.shutdown();
            }
        }
    }
}
