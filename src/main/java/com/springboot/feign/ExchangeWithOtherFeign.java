package com.springboot.feign;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.entity.chenbin.personnel.tax.TaxParamBody;
import com.springboot.entity.chenbin.personnel.tra.TraParamBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "otherFeign", url = "${other.url}")
public interface ExchangeWithOtherFeign {

    @RequestMapping(value = "api/test/testTax",method = RequestMethod.POST)
    public ObjectRestResponse<String> testTax(TaxParamBody taxParamBody);
    @RequestMapping(value = "api/test/testTra",method = RequestMethod.POST)
    public ObjectRestResponse<String> testTra(TraParamBody TraParamBody);

}
