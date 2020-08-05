package com.springboot.rest.newPlat.transInner.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.emm.KEY_NOTICE_CODE;
import com.springboot.entity.newPlat.transInner.req.BdcNoticeReq;
import com.springboot.service.newPlat.chenbin.BdcInteractService;
import com.springboot.util.chenbin.ErrorDealUtil;
import com.springboot.util.newPlatBizUtil.DicConvertUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * @author chenb
 * @version 2020/7/30/030
 * description：新平台不动产交互接口
 */
@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("api/bdc/interact")
@Api(tags = "不动产(新平台)交互接口")
public class BdcInteractRest {

    @Autowired
    private BdcInteractService bdcInteractService;

    /**
     * 描述：通知接口模块
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[noticeBody]
     * 返回：通知的结果信息
     * 更新记录：更新人：{}，更新日期：{}
     */
    @RequestMapping(value = "notice",method = RequestMethod.POST)
    public void noticeMe(@RequestBody BdcNoticeReq noticeBody, HttpServletResponse resp) throws IOException {
        String bizStamp = "【" + new Date().getTime() + "】"+" 批次节点通知";
        try {
            log.debug("BDC->YCSL：接入不动产办件节点通知模块{"+bizStamp+"},数据解析："+ JSONObject.toJSONString(noticeBody));
            if(noticeBody==null){
                throw new ZtgeoBizException("通知失败，参数校验失败，传入参数为空");
            }
            noticeBody.checkSelfStandard();
            bdcInteractService.noticeMe(noticeBody,resp);
            log.info("BDC->YCSL：接入不动产办件节点通知模块{"+bizStamp+"},通知节点类型："+ DicConvertUtil.getKeyWordByCode(noticeBody.getJdbs(), KEY_NOTICE_CODE.values()));
        } catch (Exception e) {
            log.error("不动产办件节点通知异常，异常信息："+ ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("通知失败，通知出现不可预知的异常，联系管理员解决");
        }
    }

    @RequestMapping(value = "creatOneAcceptProcCommon",method = RequestMethod.POST)
    public ObjectRestResponse<String> creatOneAcceptProc(@RequestBody BdcNoticeReq noticeBody){
        return new ObjectRestResponse<>();
    }

    @RequestMapping(value = "creatOneAcceptProcByHistoryWord",method = RequestMethod.POST)
    public ObjectRestResponse<String> creatOneAcceptProcByHistoryWord(@RequestBody BdcNoticeReq noticeBody){
        return new ObjectRestResponse<>();
    }

    @RequestMapping(value = "creatProcSecTra2BdcWithoutDY",method = RequestMethod.POST)
    public ObjectRestResponse<String> creatProcSecTra2BdcWithoutDY(@RequestBody BdcNoticeReq noticeBody){
        return new ObjectRestResponse<>();
    }

    @RequestMapping(value = "creatProcSecTra2BdcWithDY",method = RequestMethod.POST)
    public ObjectRestResponse<String> creatProcSecTra2BdcWithDY(@RequestBody BdcNoticeReq noticeBody){
        return new ObjectRestResponse<>();
    }
}
