package com.springboot.feign;

import com.alibaba.fastjson.JSONObject;
import com.springboot.entity.chenbin.personnel.bdc.SynNewEcertEntity;
import com.springboot.entity.chenbin.personnel.resp.BDCInterfaceResp;
import com.springboot.popj.registration.RegistrationBureau;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "immovableFeign", url = "${bdccx.url}")
public interface ForImmovableFeign {

    @RequestMapping(value="api/services/app/BdcQuery/GetBdcInfoByZL",method = RequestMethod.POST,produces = "application/json",consumes = "application/json")
    List<JSONObject> getBdcInfoByZL(Map<String,Object> map);

    @RequestMapping(value="api/services/app/BdcQuery/GetBdcInfoByBDCZH",method = RequestMethod.GET,produces = "application/json")
    List<JSONObject> getBdcInfoByZH(
            @RequestParam("BDCZH") String BDCZH,
            @RequestParam(value = "obligeeName",required = false) String obligeeName,
            @RequestParam(value = "obligeeId",required = false) String obligeeId
    );

    @RequestMapping(value = "api/services/app/BdcWorkFlow/CreateFlow",method = RequestMethod.POST,produces = "application/json",consumes = "application/json")
    Map<String,Object> createFlow(RegistrationBureau registrationBureau);

    @RequestMapping(value = "api/services/app/bdcWorkFlow/GetProcessUsers",method = RequestMethod.POST,produces = "application/json",consumes = "application/json")
    Map<String,Object> postProcessUsers(Map<String,String> pMap);

    //徐州
//    @RequestMapping(value = "api/services/app/bdcElecLicense/GetAttachList4XZ",method = RequestMethod.POST,produces = "application/json",consumes = "application/json")
//    BDCInterfaceResp<List<SynNewEcertEntity>> postECerts(Map<String,List<String>> req);

    //宿迁
    @RequestMapping(value = "api/services/app/bdcElecLicense/GetAttachList4SQ",method = RequestMethod.POST,produces = "application/json",consumes = "application/json")
    BDCInterfaceResp<List<SynNewEcertEntity>> postECerts(Map<String,List<String>> req);
}
