package com.springboot.rest.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.HttpCallComponent;
import com.springboot.component.chenbin.file.FromFTPDownloadComponent;
import com.springboot.component.chenbin.file.ToFTPUploadComponent;
import com.springboot.component.fileMapping.FileNameConfigService;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.newPlat.jsonMap.FileNameMapping;
import com.springboot.entity.newPlat.settingTerm.*;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.registration.ImmovableFile;
import com.springboot.service.chenbin.other.ExchangeInterfaceService;
import com.springboot.util.chenbin.Base64Util;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import com.springboot.util.chenbin.IDUtil;
import feign.RetryableException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import java.io.*;
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
    @Autowired
    private NewPlatSettings newPlatSettings;
    @Autowired
    private FtpSettings ftpSettings;
    @Autowired
    private DzzzSetting dzzzSetting;
    @Autowired
    private NoticeSettings noticeSettings;
    @Autowired
    private FileNameConfigService fileNameConfigService;

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
        BASE64Decoder base64Decoder = new BASE64Decoder();
        try {
            File file = new File("D:/file/test/222.pdf");
            String paramreq = map.get("base64pdf");
//                    .replaceAll(" ","+");
//            String paramreq = map.get("base64pdf");
            System.out.println("base64:"+paramreq);
            Base64Util.base64StringToPdfForTax(paramreq,file);
//            Base64Util.base64StringToPDF(paramreq,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ObjectRestResponse<String>().data("哈哈哈");
    }

    @RequestMapping(value = "testJson" , method = RequestMethod.POST)
    public ObjectRestResponse<Object> testJson(@RequestBody Map map){
        Map<String ,String> returnMap = new HashMap<String ,String>();
        returnMap.put("sign","");
        returnMap.put("data",JSONObject.toJSONString(map));
        return new ObjectRestResponse<>().data(returnMap);
    }

    //测试超时
    @RequestMapping(value = "testTimeOut" , method = RequestMethod.GET)
    public ObjectRestResponse<Object> testTimeOut(){
        try {
            return outerBackFeign.testTimeOut();
        }catch ( RetryableException e){
            e.printStackTrace();
            if(e.getMessage().contains("timed out"))
                log.info("访问超时");
            log.info(e.getCause().getMessage());
        }
        return null;
    }

    @RequestMapping(value = "testSetting",method = RequestMethod.GET)
    public ObjectRestResponse<FileNameMapping> testSetting(){
        log.info("转内网配置："+JSONObject.toJSONString(newPlatSettings));
        log.info("FTP配置："+JSONObject.toJSONString(ftpSettings));
        log.info("电子证照忽略配置："+JSONObject.toJSONString(dzzzSetting));
        log.info("通知类设置："+JSONObject.toJSONString(noticeSettings));
        return new ObjectRestResponse<FileNameMapping>().data(fileNameConfigService.getFileMapConfigInfo().get(3));
    }
}
