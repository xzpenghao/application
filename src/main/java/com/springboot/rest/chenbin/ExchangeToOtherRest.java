package com.springboot.rest.chenbin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.AnonymousInnerComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.other.web.RequestParamBody;
import com.springboot.entity.chenbin.personnel.pub_use.FileEntityForOther;
import com.springboot.entity.chenbin.personnel.tax.TaxRespBody;
import com.springboot.entity.chenbin.personnel.tra.TraRespBody;
import com.springboot.popj.GetReceiving;
import com.springboot.popj.pub_data.Sj_Info_Qsxx;
import com.springboot.service.chenbin.ExchangeToTaxService;
import com.springboot.service.chenbin.ExchangeToTransactionService;
import com.springboot.service.chenbin.ExchangeToWebService;
import com.springboot.service.chenbin.other.ExchangeCommonService;
import com.springboot.service.chenbin.other.ExchangeInterfaceService;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import com.springboot.util.chenbin.ErrorDealUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "其它部门api")
@RestController
@Scope("prototype")
public class ExchangeToOtherRest {
    @Autowired
    private ExchangeToTaxService exc2Tax;
    @Autowired
    private ExchangeToTransactionService exc2Tran;
    @Autowired
    private ExchangeToWebService exc2Web;
    @Autowired
    private ExchangeCommonService commService;
    @Autowired
    private AnonymousInnerComponent anonymousInnerComponent;

    @RequestMapping(value = "exchange2Tax",method = RequestMethod.GET)
    public ObjectRestResponse<Object> exchange2Tax(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer){
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        try {
            //log.warn("契税转入，本次参数为：" + commonInterfaceAttributer);
            rv.data(exc2Tax.deal2Tax(commonInterfaceAttributer));
        } catch (ParseException e1) {
            log.error(ErrorDealUtil.getErrorInfo(e1));
            rv.setStatus(20500);
            rv.setData("转内网传入的数据格式不正确");
        } catch (ZtgeoBizException e2) {
            log.error(ErrorDealUtil.getErrorInfo(e2));
            rv.setStatus(20500);
            rv.setData(e2.getMessage());
        } catch (Exception e3) {
            log.error(ErrorDealUtil.getErrorInfo(e3));
            rv.setStatus(20500);
            rv.setData("转内网出现其它运行时异常，请排查！");
        }
        return rv;
    }

    @RequestMapping(value = "exchange2Tra",method = RequestMethod.GET)
    public ObjectRestResponse<Object> exchange2Tra(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer){
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        try {
            //log.warn("交易转入，本次参数为：" + commonInterfaceAttributer);
            rv.data(exc2Tran.deal2Tra(commonInterfaceAttributer));
        } catch (ParseException e1) {
            log.error(ErrorDealUtil.getErrorInfo(e1));
            rv.setStatus(20500);
            rv.setData("转内网传入的数据格式不正确");
        } catch (ZtgeoBizException e2) {
            log.error(ErrorDealUtil.getErrorInfo(e2));
            rv.setStatus(20500);
            rv.setData(e2.getMessage());
        } catch (Exception e3) {
            log.error(ErrorDealUtil.getErrorInfo(e3));
            rv.setStatus(20500);
            rv.setData("转内网出现其它运行时异常，请排查！");
        }
        return rv;
    }

