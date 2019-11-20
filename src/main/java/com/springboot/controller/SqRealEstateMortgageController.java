package com.springboot.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.AnonymousInnerComponent;
import com.springboot.component.BdcFTPDownloadComponent;
import com.springboot.component.RealEstateMortgageComponent;
import com.springboot.component.SqRealEstateMortgageComponent;
import com.springboot.component.chenbin.file.ToFTPUploadComponent;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.ParamEntity;
import com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.MortgageRegistrationReqVo;
import com.springboot.entity.chenbin.personnel.other.bank.business.revok.RevokeRegistrationReqVo;
import com.springboot.entity.chenbin.personnel.other.bank.business.revok.RevokeRegistrationRespVo;
import com.springboot.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Api(tags = "宿迁不动产api")
@RestController
@Scope("prototype")
public class SqRealEstateMortgageController {

    @Autowired
    private SqRealEstateMortgageComponent sqRealEstateMortgageComponent;
    @Autowired
    private RealEstateMortgageComponent realEstateMortgageComponent;

    @RequestMapping(value = "/sqTransactionContract", method = RequestMethod.POST)
    @ApiOperation("二手房交易合同信息处理")
    public ObjectRestResponse sqTransactionContract(@RequestBody ParamEntity paramEntity)throws IOException {
        return sqRealEstateMortgageComponent.sqTransactionContract(paramEntity);
    }


    @RequestMapping(value = "/sendTransferRegister", method = RequestMethod.POST)
    @ApiOperation("二手房转移登记发送登记局收件")
    public ObjectRestResponse sendTransferRegister(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws Exception {
        return realEstateMortgageComponent.sendTransferRegister(commonInterfaceAttributer);
    }

    @RequestMapping(value = "/sendTransferMortgage", method = RequestMethod.POST)
    @ApiOperation("二手房转移登记及抵押发送登记局收件")
    public ObjectRestResponse sendTransferMortgage(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws Exception {
        return realEstateMortgageComponent.sendTransferMortgage(commonInterfaceAttributer);
    }

    @RequestMapping(value = "/sqTaxation", method = RequestMethod.POST)
    @ApiOperation("地税信息处理")
    public ObjectRestResponse sqTaxation(@RequestParam("htbh") String htbh) throws Exception{
        return sqRealEstateMortgageComponent.sqTaxation(htbh);
    }

    @RequestMapping(value = "/sqBdcdydj", method = RequestMethod.POST)
    @ApiOperation("不动产抵押登记,预告及预告抵押通知接口")
    public void sqBdcdydj(@RequestBody MortgageRegistrationReqVo mortgageRegistrationReqVo, OutputStream outputStream){
         sqRealEstateMortgageComponent.sqJgdyjk(mortgageRegistrationReqVo,outputStream);
    }


    @RequestMapping(value = "/sqBdczxdj", method = RequestMethod.POST)
    @ApiOperation("抵押注销通知接口")
    public void sqBdczxdj(@RequestBody RevokeRegistrationReqVo revokeRegistrationRespVo,OutputStream outputStream){
        sqRealEstateMortgageComponent.sqJgdyzx(revokeRegistrationRespVo,outputStream);
    }

}
