package com.springboot.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.entity.SJHouseSet;
import com.springboot.service.shike.SjHouseSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ObjectRestResponse queryBdcHouseSetByObligeeList(@RequestBody String obligeeList){
        return new ObjectRestResponse<List<SJHouseSet>>().data(sjHouseSetService.queryBdcHouseSetByObligeeList(obligeeList));
    }
}
