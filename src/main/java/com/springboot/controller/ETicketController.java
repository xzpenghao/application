package com.springboot.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.service.shike.ETicketService;
import com.springboot.vo.TaxAttachment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

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
    public ObjectRestResponse ETicketQuery1(@RequestParam List<String> receiptNumbers) {
        List<TaxAttachment> taxAttachments = new LinkedList<>();
        TaxAttachment taxAttachment = new TaxAttachment();
        taxAttachment.setSqbh("123456");
        TaxAttachment.ETax eTax = new TaxAttachment.ETax();
        eTax.setDzsphm("abcde");
        eTax.setBase64("");

        List<TaxAttachment.ETax> dzsps = new LinkedList<>();
        dzsps.add(eTax);
        dzsps.add(eTax);
        taxAttachment.setDzsps(dzsps);

        taxAttachments.add(taxAttachment);
        taxAttachments.add(taxAttachment);

        return new ObjectRestResponse<>().data(taxAttachments);
    }

}
