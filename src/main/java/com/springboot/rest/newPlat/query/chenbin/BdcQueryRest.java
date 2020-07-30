package com.springboot.rest.newPlat.query.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.other.paph.PaphEntity;
import com.springboot.entity.chenbin.personnel.req.PaphReqEntity;
import com.springboot.popj.pub_data.SJ_Info_Bdcqlxgxx;
import com.springboot.popj.pub_data.Sj_Info_Bdcdyxgxx;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.popj.warrant.ParametricData2;
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

import java.util.List;

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

    /**
     * 描述：根据产权证号+权利人名称，获取带它项权信息的权证数据
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：ParametricData
     * 返回：ObjectRestResponse
     * 更新记录：更新人：{}，更新日期：{}
    */
    @RequestMapping(value = "getBdcQlInfoWithItsRights",method = RequestMethod.POST)
    public ObjectRestResponse<List<SJ_Info_Bdcqlxgxx>> getBdcQlInfoWithItsRights(@RequestBody ParametricData parametricData){
        try {
            return new ObjectRestResponse().data(bdcQueryService.queryQzxxWithItsRights(parametricData));
        } catch (Exception e){
            log.error("【产权证书查询】--> 查询时出现异常，记录原始异常信息："+ ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("查询时出现异常");
        }
    }

    /**
     * 描述：根据抵押证明号+抵押人名称，获取抵押证明信息数据
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：ParametricData2
     * 返回：ObjectRestResponse
     * 更新记录：更新人：{}，更新日期：{}
    */
    @RequestMapping(value = "getBdcDyInfoByZmhAndDyr",method = RequestMethod.POST)
    public ObjectRestResponse<List<Sj_Info_Bdcdyxgxx>> getBdcDyInfoByZmhAndDyr(@RequestBody ParametricData2 parametricData){
        try {
            return new ObjectRestResponse().data(bdcQueryService.queryDyxxByZmhAndDyr(parametricData));
        } catch (Exception e){
            log.error("【抵押证明查询】--> 查询时出现异常，记录原始异常信息："+ ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("查询时出现异常");
        }
    }

    /**
     * 描述：平安普惠贷前查询
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[paph]
     * 返回：List<PaphEntity>
     * 更新记录：更新人：{}，更新日期：{}
     */
    @RequestMapping(value = "queryPaphMortBefore",method = RequestMethod.POST)
    public ObjectRestResponse<List<PaphEntity>> getPaphMortBefore(@RequestBody PaphReqEntity paph){
        try {
            return new ObjectRestResponse().data(bdcQueryService.queryPaphMort(paph,"before"));
        } catch (Exception e){
            log.error("【银行贷前查询】--> 查询时出现异常，记录原始异常信息："+ ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("查询时出现异常");
        }
    }

    /**
     * 描述：平安普惠贷后查询
     * 作者：chenb
     * 日期：2020/7/30/030
     * 参数：[paph]
     * 返回：List<PaphEntity>
     * 更新记录：更新人：{}，更新日期：{}
     */
    @RequestMapping(value = "queryPaphMortAfter",method = RequestMethod.POST)
    public ObjectRestResponse<List<PaphEntity>> getPaphMortAfter(@RequestBody PaphReqEntity paph){
        try {
            return new ObjectRestResponse().data(bdcQueryService.queryPaphMort(paph,"after"));
        } catch (Exception e){
            log.error("【银行贷后查询】--> 查询时出现异常，记录原始异常信息："+ ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("查询时出现异常");
        }
    }

}
