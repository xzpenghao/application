package com.springboot.component;

import com.springboot.config.Msgagger;
import com.springboot.popj.GetReceiving;
import com.springboot.popj.ReturnVo;
import com.springboot.popj.pub_data.RespServiceData;
import com.springboot.popj.pub_data.SJ_Info_Handle_Result;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.popj.register.HttpRequestMethedEnum;
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
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            public String call() throws Exception { //建议抛出异常
                Map<String, String> mapParmeter = new HashMap<>();
                try {
                    if (getReceiving.getMessageType().equals(Msgagger.VERIFYNOTICE)){
                        //发送登记局获取数据整理发送一窗受理
                        String json = httpClientUtils.getJsonData(JSONObject.fromObject(getReceiving.getSlbh()),"http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetVerifyInfo");
                        JSONObject jsonObject=JSONObject.fromObject(json);
                        JSONArray verfyInfoArray=jsonObject.getJSONArray("verifyInfoVoList");
                        List<SJ_Info_Handle_Result> handleResultVoList=new ArrayList<>();
                        List<RespServiceData> serviceDatas=new ArrayList<>();
                        for (int i=0;i<verfyInfoArray.size();i++){
                            JSONObject verfyInfoObject=verfyInfoArray.getJSONObject(i);
                            SJ_Info_Handle_Result handleResult=new SJ_Info_Handle_Result();
                            handleResult.setHandleText(verfyInfoObject.getString("verifyOpinion"));
                            handleResult.setHandleResult("某某"+verfyInfoObject.getString("registerSubType")+"审核"+ Msgagger.ADOPT);
                            handleResult.setProvideUnit("不动产登记局");
                            handleResultVoList.add(handleResult);
                        }
                        RespServiceData respServiceData=new RespServiceData();
                        respServiceData.setServiceCode(immovableHandleResultService);
                        respServiceData.setServiceDataInfos(handleResultVoList);
                        serviceDatas.add(respServiceData);
                        mapParmeter.put("serviceDatas",JSONObject.fromObject(serviceDatas).toString());
                        mapParmeter.put("registerNumber", jsonObject.getString("slbh"));//受理编号)
                    }else if (getReceiving.getMessageType().equals(Msgagger.RESULTNOTICE)){
                        //发送登记局获取数据整理发送一窗受理
                        String json = httpClientUtils.getJsonData(JSONObject.fromObject(getReceiving.getSlbh()),"http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetCertificateInfo");
                        JSONObject jsonObject=JSONObject.fromObject(json);
                        mapParmeter.put("immovableSite",jsonObject.getString("sit"));
                        mapParmeter.put("registerNumber",jsonObject.getString("slbh"));
                        JSONArray ficateInfoArray=jsonObject.getJSONArray("certificateInfoVoList");
                        for (int i=0;i<ficateInfoArray.size();i++){
                            JSONObject verfyInfoObject=ficateInfoArray.getJSONObject(i);

                        }
                    }else {
                        mapParmeter.put("registerNumber", getReceiving.getSlbh());
                    }
                    String token = realEstateMortgageComponent.getToken("tsdjj", "123456");//获得token
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
        try {
            returnVo.setCode(200);
            returnVo.setMessage("受理成功");
//                    JSONObject.fromObject(returnVo);
//                    Thread.sleep(1000);
            outputStream.write(returnVo.toString().getBytes());
            outputStream.flush();
            System.out.println(JSONObject.fromObject(returnVo));
            // 创建数据
            String result = future.get(); //取得结果，同时设置超时执行时间为5秒。
            System.err.println("result is " + JSONObject.fromObject(returnVo) + ", time is " + (System.currentTimeMillis() - t));


//            ps.print(returnVo.toString());
//            return  returnVo.toString();
        } catch (InterruptedException e) {
            future.cancel(true);
            System.err.println("Interrupte time is " + (System.currentTimeMillis() - t));
        } catch (ExecutionException e) {
            future.cancel(true);
            System.err.println("Throw Exception time is " + (System.currentTimeMillis() - t));
        } finally {
            outputStream.close();
            executor.shutdown();
        }
    }

    public String getRegistrationSubcategory(String registrationName){
        String serviceCode="";
        //判断模糊小类输出serviceCode
        if (registrationName.equals("抵押权注销登记")){
            serviceCode="MortgageElectronicCertCancellation";
        }else if (registrationName.equals("一般抵押权")){
            serviceCode="";
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
