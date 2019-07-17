package com.springboot.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.RealEstateMortgageComponent;
import com.springboot.popj.warrant.ParametricData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@Slf4j
@Api(tags = "不动产api")
@RestController
@Scope("prototype")
public class RealEstateMortgageController {

    @Value("${httpclient.seam}")
    private String seam;//接口
    @Value("${httpclient.ip}")
    private String ip;
    @Value("${glHouseBuyer.obligeeQlr}")
    private String obligeeQlr;
    @Autowired
    private RealEstateMortgageComponent realEstateMortgageComponent;




    @RequestMapping(value = "/getRealEstateMortgage", method = RequestMethod.POST)
    @ApiOperation("不动产抵押信息")
    public ObjectRestResponse getRealEstateMortgage(@RequestParam("dyzmh") String dyzmh) throws IOException {
        System.out.println(dyzmh);
        return  realEstateMortgageComponent.getRealEstateMortgage(dyzmh);
    }

    @RequestMapping(value = "/getRealPropertyCertificate", method = RequestMethod.POST)
    @ApiOperation("不动产权属信息")
    public ObjectRestResponse getRealPropertyCertificate(@RequestBody ParametricData parametricData) throws IOException {
        return  realEstateMortgageComponent.getRealPropertyCertificate(parametricData);
    }


    @RequestMapping(value = "/sendRegistrationMortgageRevocation", method = RequestMethod.POST)
    @ApiOperation("发送数据到登记局进行审批操作返回受理编号")
    public ObjectRestResponse sendRegistrationMortgageRevocation(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws ParseException {
        return  realEstateMortgageComponent.sendRegistrationMortgageRevocation(commonInterfaceAttributer);
    }


}
