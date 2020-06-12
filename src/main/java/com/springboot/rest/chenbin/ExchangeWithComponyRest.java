package com.springboot.rest.chenbin;

import com.springboot.entity.chenbin.personnel.req.SDQReqEntity;
import com.springboot.entity.chenbin.personnel.req.ReqSendForWEGEntity;
import com.springboot.entity.chenbin.personnel.resp.SDQReturnUnitEntity;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public OtherResponseEntity<SDQReturnUnitEntity> exchange2Tax(@RequestBody SDQReqEntity dlcs){
        return exc2Comp.exchangeWithPowerCompany(dlcs);
    }

    @RequestMapping(value = "exchangeWithWEGComponies",method = RequestMethod.POST)
    public void exchangeWithWEGComponies(@RequestBody ReqSendForWEGEntity transferEntity, HttpServletRequest req, HttpServletResponse resp){
        log.info("YCSL->SDQG：二手房水电气发送电部门通知");
        exc2Comp.exchangeWithWEGComponies(req.getHeader("Authorization"),transferEntity,resp);
    }
}
