package com.springboot.rest.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.HttpCallComponent;
import com.springboot.component.chenbin.file.FromFTPDownloadComponent;
import com.springboot.component.chenbin.file.ToFTPUploadComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.registration.ImmovableFile;
import com.springboot.service.chenbin.other.ExchangeInterfaceService;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import com.springboot.util.chenbin.IDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "测试api")
@RestController
@Scope("prototype")
public class TestRest {
    @Autowired
    private HttpCallComponent httpCallComponent;
    @Autowired
    private OuterBackFeign outerBackFeign;

    @RequestMapping(value = "/getFileList", method = RequestMethod.POST)
    public ObjectRestResponse<List<ImmovableFile>> getFileList(@RequestParam("receiptNumber") String receiptNumber, @RequestParam("token") String token) {
        List<SJ_Fjfile> fileVoList = httpCallComponent.getFileVoList(receiptNumber, token);

        return new ObjectRestResponse<List<ImmovableFile>>().data(null);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ObjectRestResponse<Object> test(@RequestBody Map<String,String> map){
        log.info("开始");
        Map<String,String> mapR = new HashMap<String,String>();
        mapR.put("sign","");
        mapR.put("data", JSONObject.toJSONString(map));
        return outerBackFeign.test("哈哈哈");
    }
}
