package com.springboot.rest.newPlat.transInner.penghao;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.service.newPlat.chenbin.BdcInteractService;
import io.swagger.annotations.Api;
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
@RequestMapping("api/bdc/interact")
@Scope("prototype")
@Slf4j
@Api(tags = "不动产(新平台)交互银行接口")
public class BdcTransToInterRest {

    @Autowired
    private BdcInteractService bdcInteractService;


  /**
   * 功能描述: 不动产抵押登记转内网接口<br>
   * 〈〉
   * @Param: [commonInterfaceAttributer, checkAlready]
   * @Return: com.github.wxiaoqi.security.common.msg.ObjectRestResponse<java.lang.String>
   * @Author: Peng Hao
   * @Date: 2020/8/11 15:03
   */
    @RequestMapping(value = "creatProcMortDyDjBdc",method = RequestMethod.GET)
    public ObjectRestResponse<String> creatProcMortDyDjBdc(
            @RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer,
            @RequestParam(name = "checkAlready",required = false) String checkAlready
    ){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        try {
            rv.data(bdcInteractService.commonCreatNewPlatProc(commonInterfaceAttributer,checkAlready,Msgagger.BDCDYDJ));
        } catch (ParseException e) {
            log.error("不动产抵押登记传入数据异常，原始数据为："+commonInterfaceAttributer);
            throw new ZtgeoBizException("不动产抵押登记内网办件传入数据异常");
        }
        return rv;
    }

    /**
     * 功能描述: 预告及预告抵押登记转内网接口<br>
     * 〈〉
     * @Param: [commonInterfaceAttributer, checkAlready]
     * @Return: com.github.wxiaoqi.security.common.msg.ObjectRestResponse<java.lang.String>
     * @Author: Peng Hao
     * @Date: 2020/8/11 15:03
     */
    @RequestMapping(value = "creatProcYgDyBdc",method = RequestMethod.GET)
    public ObjectRestResponse<String> creatProcYgDyBdc(
            @RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer,
            @RequestParam(name = "checkAlready",required = false) String checkAlready
    ){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        try {
            rv.data(bdcInteractService.commonCreatNewPlatProc(commonInterfaceAttributer,checkAlready,Msgagger.YGJYGDY));
        } catch (ParseException e) {
            log.error("不动产抵押登记传入数据异常，原始数据为："+commonInterfaceAttributer);
            throw new ZtgeoBizException("不动产抵押登记内网办件传入数据异常");
        }
        return rv;
    }

    /**
     * 功能描述:抵押注销登记转内网接口
     * 〈〉
     * @Param: [commonInterfaceAttributer, checkAlready]
     * @Return: com.github.wxiaoqi.security.common.msg.ObjectRestResponse<java.lang.String>
     * @Author: Peng Hao
     * @Date: 2020/8/11 15:03
     */
    @RequestMapping(value = "creatProcMortZxBdc",method = RequestMethod.GET)
    public ObjectRestResponse<String> creatProcMortZxBdc(
            @RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer,
            @RequestParam(name = "checkAlready",required = false) String checkAlready
    ){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        try {
            rv.data(bdcInteractService.commonCreatNewPlatProc(commonInterfaceAttributer,checkAlready,Msgagger.DYZXDJ));
        } catch (ParseException e) {
            log.error("不动产抵押注销传入数据异常，原始数据为："+commonInterfaceAttributer);
            throw new ZtgeoBizException("不动产抵押注销内网办件传入数据异常");
        }
        return rv;
    }

}
