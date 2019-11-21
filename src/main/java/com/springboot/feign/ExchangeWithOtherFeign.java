package com.springboot.feign;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "otherFeign", url = "${other.url}")
public interface ExchangeWithOtherFeign {

    @RequestMapping(value = "api/test/testTax",method = RequestMethod.POST)
    public ObjectRestResponse<String> testTax(Map<String,Object> taxParamBody);

//    @RequestMapping(value = "api/test/testTra",method = RequestMethod.POST,produces = "application/json",consumes = "application/json",headers = {"api_id=bXREUCzt","from_user=0b6c220cdfc54288b6630eb1a7fa612f"})
    @RequestMapping(value = "sqservice/bdc/getOneAcceptInfo",method = RequestMethod.POST,produces = "application/json",consumes = "application/json",headers = {"api_id=bXREUCzt","from_user=0b6c220cdfc54288b6630eb1a7fa612f"})
    public ObjectRestResponse<String> testTra(Map<String,Object> TraParamBody);

}
