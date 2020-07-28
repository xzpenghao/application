package com.springboot.rest.newPlat.query.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.service.newPlat.chenbin.BdcQueryService;
import com.springboot.util.chenbin.ErrorDealUtil;
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
 * @version 2020/7/27/027
 * description：新平台不动产查询请求接口
 */
@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("api/bdc/query")
@Api(tags = "不动产(新平台)数据查询接口")
public class BdcQueryRest {

    @Autowired
    private BdcQueryService bdcQueryService;

    @RequestMapping(value = "getBdcQlInfoWithItsRights",method = RequestMethod.POST)
    public ObjectRestResponse getBdcQlInfoWithItsRights(@RequestBody ParametricData parametricData){
        try {
            return new ObjectRestResponse().data(bdcQueryService.queryQzxxWithItsRights(parametricData));
        } catch (Exception e){
            log.error("【产权证书查询】--> 查询时出现异常，记录原始异常信息："+ ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("查询时出现异常");
        }
    }
}
