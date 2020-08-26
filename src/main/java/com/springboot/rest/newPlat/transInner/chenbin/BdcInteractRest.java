package com.springboot.rest.newPlat.transInner.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.emm.KEY_NOTICE_CODE_Enums;
import com.springboot.entity.newPlat.transInner.req.BdcNoticeReq;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.service.newPlat.chenbin.BdcInteractService;
import com.springboot.util.chenbin.ErrorDealUtil;
import com.springboot.util.newPlatBizUtil.DicConvertUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
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
    public void noticeMe(@RequestBody BdcNoticeReq noticeBody, HttpServletRequest request, HttpServletResponse resp) throws IOException {
        String bizStamp = "【" + new Date().getTime() + "】"+" 批次节点通知";
        try {
            log.debug("BDC->YCSL：接入不动产办件节点通知模块{"+bizStamp+"},数据解析："+ JSONObject.toJSONString(noticeBody));
            if(noticeBody==null){
                throw new ZtgeoBizException("通知失败，参数校验失败，传入参数为空");
            }
            noticeBody.checkSelfStandard();
            bdcInteractService.noticeMe(noticeBody,request.getRequestURI(),resp);
            log.info("BDC->YCSL：接入不动产办件节点通知模块{"+bizStamp+"},通知节点类型："+ DicConvertUtil.getKeyWordByCode(noticeBody.getJdbs(), KEY_NOTICE_CODE_Enums.values()));
        } catch (ZtgeoBizException e){
            throw new ZtgeoBizException("通知失败，异常信息为："+e.getMessage());
        } catch (Exception e) {
            log.error("不动产办件节点通知异常，异常信息："+ ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("通知失败，通知出现不可预知的异常，联系管理员解决");
        }
    }

    /**
     * 描述：二手房转移登记转内网接口
     *          commonInterfaceAttributer -- 通用参数
     *          checkAlready -- 验证已经生成的标识
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[String]
     * 返回：String
     * 更新记录：更新人：{}，更新日期：{}
     */
    @RequestMapping(value = "creatProcSecTra2BdcWithoutDY",method = RequestMethod.GET)
    public ObjectRestResponse<String> creatProcSecTra2BdcWithoutDY(
            @RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer,
            @RequestParam(name = "checkAlready",required = false) String checkAlready
    ) {
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        try {
            log.info("进入二手房转移登记【转内网】");
            rv.data(bdcInteractService.commonCreatNewPlatProc(commonInterfaceAttributer,checkAlready,Msgagger.ESFZYDJ));
        } catch (ParseException e) {
            log.error("二手房转内网办件传入数据异常，原始数据为："+commonInterfaceAttributer);
            throw new ZtgeoBizException("二手房转内网办件传入数据异常");
        }
        return rv;
    }

    /**
     * 描述：二手房转移及抵押登记转内网接口
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[String]
     * 返回：String
     * 更新记录：更新人：{}，更新日期：{}
     */
    @RequestMapping(value = "creatProcSecTra2BdcWithDY",method = RequestMethod.GET)
    public ObjectRestResponse<String> creatProcSecTra2BdcWithDY(
            @RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer,
            @RequestParam(name = "checkAlready",required = false) String checkAlready
    ){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        try {
            log.info("进入二手房转移及抵押登记【转内网】");
            rv.data(bdcInteractService.commonCreatNewPlatProc(commonInterfaceAttributer,checkAlready, Msgagger.ESFZYJDYDJ));
        } catch (ParseException e) {
            log.error("二手房(及抵押办件)转内网办件传入数据异常，原始数据为："+commonInterfaceAttributer);
            throw new ZtgeoBizException("二手房转内网办件传入数据异常");
        }
        return rv;
    }



    /**
     * 描述：一窗流程创建通用接口
     * 作者：chenb
     * 日期：2020/8/10
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    @RequestMapping(value = "creatOneAcceptProcCommon",method = RequestMethod.POST)
    public ObjectRestResponse<String> creatOneAcceptProc(@RequestBody BdcNoticeReq noticeBody){
        return new ObjectRestResponse<>();
    }

    /**
     * 描述：通过
     * 作者：chenb
     * 日期：2020/8/10
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    @RequestMapping(value = "creatOneAcceptProcByHistoryWord",method = RequestMethod.POST)
    public ObjectRestResponse<String> creatOneAcceptProcByHistoryWord(@RequestBody BdcNoticeReq noticeBody){
        return new ObjectRestResponse<>();
    }


}
