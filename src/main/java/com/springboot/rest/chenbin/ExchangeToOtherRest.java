package com.springboot.rest.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.other.web.RequestParamBody;
import com.springboot.service.chenbin.ExchangeToTaxService;
import com.springboot.service.chenbin.ExchangeToTransactionService;
import com.springboot.service.chenbin.ExchangeToWebService;
import com.springboot.util.chenbin.ErrorDealUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@Api(tags = "其它部门api")
@RestController
@Scope("prototype")
public class ExchangeToOtherRest {
    @Autowired
    private ExchangeToTaxService exc2Tax;
    @Autowired
    private ExchangeToTransactionService exc2Tran;
    @Autowired
    private ExchangeToWebService exc2Web;

    @RequestMapping(value = "exchange2Tax",method = RequestMethod.POST)
    public ObjectRestResponse<Object> exchange2Tax(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer){
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        try {
            //log.warn("契税转入，本次参数为：" + commonInterfaceAttributer);
            rv.data(exc2Tax.deal2Tax(commonInterfaceAttributer));
        } catch (ParseException e1) {
            log.error(ErrorDealUtil.getErrorInfo(e1));
            rv.setStatus(20500);
            rv.setData("转内网传入的数据格式不正确");
        } catch (ZtgeoBizException e2) {
            log.error(ErrorDealUtil.getErrorInfo(e2));
            rv.setStatus(20500);
            rv.setData(e2.getMessage());
        } catch (Exception e3) {
            log.error(ErrorDealUtil.getErrorInfo(e3));
            rv.setStatus(20500);
            rv.setData("转内网出现其它运行时异常，请排查！");
        }
        return rv;
    }

    @RequestMapping(value = "exchange2Tra",method = RequestMethod.POST)
    public ObjectRestResponse<Object> exchange2Tra(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer){
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        try {
            //log.warn("交易转入，本次参数为：" + commonInterfaceAttributer);
            rv.data(exc2Tran.deal2Tra(commonInterfaceAttributer));
        } catch (ParseException e1) {
            log.error(ErrorDealUtil.getErrorInfo(e1));
            rv.setStatus(20500);
            rv.setData("转内网传入的数据格式不正确");
        } catch (ZtgeoBizException e2) {
            log.error(ErrorDealUtil.getErrorInfo(e2));
            rv.setStatus(20500);
            rv.setData(e2.getMessage());
        } catch (Exception e3) {
            log.error(ErrorDealUtil.getErrorInfo(e3));
            rv.setStatus(20500);
            rv.setData("转内网出现其它运行时异常，请排查！");
        }
        return rv;
    }

    @RequestMapping(value = "web2ycslForSec",method = RequestMethod.POST)
    public ObjectRestResponse<Object> web2ycslForSecondTransfer(@RequestBody RequestParamBody paramBody){
        System.out.println("传入变量为："+ JSONObject.toJSONString(paramBody));
        ObjectRestResponse rv = new ObjectRestResponse<>();
        Object r = null;
        try {
            r = exc2Web.initWebSecReg(paramBody);
            rv.data(r);
        } catch (ZtgeoBizException e1){
            rv.setStatus(20500);
            rv.setMessage(e1.getMessage());
        } catch (Exception e2){
            rv.setStatus(20500);
            rv.setMessage("请求出现其它运行时异常，请联系管理员解决");
        }
        return rv;
    }
}
