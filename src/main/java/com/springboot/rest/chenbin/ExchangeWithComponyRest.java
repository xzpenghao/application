package com.springboot.rest.chenbin;

import com.springboot.entity.chenbin.personnel.req.DLReqEntity;
import com.springboot.entity.chenbin.personnel.req.ReqSendForWEGEntity;
import com.springboot.entity.chenbin.personnel.resp.DLReturnUnitEntity;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.service.chenbin.impl.ExchangeWithComponyService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author chenb
 * @version 2020/4/29/029
 * description：
 */
@Slf4j
@Api(tags = "与水电气等公司交互的api")
@RestController
@Scope("prototype")
public class ExchangeWithComponyRest {
    @Autowired
    private ExchangeWithComponyService exc2Comp;

    @RequestMapping(value = "exchange2Ele",method = RequestMethod.POST)
    public OtherResponseEntity<DLReturnUnitEntity> exchange2Tax(@RequestBody DLReqEntity dlcs){
        return exc2Comp.exchangeWithPowerCompany(dlcs);
    }

    @RequestMapping(value = "exchangeWithWEGComponies",method = RequestMethod.POST)
    public Object exchangeWithWEGComponies(@RequestBody ReqSendForWEGEntity transferEntity) throws IOException {
        log.info("二手房水电气发送电部门通知");
        return exc2Comp.exchangeWithWEGComponies(transferEntity,null);
    }
}
