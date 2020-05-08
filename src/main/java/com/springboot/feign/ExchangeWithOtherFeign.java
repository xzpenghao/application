package com.springboot.feign;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.entity.chenbin.personnel.req.DLReqEntity;
import com.springboot.entity.chenbin.personnel.resp.DLReturnUnitEntity;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "otherFeign", url = "${other.url}")
public interface ExchangeWithOtherFeign {

    @RequestMapping(value = "${Feign.Tax}",method = RequestMethod.POST,produces = "application/json",consumes = "application/json;charset=UTF-8",headers = {"api_id=3ycpo7Cc","from_user=0b6c220cdfc54288b6630eb1a7fa612f"})
    ObjectRestResponse<String> testTax(Map<String,Object> taxParamBody);

    @RequestMapping(value = "${Feign.Tra}",method = RequestMethod.POST,produces = "application/json",consumes = "application/json;charset=UTF-8",headers = {"api_id=qYvkPWSr","from_user=0b6c220cdfc54288b6630eb1a7fa612f"})
    ObjectRestResponse<String> testTra(Map<String,Object> TraParamBody);

    /**
     * 电子税票请求共享交换平台
     * @param receiptNumbers 申请编号
     * @return 请求结果
     */
    @RequestMapping(value = "${Feign.ETicket}",method = RequestMethod.POST,produces = "application/json",consumes = "application/json;charset=UTF-8",headers = {"api_id=4i0zZlvo","from_user=1"})
    ObjectRestResponse<String> getETicket(@RequestBody Map<String,Object> receiptNumbers);

    @RequestMapping(value = "share/electric/electric_lmgh_apply_api.action",method = RequestMethod.POST,produces = "application/json",consumes = "application/json;charset=UTF-8",headers = {"api_id=la7dbtxP","from_user=e4f0fbe9ac9449d3bad6edee1be5626e"})
    OtherResponseEntity<DLReturnUnitEntity> sendPowerCompany(Map<String,Object> dlcs);
}
