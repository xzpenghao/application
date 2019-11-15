package com.springboot.rest.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.PersonnelUnitEntity;
import com.springboot.entity.chenbin.personnel.req.PersonnelUnitReqEntity;
import com.springboot.service.chenbin.OuterInterfaceHandleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketTimeoutException;
import java.util.List;

@Slf4j
@Api(tags = "其它外部接口api")
@RestController
@Scope("prototype")
public class OuterInterfaceHandleRest {

    @Autowired
    private OuterInterfaceHandleService outerIntfService;

    @RequestMapping(value = "/checkPersonnelUnit",method = RequestMethod.POST)
    public ObjectRestResponse<List<PersonnelUnitEntity>> checkPersonnelUnit(@RequestBody PersonnelUnitReqEntity personnelUnit){
        ObjectRestResponse<List<PersonnelUnitEntity>> rv = new ObjectRestResponse<List<PersonnelUnitEntity>>();
        List<PersonnelUnitEntity> personnelUnitEntityList = null;
        try {
            personnelUnitEntityList = outerIntfService.getPersonnelUnits(personnelUnit);
        } catch (ZtgeoBizException e){
            e.printStackTrace();
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().equals("connect timed out")){
                throw new ZtgeoBizException("接口连接超时");
            }
            if(e.getMessage().contains("404")){
                throw new ZtgeoBizException("请求地址错误");
            }
            throw e;
        }
        if(personnelUnitEntityList==null || personnelUnitEntityList.size()==0){
            throw new ZtgeoBizException("接口请求异常，返回空数据集");
        }
        return rv.data(personnelUnitEntityList);
    }

    @RequestMapping(value = "/exchangePersonnelUnit",method = RequestMethod.POST)
    public ObjectRestResponse<List<PersonnelUnitEntity>> exchangePersonnelUnit(@RequestBody PersonnelUnitReqEntity personnelUnit){
        return null;
    }

    @RequestMapping(value = "/checkMarry2",method = RequestMethod.POST)
    public ObjectRestResponse<PersonnelUnitEntity> checkMarry2(@RequestBody PersonnelUnitReqEntity personnelUnit){
        return null;
    }
}
