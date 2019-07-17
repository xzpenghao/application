package com.springboot.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.HTSoapAnalyzeComponent;
import com.springboot.util.NetSignUtils;
import com.springboot.util.ParseXML;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 网签数据
 */
@Slf4j
@RestController
@Scope("prototype")
public class HTInfoGetController {

    @Autowired
    private HTSoapAnalyzeComponent htSoapAnalyzeComponent;


    @RequestMapping(value = "/getHTInfo", method = RequestMethod.GET)
    public ObjectRestResponse getHTInfo(@RequestParam("htbh") String htbh) {
        log.info("进入合同信息获取");
        ObjectRestResponse resultRV = new ObjectRestResponse();
        try{
            String htxx = NetSignUtils.spfyght(htbh);
            if (StringUtils.isEmpty(htxx)){
                resultRV.setMessage("连接webservice接口失败");
                return resultRV;
            }
            Map<String,Object> m = ParseXML.parseByStr(htxx);
            if(Integer.parseInt((String) m.get("flag"))==0){
                log.info("经查合同号为："+htbh+"的合同无效");
                resultRV.setStatus(20500);
                resultRV.setMessage("faild");
                resultRV.data("查询合同信息无效，不允许申请登记");
            }else{
                log.info("经查合同号为："+htbh+"的合同有效");
                String qlrxx = NetSignUtils.spfQlrxx(htbh);//获取权利人，义务人信息
                resultRV = this.htSoapAnalyzeComponent.analyzeSoap(htxx,qlrxx);
            }
        } catch (Exception e){
            log.error(e.getStackTrace().toString());
            log.info(e.getMessage());
            e.printStackTrace();
            resultRV.setStatus(20500);
            resultRV.setMessage("faild");
            resultRV.data("查询合同信息异常，请稍后重试");
        }
        return resultRV;
    }


}
