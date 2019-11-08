package com.springboot.feign;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.popj.register.JwtAuthenticationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "backFeign", url = "http://localhost:7780/")
public interface OuterBackFeign {

    //获取token
    @RequestMapping(value = "jwt/token", method = RequestMethod.POST)
    ObjectRestResponse<String> getToken(JwtAuthenticationRequest authenticationRequest);

    //同步受理号
    @RequestMapping(value = "api/biz/RecService/DealRecieveFromOuter1",method = RequestMethod.POST,consumes = "application/json")
    ObjectRestResponse<String> dealRecieveFromOuter1(@RequestHeader(name = "Authorization") String token, Map<String, String> sjsq);

    //提交各个步骤
    @RequestMapping(value = "api/biz/RecService/DealRecieveFromOuter2", method = RequestMethod.POST,consumes = "application/json")
    ObjectRestResponse<String> DealRecieveFromOuter2(@RequestHeader(name = "Authorization") String token, Map<String, String> sjsq);

    //创建流程
    @RequestMapping(value = "api/biz/RecService/DealRecieveFromOuter5", method = RequestMethod.POST,consumes = "application/json")
    ObjectRestResponse<Object> DealRecieveFromOuter5(@RequestHeader(name = "Authorization") String token, Map<String, String> sjsq);
}