    @RequestMapping(value = "web2ycslForSec",method = RequestMethod.POST)
    public ObjectRestResponse<Object> web2ycslForSecondTransfer(@RequestBody RequestParamBody paramBody){
        System.out.println("传入变量为："+ JSONObject.toJSONString(paramBody));
        ObjectRestResponse rv = new ObjectRestResponse<>();
        Object r = null;
        try {
            r = exc2Web.initWebSecReg(paramBody);
            log.info("互联网+生成办件成功！"+r);
            JSONObject ycslRv = JSONObject.parseObject((String)r);
            log.info("互联网+生成办件对象成功！"+ycslRv);
            rv.data(ycslRv.get("receiptNumber"));
        } catch (ZtgeoBizException e1){
            log.error("ZTGEO异常："+ErrorDealUtil.getErrorInfo(e1));
            e1.printStackTrace();
            rv.setStatus(20500);
            rv.setMessage(e1.getMessage());
        } catch (Exception e2){
            log.error("请求出现其它运行时异常："+ErrorDealUtil.getErrorInfo(e2));
            e2.printStackTrace();
            rv.setStatus(20500);
            rv.setMessage("请求出现其它运行时异常，请联系管理员解决");
        }
        return rv;
    }



    @RequestMapping(value = "examineSuccess/jy", method = RequestMethod.POST)
    @ApiOperation(value = "网签备案成功通知",
            notes = "模拟交易审核网签备案成功后返回数据并驱动一窗受理平台流程继续。")
    public ObjectRestResponse<String> examineSuccess4(@RequestBody Map<String,String> respMap ){
        System.out.println("进入网签备案结果回推");
        log.info("进入网签备案结果回推");
        log.debug("网签传入回推数据："+JSONObject.toJSONString(respMap));
        TraRespBody traRespBody = JSONObject.parseObject(respMap.get("data"),TraRespBody.class);
        return exc2Tran.examineSuccess4Tra(traRespBody);
    }

    @RequestMapping(value = "examineSuccess/qs", method = RequestMethod.POST)
    @ApiOperation(value = "核税成功通知",
            notes = "模拟税务局核税成功后返回数据并驱动一窗受理平台流程继续。")
    public ObjectRestResponse<List<ObjectRestResponse>> examineSuccess2(@RequestBody Map<String,String> respMap ){
        log.info("进入税务完税结果回推");
        log.info("交换平台处理所得完税结果："+JSONObject.toJSONString(respMap));
        log.info("截取推入结果为："+respMap.get("data"));
        return exc2Tax.examineSuccess4TaxBatch(respMap.get("data"));
    }


    @RequestMapping(value = "postFileByPath", method = RequestMethod.POST)
    @ApiOperation(value = "附件拉取接口",
            notes = "各平行部门接收收件成功后回调拉取具体附件的接口。")
    public ObjectRestResponse<FileEntityForOther> postFileByPath(@RequestBody Map<String,String> respMap ){
        log.info("进入单一附件拉取");
        FileEntityForOther fileEntity = JSONObject.parseObject(BusinessDealBaseUtil.convertStr(respMap.get("data")),FileEntityForOther.class);
        return new ObjectRestResponse<FileEntityForOther>().data(commService.getFileEntityForOther(fileEntity));
    }

    @RequestMapping(value = "postFiles", method = RequestMethod.POST)
    @ApiOperation(value = "附件批量拉取接口",
            notes = "各平行部门接收收件成功后回调批量拉取附件的接口。")
    public ObjectRestResponse<FileEntityForOther> postFiles(@RequestBody Map<String,String> respMap ){
        log.info("进入批量附件拉取");
        List<FileEntityForOther> fileEntityArray = JSONArray.parseArray(respMap.get("data"),FileEntityForOther.class);
        return new ObjectRestResponse<List<FileEntityForOther>>().data(commService.getFileEntityArrayForOther(fileEntityArray));
    }

    @RequestMapping(value = "postBookInfo",method = RequestMethod.POST)
    public ObjectRestResponse<String> postBookInfo(@RequestBody GetReceiving getReceiving, HttpServletRequest request){
        try {
            anonymousInnerComponent.extractInformation(getReceiving, request.getHeader("Authorization"), new HashMap<String, Object>());
        } catch (Exception e){
            log.error("手动获取登簿数据时出现异常，异常信息为："+ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("登簿数据获取失败！");
        }
        return new ObjectRestResponse<String>().data("操作成功");
    }
}
