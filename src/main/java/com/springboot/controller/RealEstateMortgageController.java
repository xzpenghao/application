package com.springboot.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.AnonymousInnerComponent;
import com.springboot.component.RealEstateMortgageComponent;
import com.springboot.popj.GetReceiving;
import com.springboot.popj.warrant.ParametricData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
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
    @Autowired
    private AnonymousInnerComponent anonymousInner;






    @RequestMapping(value = "/getRealEstateMortgage", method = RequestMethod.POST)
    @ApiOperation("不动产抵押信息")
    public ObjectRestResponse getRealEstateMortgage(@RequestParam("dyzmh") String dyzmh,@RequestParam(value = "qlrmc",required = false) String qlrmc) throws Exception {
        System.out.println(dyzmh);
        return  realEstateMortgageComponent.getRealEstateMortgage(dyzmh,qlrmc,false);
    }

    @RequestMapping(value = "/getRealPropertyCertificate", method = RequestMethod.POST)
    @ApiOperation("不动产权属信息")
    public ObjectRestResponse getRealPropertyCertificate(@RequestBody ParametricData parametricData) throws Exception {
        return  realEstateMortgageComponent.getRealPropertyCertificate(parametricData);
    }


    @RequestMapping(value = "/sendRegistrationMortgageRevocation", method = RequestMethod.POST)
    @ApiOperation("发送数据到登记局进行审批操作返回受理编号")
    public ObjectRestResponse sendRegistrationMortgageRevocation(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws ParseException {
        return  realEstateMortgageComponent.sendRegistrationMortgageRevocation(commonInterfaceAttributer);
    }

    @RequestMapping(value = "/getReceiving",method =RequestMethod.POST)
    @ApiOperation(value = "登记局返回成功数据")
    public void getReceiving(@RequestBody GetReceiving getReceiving, OutputStream resp) throws  IOException{
         anonymousInner.GetReceiving(getReceiving,resp);
    }

    @RequestMapping(value = "/getMortgageCancellation",method =RequestMethod.POST)
    @ApiOperation(value = "不动产预告证明号")
    public ObjectRestResponse getMortgageCancellation(String ygzmh) throws  Exception{
         return   realEstateMortgageComponent.getMortgageCancellation(ygzmh);
    }


}
