package com.springboot.controller;

import com.github.wxiaoqi.security.common.exception.base.BusinessException;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.service.shike.PublicInspectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("PublicInspect")
@Api(tags = {"公检法相关API"})
public class PublicInspectController {
    @Autowired
    private PublicInspectService publicInspectService;

    @RequestMapping(value = "/JudicialQuery", method = RequestMethod.POST)
    @ApiOperation("司法查询反馈")
    public ObjectRestResponse JudicialQuery(String bdczh,String type) {
        if (StringUtils.isBlank(bdczh)||StringUtils.isBlank(type)){
            throw new BusinessException("参数格式错误");
        }
        return new ObjectRestResponse<>().data(publicInspectService.JudicialQuery(bdczh,type));
    }


}
