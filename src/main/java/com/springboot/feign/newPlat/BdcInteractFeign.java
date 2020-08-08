package com.springboot.feign.newPlat;

import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.entity.newPlat.query.resp.Djdbxx;
import com.springboot.entity.newPlat.query.resp.Djshxx;
import com.springboot.entity.newPlat.transInner.req.NewBdcFlowCheckReq;
import com.springboot.entity.newPlat.transInner.req.fromZY.NewBdcFlowRequest;
import com.springboot.entity.newPlat.transInner.req.fromZY.domain.NewBdcFlowRespData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author chenb
 * @version 2020/8/7
 * description：不动产交互feign
 */
@FeignClient(name = "bdcInteractFeign", url = "${bdccx.url}")
public interface BdcInteractFeign {
    @RequestMapping(
            value = "${Feign.bdc.newPlat.interact.wsjgjc}",
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json;charset=UTF-8"
    )   //网申结果检查
    OtherResponseEntity<List<NewBdcFlowRespData>> wsjgjc(NewBdcFlowCheckReq wwywh);

    @RequestMapping(
            value = "${Feign.bdc.newPlat.interact.wsbjcj}",
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json;charset=UTF-8"
    )   //网申办件创建
    OtherResponseEntity<List<NewBdcFlowRespData>> wsbjcj(NewBdcFlowRequest newBdcFlowRequest);
}
