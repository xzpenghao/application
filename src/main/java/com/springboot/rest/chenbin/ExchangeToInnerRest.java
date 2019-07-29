package com.springboot.rest.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.service.chenbin.ExchangeToInnerService;
import com.springboot.util.chenbin.ErrorDealUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@Api(tags = "不动产apiII")
@RestController
@Scope("prototype")
public class ExchangeToInnerRest {
    @Autowired
    private ExchangeToInnerService exchangeToInnerService;

    public ObjectRestResponse<String> YGYD2Inner(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        try{
            rv.data(exchangeToInnerService.dealYGYD2Inner(commonInterfaceAttributer));
        }catch (ParseException e1){
            log.error(ErrorDealUtil.getErrorInfo(e1));
            rv.setStatus(20500);
            rv.setData("转内网传入的数据格式不正确");
        }catch (ZtgeoBizException e2){
            log.error(ErrorDealUtil.getErrorInfo(e2));
            rv.setStatus(20500);
            rv.setData(e2.getMessage());
        }catch (Exception e3){
            log.error(ErrorDealUtil.getErrorInfo(e3));
            rv.setStatus(20500);
            rv.setData("转内网出现其它运行时异常，请排查！");
        }
        return rv;
    }
}
