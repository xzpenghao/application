package com.springboot.feign.newPlat;

import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.entity.newPlat.query.req.CqzsReq;
import com.springboot.entity.newPlat.query.req.DjzlReq;
import com.springboot.entity.newPlat.query.req.DyzmReq;
import com.springboot.entity.newPlat.query.req.DzzzReq;
import com.springboot.entity.newPlat.query.resp.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author chenb
 * @version 2020/7/27/027
 * description：不动产查询feign
 */
@FeignClient(name = "bdcQueryFeign", url = "${bdccx.url}")
public interface BdcQueryFeign {

    @RequestMapping(
            value = "${Feign.bdc.new_plat.query.djzlcx}",
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json;charset=UTF-8"
    )   //登记资料查询
    OtherResponseEntity<List<DjzlResponse>> djzlcx(DjzlReq djzlReq);

    @RequestMapping(
            value = "${Feign.bdc.new_plat.query.cqzscx}",
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json;charset=UTF-8"
    )   //产权证书查询
    OtherResponseEntity<List<CqzsResponse>> cqzscx(CqzsReq cqzsReq);

    @RequestMapping(
            value = "${Feign.bdc.new_plat.query.dyzmcx}",
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json;charset=UTF-8"
    )   //抵押证明查询
    OtherResponseEntity<List<DyzmResponse>> dyzmcx(DyzmReq dyzmReq);

    @RequestMapping(
            value = "${Feign.bdc.new_plat.query.dzzzcx}",
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json;charset=UTF-8"
    )   //电子证照查询
    OtherResponseEntity<List<Dzzzxx>> dzzzcx(DzzzReq dzzzReq);

    @RequestMapping(
            value = "${Feign.bdc.new_plat.query.shsjcx}",
            method = RequestMethod.GET,
            produces = "application/json"
    )   //审核数据查询
    OtherResponseEntity<Djshxx> shsjcx(@RequestParam("ywh")String ywh);

    @RequestMapping(
            value = "${Feign.bdc.new_plat.query.dbsjcx}",
            method = RequestMethod.GET,
            produces = "application/json"
    )   //登簿数据查询
    OtherResponseEntity<Djdbxx> dbsjcx(@RequestParam("ywh")String ywh);
}
