package com.springboot.rest.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.PersonnelUnitEntity;
import com.springboot.entity.chenbin.personnel.req.PersonnelUnitReqEntity;
import com.springboot.service.chenbin.OuterInterfaceHandleService;
import com.springboot.util.chenbin.ErrorDealUtil;
import feign.Param;
import feign.codec.DecodeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "其它外部接口api")
@RestController
@Scope("prototype")
public class OuterInterfaceHandleRest {

    @Autowired
    private OuterInterfaceHandleService outerIntfService;



    @RequestMapping(value = "/getContractRecordxx",method = RequestMethod.POST)
    @ApiOperation("交易合同备案号获取")
    public ObjectRestResponse<Object> getContractRecordxx(@RequestBody Map<String,String> params){
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return rv.data(outerIntfService.getContractRecordxx(params));
    }


    @RequestMapping(value = "/checkPersonnelUnit",method = RequestMethod.POST)
    public ObjectRestResponse<Object> checkPersonnelUnit(@RequestBody PersonnelUnitReqEntity personnelUnit){
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        try {
            rv.data(outerIntfService.getPersonnelUnits(personnelUnit));
        } catch (ZtgeoBizException e){
            e.printStackTrace();
            log.error("异常产生"+e);
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            if(StringUtils.isNotBlank(e.getMessage())) {
                if (e.getMessage().contains("connect timed out")) {
                    throw new ZtgeoBizException("接口连接超时");
                }
                if (e.getMessage().contains("404")) {
                    throw new ZtgeoBizException("请求地址错误");
                }
            }
            if(e.getClass().equals(DecodeException.class)){
                throw new ZtgeoBizException("公安接口调用失败");
            }
            log.error("异常产生"+ ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("接口请求出现未知异常，请联系管理员");
        }
        return rv;
    }

    @RequestMapping(value = "/exchangePersonnelUnit",method = RequestMethod.POST)
    public ObjectRestResponse<List<PersonnelUnitEntity>> exchangePersonnelUnit(@RequestBody PersonnelUnitReqEntity personnelUnit){
        return null;
    }

    @RequestMapping(value = "/checkMarry2",method = RequestMethod.POST)
    public ObjectRestResponse<PersonnelUnitEntity> checkMarry2(@RequestBody PersonnelUnitReqEntity personnelUnit){
        return null;
    }
}
