package com.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.service.shike.ETicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author sk
 * @version 2020/1/16
 */
@Slf4j
@RestController
@RequestMapping("ETicket")
@Api(tags = {"电子税票相关API"})
public class ETicketController {
    @Autowired
    private ETicketService eTicketService;

    @RequestMapping(value = "/ETicket", method = RequestMethod.POST)
    @ApiOperation("电子税票反馈")
    public ObjectRestResponse ETicketQuery(@RequestParam List<String> receiptNumbers,@RequestParam String ftpFlag) {
        return new ObjectRestResponse().data(eTicketService.ETicketQuery(receiptNumbers,ftpFlag));
    }

    @RequestMapping(value = "/morkData", method = RequestMethod.POST)
    @ApiOperation("电子税票反馈")
    public String ETicketQuery1(@RequestBody Map<String,Object> receiptNumbers) {
        log.info(JSON.toJSONString(receiptNumbers));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n" +
                "    \"messge\": \"操作成功\",\n" +
                "    \"data\": \"[{\\\"sqbh\\\":\\\"YCSL-202003300006\\\",\\\"dzsps\\\":[{\\\"dzsphm\\\":\\\"332131200300080725\\\",\\\"tgdw\\\":\\\"税务\\\",\\\"imgBase64\\\":\\\"\\\"},{\\\"dzsphm\\\":\\\"332131200300094424\\\",\\\"tgdw\\\":\\\"税务\\\",\\\"imgBase64\\\":\\\"\\\"}]}]\",\n" +
                "    \"status\": \"200\"\n" +
                "}");
        return stringBuilder.toString();
    }

}
