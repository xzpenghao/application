package com.springboot.component;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.HttpCallComponent;
import com.springboot.config.DJJUser;
import com.springboot.config.Msgagger;
import com.springboot.popj.GetReceiving;
import com.springboot.popj.MortgageService;
import com.springboot.popj.ReturnVo;
import com.springboot.popj.pub_data.RespServiceData;
import com.springboot.popj.pub_data.SJ_Info_Handle_Result;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.popj.register.HttpRequestMethedEnum;
import com.springboot.popj.warrant.RealPropertyCertificate;
import com.springboot.util.HttpClientUtils;
import com.springboot.util.ParamHttpClientUtil;
import com.springboot.util.ReturnMsgUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
@Slf4j
public class AnonymousInnerComponent {


    @Autowired
    private HttpCallComponent httpCallComponent;
    @Autowired
    private RealEstateMortgageComponent realEstateMortgageComponent;
    @Value("${httpclient.windowAcceptanceIp}")
    private String windowAcceptanceIp; //一窗受理ip
    @Value("${httpclient.windowAcceptanceSeam}")
    private String windowAcceptanceSeam; //一窗受理接口
    @Value("${httpclient.ip}")
    private String ip;
    @Value("${httpclient.seam}")
    private String seam;
    @Value("${service.bdcbljgtjfw}")
    private String immovableHandleResultService;
    @Autowired
    private HttpClientUtils httpClientUtils;


    public void GetReceiving(GetReceiving getReceiving, OutputStream outputStream) throws  IOException{
        ExecutorService executor = Executors.newCachedThreadPool();
        ReturnVo returnVo = new ReturnVo();
        Map<String, String> mapParmeter = new HashMap<>();
        Map<String,Object> map=new HashMap<>();
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            public String call() throws Exception { //建议抛出异常
                try {
                    System.out.println("执行主线程");
                    String token = httpCallComponent.getToken(DJJUser.USERNAME, DJJUser.PASSWORD);//获得token
                    if (getReceiving.getMessageType().equals(Msgagger.VERIFYNOTICE)){//审核
                        System.out.println("进入审核");
                        map.put("slbh",getReceiving.getSlbh());
                        //发送登记局获取数据整理发送一窗受理
                        String json = httpClientUtils.doGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetVerifyInfo",map,null);
                        System.out.println(json);
                        JSONObject jsonObject=JSONObject.fromObject(json);
                        JSONArray verfyInfoArray=jsonObject.getJSONArray("verifyInfoVoList");
                        List<SJ_Info_Handle_Result> handleResultVoList=new ArrayList<>();
                        List<RespServiceData> serviceDatas=new ArrayList<>();
                        for (int i=0;i<verfyInfoArray.size();i++){
                            JSONObject verfyInfoObject=verfyInfoArray.getJSONObject(i);
                            SJ_Info_Handle_Result handleResult=new SJ_Info_Handle_Result();
                            handleResult.setHandleText(verfyInfoObject.getString("verifyOpinion"));
                            handleResult.setHandleResult(verfyInfoObject.getString("registerSubType")+ Msgagger.ADOPT);
                            handleResult.setProvideUnit(Msgagger.REGISTRATION);
                            handleResult.setDataComeFromMode(Msgagger.SUCCESSFUL_INTERFACE);
                            handleResultVoList.add(handleResult);
                        }
                        RespServiceData respServiceData=new RespServiceData();
                        respServiceData.setServiceCode(immovableHandleResultService);
                        respServiceData.setServiceDataInfos(handleResultVoList);
                        serviceDatas.add(respServiceData);
                        JSONArray jsonArray=JSONArray.fromObject(serviceDatas);
                        System.out.println("servicedatas"+jsonArray);
                        mapParmeter.put("serviceDatas",jsonArray.toString());
                        mapParmeter.put("registerNumber", jsonObject.getString("slbh"));//受理编号)
                    }else if (getReceiving.getMessageType().equals(Msgagger.RESULTNOTICE)){//登簿审核
                        System.out.println("进入登簿");
                        map.put("slbh",getReceiving.getSlbh());
                        //发送登记局获取数据整理发送一窗受理
                        String json = httpClientUtils.doGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetCertificateInfo",map,null);
                        JSONObject jsonObject=JSONObject.fromObject(json);
                        mapParmeter.put("immovableSite",jsonObject.getString("sit"));
                        mapParmeter.put("registerNumber",jsonObject.getString("slbh"));
                        //登簿返回信息
                        JSONArray ficateInfoArray=jsonObject.getJSONArray("certificateInfoVoList");
                        List<SJ_Info_Handle_Result> handleResultVoList=new ArrayList<>();
                        List<RespServiceData> respServiceDataList=new ArrayList<>();
                        List<String> stringList=new ArrayList<>();
                        //获取受理编号信息
                        for (int i=0;i<ficateInfoArray.size();i++){
                            JSONObject verfyInfoObject=ficateInfoArray.getJSONObject(i);
                            stringList.add(verfyInfoObject.getString("registerSubType"));
                            RespServiceData RealEstateBookData=new RespServiceData();
                            RespServiceData getRealEstateBooking=getRealEstateBooking(verfyInfoObject.getString("certificateType"),
                                    verfyInfoObject.getString("certificateId"),RealEstateBookData);
                            if (getRealEstateBooking.getServiceCode().equals(Msgagger.DYZMHSERVICE_CODE)){
                                getRealEstateBooking.setServiceCode(Msgagger.DYZXSERVICECODE);
                            }
                            respServiceDataList.add(getRealEstateBooking);//不动产展示登簿信息
                        }
                            RespServiceData respServiceData=new RespServiceData();
                            respServiceData.setServiceCode(Msgagger.DBSERVICECODE);
                            //获取登记小类信息
                            for (String string:stringList) {
                            SJ_Info_Handle_Result handleResult=new SJ_Info_Handle_Result();
                            handleResult.setHandleResult(string+"登簿成功");
                            handleResult.setHandleText(Msgagger.SUCCESSFUL_DENGBUCG);
                            handleResult.setProvideUnit(Msgagger.REGISTRATION);
                            handleResult.setDataComeFromMode(Msgagger.SUCCESSFUL_INTERFACE);
                            handleResultVoList.add(handleResult);
                            respServiceData.setServiceDataInfos(handleResultVoList);
                        }

                        respServiceDataList.add(respServiceData);
                        JSONArray jsonArray=JSONArray.fromObject(respServiceDataList);
                        mapParmeter.put("serviceDatas",jsonArray.toString());
                    }else if (getReceiving.getMessageType().equals(Msgagger.ACCPETNOTICE)){
                        System.out.println("执行受理操作");
                        mapParmeter.put("registerNumber", getReceiving.getSlbh());
                    }
                    System.out.println(token);
                    //返回数据到一窗受理平台保存受理编号和登记编号
                    String resultJson = preservationRegistryData(mapParmeter, token);
                    System.err.println("result is " + resultJson );
                    return resultJson;
                } catch (Exception e) {
                    throw new Exception("Callable terminated with Exception!"); // call方法可以抛出异常
                }
            }
        });
        executor.execute(future);
        long t = System.currentTimeMillis();
