package com.springboot.feign;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.popj.pub_data.SJ_Exception_Record;
import com.springboot.popj.pub_data.SJ_Sdqg_Send_Result;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.popj.register.JwtAuthenticationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "backFeign", url = "${ycsl.url}")
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

    //拉取有效数据
    @RequestMapping(value = "/DealRecieveFromOuter7", method = RequestMethod.GET)
    ObjectRestResponse<SJ_Sjsq> DealRecieveFromOuter7(@RequestHeader(name = "Authorization") String token, @RequestParam("sqbh") String sqbh, @RequestParam("taskId") String taskId);

    //拉取访问异常信息
    @RequestMapping(value = "/DealRecieveFromOuter8", method = RequestMethod.POST,consumes = "application/json")
    ObjectRestResponse<List<SJ_Exception_Record>> DealRecieveFromOuter8(@RequestHeader(name = "Authorization") String token,SJ_Exception_Record exc);

    //插入访问异常信息
    @RequestMapping(value = "/DealRecieveFromOuter9", method = RequestMethod.POST,consumes = "application/json")
    ObjectRestResponse<String> DealRecieveFromOuter9(@RequestHeader(name = "Authorization") String token,SJ_Exception_Record exc);

    //异常外部处理
    @RequestMapping(value = "/DealRecieveFromOuter10", method = RequestMethod.POST,consumes = "application/json")
    ObjectRestResponse<String> DealRecieveFromOuter10(@RequestHeader(name = "Authorization") String token,SJ_Exception_Record exc);

    @RequestMapping(value = "/DealRecieveFromOuter11", method = RequestMethod.POST,consumes = "application/json")
    ObjectRestResponse<String> DealRecieveFromOuter11(@RequestHeader(name = "Authorization") String token, SJ_Sdqg_Send_Result result);

    @RequestMapping(value = "api/test/test",method = RequestMethod.POST)
    ObjectRestResponse<Object> test(@RequestParam("paramsss") String paramsss);
}
