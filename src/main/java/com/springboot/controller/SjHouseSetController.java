package com.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.github.wxiaoqi.security.common.msg.BaseResponse;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.entity.SJHouseSet;
import com.springboot.service.shike.SjHouseSetService;
import com.springboot.vo.Obligee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author sk
 * @version 2020/5/21
 * description：房屋套次 控制层
 */
@Slf4j
@RestController
@RequestMapping("sjHouseSet")
@Api(tags = {"房屋套次信息相关API"})
public class SjHouseSetController {

    @Autowired
    private SjHouseSetService sjHouseSetService;

    @RequestMapping(value = "/queryBdcHouseSet", method = RequestMethod.POST)
    @ApiOperation("获取不动产房屋套次信息")
    public ObjectRestResponse queryBdcHouseSetByObligeeList(@RequestBody List<Obligee> obligeeList){
        return new ObjectRestResponse<List<SJHouseSet>>().data(sjHouseSetService.queryBdcHouseSetByObligeeList(obligeeList));
    }

    @RequestMapping(value = "/queryZjHouseSet", method = RequestMethod.POST)
    @ApiOperation("获取住建房屋套次信息")
    public ObjectRestResponse queryZjHouseSetByObligeeList(@RequestBody List<Obligee> obligeeList){
        return new ObjectRestResponse<List<SJHouseSet>>().data(sjHouseSetService.queryZjHouseSetByObligeeList(obligeeList));
    }

    @RequestMapping(value = "/taxPush", method = RequestMethod.POST)
    @ApiOperation("税务推送")
    public BaseResponse taxPush(@RequestBody Map<String,String> taxPushMap){
        return sjHouseSetService.taxPush(taxPushMap);
    }

    @RequestMapping(value = "/mockTax", method = RequestMethod.POST)
    @ApiOperation("mock税务响应")
    public ObjectRestResponse mockTax(@RequestBody String data){
        log.info("mock税务响应:{}", JSON.toJSONString(data));
        ObjectRestResponse response = new ObjectRestResponse();
        response.setMessage("操作成功");
        return response;
    }
}
