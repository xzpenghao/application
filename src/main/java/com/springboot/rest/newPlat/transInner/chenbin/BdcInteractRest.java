package com.springboot.rest.newPlat.transInner.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.entity.newPlat.transInner.req.BdcNoticeReq;
import com.springboot.service.newPlat.chenbin.BdcInteractService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenb
 * @version 2020/7/30/030
 * description：新平台不动产交互接口
 */
@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("api/interact")
@Api(tags = "不动产(新平台)交互接口")
public class BdcInteractRest {

    @Autowired
    private BdcInteractService bdcInteractService;

    /**
     * 描述：
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[noticeBody]
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
     */
    @RequestMapping(value = "notice",method = RequestMethod.POST)
    public ObjectRestResponse<String> noticeMe(@RequestBody BdcNoticeReq noticeBody){
        return new ObjectRestResponse<>();
    }

    @RequestMapping(value = "creatOneAcceptProc",method = RequestMethod.POST)
    public ObjectRestResponse<String> creatOneAcceptProc(@RequestBody BdcNoticeReq noticeBody){
        return new ObjectRestResponse<>();
    }

    @RequestMapping(value = "creatProcSecTra2BdcWithoutDY",method = RequestMethod.POST)
    public ObjectRestResponse<String> creatProcSecTra2BdcWithoutDY(@RequestBody BdcNoticeReq noticeBody){
        return new ObjectRestResponse<>();
    }

    @RequestMapping(value = "creatProcSecTra2BdcWithDY",method = RequestMethod.POST)
    public ObjectRestResponse<String> creatProcSecTra2BdcWithDY(@RequestBody BdcNoticeReq noticeBody){
        return new ObjectRestResponse<>();
    }
}