//        try {
            returnVo.setCode(200);
            returnVo.setMessage("受理成功");
            JSONObject object=JSONObject.fromObject(returnVo);
            outputStream.write(object.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            System.out.println(JSONObject.fromObject(returnVo));
            // 创建数据
//            String result = future.get(); //取得结果，同时设置超时执行时间为5秒。
            System.err.println("result is " + JSONObject.fromObject(returnVo) + ", time is " + (System.currentTimeMillis() - t));
            executor.shutdown();
    }

    /**
     * 通过类型，证号查询登记局数据
     * @return
     */
    private RespServiceData  getRealEstateBooking(String certificateType,String certificateId,RespServiceData respServiceData)  throws Exception{
        ObjectRestResponse resultRV = new ObjectRestResponse();
      switch (certificateType){
          case  "DYZMH":
               resultRV=realEstateMortgageComponent.getRealEstateMortgage(certificateId,null,true);
               List<MortgageService> mortgageServiceList=(List<MortgageService>) resultRV.getData();
               respServiceData.setServiceCode(Msgagger.DYZMHSERVICE_CODE);
               respServiceData.setServiceDataInfos(mortgageServiceList);
              break;
          case "YGZMH":
              resultRV=realEstateMortgageComponent.getMortgageCancellation(certificateId);
              List<RealPropertyCertificate> realPropertyCertificateList=(List<RealPropertyCertificate>) resultRV.getData();
              respServiceData.setServiceCode(Msgagger.YGZMSERVICE_CODE);
              respServiceData.setServiceDataInfos(realPropertyCertificateList);
              break;
      }
      return respServiceData;



    }




    public String getRegistrationSubcategory(String registrationName){
        String serviceCode="";
        //判断模糊小类输出serviceCode
        if (registrationName.equals("抵押权注销登记")){
            serviceCode=Msgagger.DBSERVICECODE;
        }else if (registrationName.equals("预告证明号登记")){
            serviceCode="ImmovableElectronicCertificate";
        }else if (registrationName.equals("")){
            serviceCode="";
        }else if (registrationName.equals("")){
            serviceCode="";
        }
        return  serviceCode;
    }




    private String preservationRegistryData(Map<String,String> map,String token){
        Map<String,String> header = new HashMap<String,String>();
        header.put("Authorization",token);
        String json = ParamHttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost,
                "application/json",
                "http://" + windowAcceptanceIp + ":" + windowAcceptanceSeam + "/api/biz/RecService/DealRecieveFromOuter2",
                map,header);
        com.alibaba.fastjson.JSONObject jsonObject=(com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(json);
        System.out.println("chenbin返回信息为："+jsonObject);
        return json;
    }






}
