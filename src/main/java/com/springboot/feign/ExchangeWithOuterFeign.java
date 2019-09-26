package com.springboot.feign;

import com.springboot.entity.chenbin.personnel.punit.GSCommonCheckEntity;
import com.springboot.entity.chenbin.personnel.punit.GSCommonEntity;
import com.springboot.entity.chenbin.personnel.punit.PersonCheckEntity;
import com.springboot.entity.chenbin.personnel.punit.PersonEntity;
import com.springboot.entity.chenbin.personnel.resp.PersonnelResponseListEntity;
import com.springboot.entity.chenbin.personnel.resp.PersonnelResponseSingleEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Map;

@FeignClient(name = "exchangeFeign", url = "http://localhost:7780/api/test/")
public interface ExchangeWithOuterFeign {

    @RequestMapping(value="share/rkkjzxxcx",method = RequestMethod.POST,produces = "application/json",consumes = "application/json",headers = {"api_id=xxx","from_user=yyy"})
    PersonnelResponseListEntity<PersonEntity> rkkjzxxcx(Map<String,Object> params); //公安信息

    @RequestMapping(value="share/rkkrxbd",method = RequestMethod.POST,produces = "application/json",consumes = "application/json",headers = {"api_id=xxx","from_user=yyy"})
    PersonnelResponseListEntity<PersonCheckEntity> rkkrxbd(Map<String,Object> params); //人像对比

    @RequestMapping(value="share/qyjbxxyz",method = RequestMethod.POST,produces = "application/json",consumes = "application/json",headers = {"api_id=xxx","from_user=yyy"})
    PersonnelResponseSingleEntity<GSCommonCheckEntity> qyjbxxyz(Map<String,Object> params); //工商-信息检查

    @RequestMapping(value="share/qyjbxxcx",method = RequestMethod.POST,produces = "application/json",consumes = "application/json",headers = {"api_id=xxx","from_user=yyy"})
    PersonnelResponseListEntity<GSCommonEntity> qyjbxxcx(Map<String,Object> params);    //工商-信息数据获取

    //社会团体
}
