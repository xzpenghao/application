package com.springboot.rest.newPlat.transInner.penghao;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.service.newPlat.chenbin.BdcInteractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * @Author Peng Hao
 * @Date 2020/8/10 19:18
 * @Version 1.0
 */
@RestController
@Scope("prototype")
@Slf4j
public class BdcTransToInterRest {

    @Autowired
    private BdcInteractService bdcInteractService;


    /**
     * 描述：不动产抵押登记转内网接口
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[String]
     * 返回：String
     * 更新记录：更新人：{}，更新日期：{}
     */
    @RequestMapping(value = "creatProcMort2Bdc",method = RequestMethod.GET)
    public ObjectRestResponse<String> creatProcMort2Bdc(
            @RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer,
            @RequestParam(name = "checkAlready",required = false) String checkAlready
    ){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        try {
            rv.data(bdcInteractService.commonCreatNewPlatProc(commonInterfaceAttributer,checkAlready,"抵押登记(含两证)"));
        } catch (ParseException e) {
            log.error("二手房(及抵押办件)转内网办件传入数据异常，原始数据为："+commonInterfaceAttributer);
            throw new ZtgeoBizException("二手房转内网办件传入数据异常");
        }
        return rv;
    }

    /**
     * 描述：预告及预告抵押登记转内网接口
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[String]
     * 返回：String
     * 更新记录：更新人：{}，更新日期：{}
     */
    @RequestMapping(value = "creatProcDoubleYG2Bdc",method = RequestMethod.GET)
    public ObjectRestResponse<String> creatProcDoubleYG2Bdc(
            @RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer,
            @RequestParam(name = "checkAlready",required = false) String checkAlready
    ){
        return new ObjectRestResponse<>();
    }

    /**
     * 描述：抵押注销登记转内网接口
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[String]
     * 返回：String
     * 更新记录：更新人：{}，更新日期：{}
     */
    @RequestMapping(value = "creatProcMortCancel2Bdc",method = RequestMethod.GET)
    public ObjectRestResponse<String> creatProcMortCancel2Bdc(
            @RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer,
            @RequestParam(name = "checkAlready",required = false) String checkAlready
    ){
        return new ObjectRestResponse<>();
    }

}
