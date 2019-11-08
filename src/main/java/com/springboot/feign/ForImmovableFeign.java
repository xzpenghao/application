package com.springboot.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "immovableFeign", url = "http://10.3.2.147:23335/")
public interface ForImmovableFeign {

    @RequestMapping(value="api/services/app/BdcQuery/GetBdcInfoByZL",method = RequestMethod.POST,produces = "application/json",consumes = "application/json")
    List<JSONObject> getBdcInfoByZL(Map<String,Object> map);

    @RequestMapping(value="api/services/app/BdcQuery/GetBdcInfoByBDCZH",method = RequestMethod.GET,produces = "application/json")
    List<JSONObject> getBdcInfoByZH(
            @RequestParam("BDCZH") String BDCZH,
            @RequestParam(value = "obligeeName",required = false) String obligeeName,
            @RequestParam(value = "obligeeId",required = false) String obligeeId
    );
}
