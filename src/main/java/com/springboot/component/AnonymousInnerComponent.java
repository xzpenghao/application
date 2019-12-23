package com.springboot.component;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.google.common.collect.Maps;
import com.springboot.component.chenbin.ExchangeToInnerComponent;
import com.springboot.component.chenbin.HttpCallComponent;
import com.springboot.component.chenbin.file.ToFTPUploadComponent;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.EsfSdq;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.chenbin.personnel.other.bank.bankenum.MortgagorPtypeEnum;
import com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.domain.FileInfoVo;
import com.springboot.entity.chenbin.personnel.other.bank.notice.result.ResultNoticeReqVo;
import com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.MortgageeInfoVo;
import com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.MortgagorInfoVo;
import com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.RealEstateInfoVo;
import com.springboot.mapper.ExceptionRecordMapper;
import com.springboot.popj.*;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.register.HttpRequestMethedEnum;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.popj.warrant.RealPropertyCertificate;
import com.springboot.util.DateUtils;
import com.springboot.util.HttpClientUtils;
import com.springboot.util.ParamHttpClientUtil;
import com.springboot.util.StrUtil;
import com.springboot.util.chenbin.HttpClientUtil;
import com.springboot.util.chenbin.IDUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.*;

@Component
@Slf4j
public class AnonymousInnerComponent {


    @Autowired
    private HttpCallComponent httpCallComponent;
    @Autowired
    private RealEstateMortgageComponent realEstateMortgageComponent;
    @Autowired
    private ExchangeToInnerComponent exchangeToInnerComponent;
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
    @Value("${sq.bank.jt.itemName}")
    private String itemName;
    @Value("${machine.ip}")
    private String machineIp;
    @Value("${machine.post}")
    private String machinePost;
    @Value("${webplus.ftpAddressBdc}")
    private String ftpAddress;
    @Value("${webplus.ftpPortBdc}")
    private String ftpPort;
    @Value("${webplus.ftpUsernameBdc}")
    private String ftpUsername;
    @Value("${webplus.ftpPasswordBdc}")
    private String ftpPassword;
    @Value("${djj.bsryname}")
    private String bsryname;
    @Value("${djj.bsrypassword}")
    private String bsrypassword;
    @Value("${djj.tsryname}")
    private String tsryname;
    @Value("${djj.tsrypaaword}")
    private String tsrypaaword;


    @Value("${sq.gxpt.ftpAddress}")
    private String gxftpAddress;
    @Value("${sq.gxpt.ftpPort}")
    private String gxftpPort;
    @Value("${sq.gxpt.ftpUsername}")
    private String gxftpUsername;
    @Value("${sq.gxpt.ftpPassword}")
    private String gxftpPassword;


    @Value("${webplus.ftpAddress}")
    private String yftpAddress;
    @Value("${webplus.ftpPort}")
    private String yftpPort;
    @Value("${webplus.ftpUsername}")
    private String yftpUsername;
    @Value("${webplus.ftpPassword}")
    private String yftpPassword;

    @Value("${sq.bank.jt.ip}")
    private String jtIp;
    @Value("${sq.bank.jt.post}")
    private String jtPost;
    @Value("${penghao.transferholdmap}")
    private boolean transferHoldmap;

    @Autowired
    private ExceptionRecordMapper exceptionRecordMapper;
    @Autowired
    private ToFTPUploadComponent toFTPUploadComponent;
    @Autowired
    private BdcFTPDownloadComponent bdcFTPDownloadComponent;

    @Autowired
    private HttpClientUtils httpClientUtils;


    public void extractInformation(GetReceiving getReceiving,String token,Map<String, Object> modelMap) throws Exception{
        com.alibaba.fastjson.JSONObject tokenObject=null;
        Map<String, String> mapHeader = new HashMap<>();
        Map<String, String> mapParmeter = new HashMap<>();
        log.info("执行主线程");
        if (
                StringUtils.isBlank(token)
        ) {
            tokenObject = httpCallComponent.getTokenYcsl(bsryname, bsrypassword);//获得token
            token = getToken(tokenObject, "getSendRoom", getReceiving.getSlbh(), getReceiving.getMessageType(), null);
        }
        mapHeader.put("Authorization", token);
        if (getReceiving.getMessageType().equals(Msgagger.ACCPETNOTICE)) {//受理
            mapParmeter.put("modelId" , getReceiving.getModelId());
            //受理启动一窗受理流程
            String entry = httpClientUtils.doGet("http://" + windowAcceptanceIp + ":" + windowAcceptanceSeam + "/api/biz/RecService/DealRecieveFromOuter6", modelMap, mapHeader);
            log.info("entry"+entry);
            JSONObject entryObject = JSONObject.fromObject(entry);
            JSONArray entryArray = JSONArray.fromObject(entryObject.get("data"));//获取data数据
            EsfSdq esfSdq = new EsfSdq();
            esfSdq.setAttDirList(entryArray);
            esfSdq.setSlbh(getReceiving.getSlbh());
            esfSdq.setTransferred(false);
            JSONObject paramObject = JSONObject.fromObject(esfSdq);//整理参数信息
            System.out.println("aa"+paramObject.toString());
            //根据受理编号查询转移信息（水电气）
            String zyxx = HttpClientUtils.getJsonData(paramObject, "http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetZYInfo4SDQ");
            log.info("zyxx"+zyxx);
            com.alibaba.fastjson.JSONObject zyxxObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(zyxx);                        //整理数据发送到一窗受理
            ownershipInFormationxx(zyxxObject, mapParmeter, Msgagger.ESFSDQSERVICE_CODE, false, getReceiving.getSlbh());//获取不动产权属信息
            log.info("sjsq"+mapParmeter.get("SJ_Sjsq"));
            log.info("modelId"+mapParmeter.get("modelId"));
            String path=DateUtils.getNowYear() + File.separator + DateUtils.getNowMonth() + File.separator + DateUtils.getNowDay();
            getProcessingAnnex(zyxxObject,mapParmeter,null,ftpAddress,ftpPort,ftpUsername,ftpPassword,path);//附件上传
            log.info("fileInfoList"+mapParmeter.get("fileInfoList"));
            //发送一窗受理进行启动流程
            String json = preservationRegistryData(mapParmeter, token, "/api/biz/RecService/DealRecieveFromOuter5");
            JSONObject jsonObject = JSONObject.fromObject(json);
            log.info("流程返回"+json);
            log.info("返回结果"+jsonObject.get("status").toString());
            if ((Integer) jsonObject.get("status") == 200) {
                log.info("存量房水电气流程数据保存成功及流程开启成功");
            } else {
                log.error("存量房信息保存失败,流程未开启"+json);
            }
        } else if (getReceiving.getMessageType().equals(Msgagger.RESULTNOTICE)) {
            log.info("开始进行二手房登簿");
            //登簿通知
            EsfSdq esfSdq = new EsfSdq();
            esfSdq.setSlbh(getReceiving.getSlbh());
            esfSdq.setTransferred(true);
            esfSdq.setHouseholdMap(transferHoldmap);
            JSONObject jsonObject = JSONObject.fromObject(esfSdq);
            //根据受理编号查询转移信息（水电气）
            String jsonData = HttpClientUtils.getJsonData(jsonObject, "http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetZYInfo4SDQ");
            log.info("查询转移信息(水电气)"+jsonData);
            com.alibaba.fastjson.JSONObject zyxxObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(jsonData);
            ownershipInFormationxx(zyxxObject, mapParmeter, Msgagger.BDCQZSDZF_SERVICE_CODE, true, getReceiving.getSlbh());//获取不动产权属信息
            mapParmeter.put("registerNumber", getReceiving.getSlbh());
            String json = preservationRegistryData(mapParmeter, token, "/api/biz/RecService/DealRecieveFromOuter2");
            JSONObject ycslObject = JSONObject.fromObject(json);
            log.info("一窗受理返回"+ycslObject.toString());
        }
    }





    public void getSendRoom(GetReceiving getReceiving, OutputStream outputStream, String token) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ReturnVo returnVo = new ReturnVo();
        Map<String, Object> modelMap = Maps.newHashMap();
        log.info("modelId"+getReceiving.getModelId());
        modelMap.put("modelId", getReceiving.getModelId());
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            public String call() throws Exception { //建议抛出异常
                extractInformation(getReceiving,token,modelMap);
                return null;
            }
        });
        executor.execute(future);
        long t = System.currentTimeMillis();
        try {
            returnVo.setCode(200);
            returnVo.setMessage(Msgagger.CG);
            JSONObject object = JSONObject.fromObject(returnVo);
            outputStream.write(object.toString().getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            System.out.println(JSONObject.fromObject(returnVo));
            // 创建数据
//            String result = future.get(); //取得结果，同时设置超时执行时间为5秒。
            System.err.println("result is " + JSONObject.fromObject(returnVo) + ", time is " + (System.currentTimeMillis() - t));
            executor.shutdown();
        } catch (Exception e) {
            log.error("e"+e);
            e.getStackTrace();
        }
    }

    /**
     * 整理二手房登簿成功数据
     *
     * @param
     */
    private void zlSJsqDbSj(List<RespServiceData> respServiceDataList) {
        RespServiceData respServiceData = new RespServiceData();
        respServiceData.setServiceCode(Msgagger.DBSERVICECODE);
        SJ_Info_Handle_Result handleResult = new SJ_Info_Handle_Result();
        List<SJ_Info_Handle_Result> handleResultVoList = new ArrayList<>();
        handleResult.setHandleResult("登簿成功");
        handleResult.setHandleText(Msgagger.SUCCESSFUL_DENGBUCG);
        handleResult.setProvideUnit(Msgagger.REGISTRATION);
        handleResult.setDataComeFromMode(Msgagger.SUCCESSFUL_INTERFACE);
        handleResultVoList.add(handleResult);
        respServiceData.setServiceDataInfos(handleResultVoList);
        respServiceDataList.add(respServiceData);
    }


    /**
     * 整理二手房水电气权属信息
     *
     * @param jsonObject
     * @return
     */
    private void ownershipInFormationxx(com.alibaba.fastjson.JSONObject jsonObject, Map<String, String> stringMap, String serviceCode, boolean flag, String registerNumber) {
        //先处理文件上传
        SJ_Sjsq sjSjsq = new SJ_Sjsq();
        List<SJ_Book_Pic_ext> sjBookPicExtList=new ArrayList<>();
        sjSjsq.setNotifiedPersonName(jsonObject.getString("contacts"));//通知人姓名
        sjSjsq.setNotifiedPersonTelephone(jsonObject.getString("contactsPhone"));//通知人电话
        sjSjsq.setNotifiedPersonAddress(jsonObject.getString("contactsAdress"));//通知人地址
        sjSjsq.setDistrictCode(jsonObject.getString("businessAreas"));//区县编码
        List<RealPropertyCertificate> realPropertyCertificateList = new ArrayList<>();
        String array = jsonObject.getString("realEstateInfoList");
        com.alibaba.fastjson.JSONObject obj1 = jsonObject.getJSONObject("sdqInfo");
        com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseArray(array);
        //整理水电气部门至不动产sj
        List<SJ_Execute_depart> executeDeparts = getExecuteDeparts(obj1);
        JSONArray departArray=JSONArray.fromObject(executeDeparts);
        sjSjsq.setExecuteDeparts(executeDeparts);
        String qzh="";
        if (null != jsonArray) {
            for (int i = 0; i < jsonArray.size(); i++) {
                com.alibaba.fastjson.JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                //设置serviceInfo信息
                RealPropertyCertificate realPropertyCertificate = realEstateMortgageComponent.getRealPropertyCertificatexx(jsonObject1, obj1);
                if (StringUtils.isNotBlank(realPropertyCertificate.getImmovableCertificateNo())){
                    qzh=realPropertyCertificate.getImmovableCertificateNo(); //获取权证号
                }
                realPropertyCertificateList.add(realPropertyCertificate);
            }
        }
        RespServiceData respServiceData = new RespServiceData();
        respServiceData.setServiceCode(serviceCode);
        List<RespServiceData> serviceDatas = new ArrayList<>();
        if (flag == true) {
            zlSJsqDbSj(serviceDatas);
            //根据权证号获取信息
            ParametricData parametricData=new ParametricData();
            parametricData.setBdczh(qzh);
            List<SJ_Book_Pic_ext> bookPics=new ArrayList<>();
            String path=DateUtils.getNowYear() + File.separator + DateUtils.getNowMonth() + File.separator + DateUtils.getNowDay();
            try {
                //权属信息
                List<SJ_Info_Bdcqlxgxx> bdcqlxgxxList=exchangeToInnerComponent.getBdcQlInfoWithItsRights(parametricData);
                log.info("获得权属信息为："+ com.alibaba.fastjson.JSONArray.toJSONString(bdcqlxgxxList));
                HandleAttachementDiagram(bdcqlxgxxList,jsonObject,registerNumber,ftpAddress,ftpPort,ftpUsername,ftpPassword,path);//附件上传
                log.info("处理权属信息为："+ com.alibaba.fastjson.JSONArray.toJSONString(bdcqlxgxxList));
                respServiceData.setServiceDataInfos(bdcqlxgxxList);
                serviceDatas.add(respServiceData);
                //抵押信息
                List<MortgageService> mortgageServiceList = new ArrayList<MortgageService>();
                for(SJ_Info_Bdcqlxgxx bdcqlxgxx:bdcqlxgxxList){
                    if(bdcqlxgxx.getItsRightVoList()!=null && bdcqlxgxx.getItsRightVoList().size()>0){
                        log.info("进入抵押信息获取");
                        for(SJ_Its_Right itsRight : bdcqlxgxx.getItsRightVoList()){
                            if(StringUtils.isNotBlank(itsRight.getItsRightType()) && "抵押".equals(itsRight.getItsRightType())) {
                                Map<String,Object> map = new HashMap<>();
                                if(StringUtils.isNotBlank(itsRight.getRegisterNumber())) {
                                    String slbh = itsRight.getRegisterNumber();
                                    if(slbh.contains("-"))
                                        slbh = slbh.substring(0,slbh.lastIndexOf("-"));
                                    map.put("slbh", slbh);
                                    //发送登记局获取数据整理发送一窗受理
                                    String json = httpClientUtils.doGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetCertificateInfo", map, null);
                                    log.info("获取登簿数据成功，为：" + json);
                                    JSONObject jsonObjectDy = JSONObject.fromObject(json);
                                    JSONArray ficateInfoArray = jsonObjectDy.getJSONArray("certificateInfoVoList");
                                    log.info("sit:" + jsonObjectDy.getString("sit") + ";slbh:" + jsonObjectDy.getString("slbh") + "certificateInfoVoList:" + ficateInfoArray);
                                    for (int i = 0; i < ficateInfoArray.size(); i++) {
                                        JSONObject verfyInfoObject = ficateInfoArray.getJSONObject(i);
                                        if(
                                                StringUtils.isNotBlank(verfyInfoObject.getString("certificateType")) &&
                                                        "DYZMH".equals(verfyInfoObject.getString("certificateType"))
                                        ) {
                                            RespServiceData RealEstateBookData = new RespServiceData();
                                            RespServiceData getRealEstateBooking = getRealEstateBooking(verfyInfoObject.getString("certificateType"),
                                                    verfyInfoObject.getString("certificateId"), RealEstateBookData);
                                            if (getRealEstateBooking.getServiceDataInfos() != null) {
                                                for (MortgageService mortgageServiceInfo : (List<MortgageService>) getRealEstateBooking.getServiceDataInfos()) {
                                                    mortgageServiceList.add(mortgageServiceInfo);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        bdcqlxgxx.setItsRightVoList(new ArrayList<SJ_Its_Right>());
                    }
                }
                if(mortgageServiceList!=null && mortgageServiceList.size()>0){
                    RespServiceData mortgageServiceData = new RespServiceData();
                    mortgageServiceData.setServiceCode(Msgagger.DYZMHSERVICE_CODE);
                    mortgageServiceData.setServiceDataInfos(mortgageServiceList);
                    serviceDatas.add(mortgageServiceData);
                }
                sjSjsq.setServiceDatas(serviceDatas);
                log.info("最终传入的数据为："+ com.alibaba.fastjson.JSONObject.toJSONString(sjSjsq));
                JSONArray serviceArray = JSONArray.fromObject(serviceDatas);
                log.info("serviceArray"+serviceArray.toString());
                stringMap.put("serviceDatas", serviceArray.toString());
                stringMap.put("executeDeparts",departArray.toString());
                log.info("实际传入的数据为："+ com.alibaba.fastjson.JSONObject.toJSONString(stringMap));
            }catch (Exception e){
              log.error("错误信息"+e);
            }
        } else {
            respServiceData.setServiceDataInfos(realPropertyCertificateList);
            serviceDatas.add(respServiceData);
            //添加土地证
            sjSjsq.setServiceDatas(serviceDatas);
            sjSjsq.setRegisterNumber(registerNumber);
            JSONObject sjsqObject = JSONObject.fromObject(sjSjsq);
            stringMap.put("SJ_Sjsq", sjsqObject.toString());//收件
        }
    }

    public List<SJ_Execute_depart> getExecuteDeparts(com.alibaba.fastjson.JSONObject obj1) {
        List<SJ_Execute_depart> departs = new ArrayList<SJ_Execute_depart>();
        if (obj1 == null) {
            return null;
        }
        if (StringUtils.isNotBlank(obj1.getString("gsdw"))) {
            SJ_Execute_depart depart_s = new SJ_Execute_depart();
            depart_s.setExecuteDepart(obj1.getString("gsdw"));
            departs.add(depart_s);
        }
        if (StringUtils.isNotBlank(obj1.getString("gddw"))) {
            SJ_Execute_depart depart_d = new SJ_Execute_depart();
            depart_d.setExecuteDepart(obj1.getString("gddw"));
            departs.add(depart_d);
        }
        if (StringUtils.isNotBlank(obj1.getString("gqdw"))) {
            SJ_Execute_depart depart_q = new SJ_Execute_depart();
            depart_q.setExecuteDepart(obj1.getString("gqdw"));
            departs.add(depart_q);
        }
        if (StringUtils.isNotBlank(obj1.getString("thhh"))) {
            SJ_Execute_depart depart_q = new SJ_Execute_depart();
            depart_q.setExecuteDepart(obj1.getString("thhh"));
            departs.add(depart_q);
        }
        return departs;
    }

    public Object sqUploadPdf(com.alibaba.fastjson.JSONArray jsonArray,String address, String port, String username, String password, String path) {
        log.info("address1"+address);
        log.info("post1"+port);
        log.info("username1"+username);
        log.info("password1"+password);
        List<SJ_File> sjFileList = new ArrayList<>();
        Object uploadObject = null;
        if (null != jsonArray && jsonArray.size() >0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                com.alibaba.fastjson.JSONObject fileObject = jsonArray.getJSONObject(i);
                String fileAddress = fileObject.getString("fileAddress");
                String fileType = fileObject.getString("fileType");
                byte[] bytes = bdcFTPDownloadComponent.downFile(StrUtil.getFTPRemotePathByFTPPath(fileAddress), StrUtil.getFTPFileNameByFTPPath(fileAddress), null, address, port, username, password);//连接一窗受理平台ftp
                uploadObject = toFTPUploadComponent.ycslUpload(bytes,DateUtils.getNowYear() + DateUtils.getNowMonth() + DateUtils.getNowDay() + IDUtil.getUUId()+"."+fileType, fileType, path,gxftpAddress,gxftpPort,gxftpUsername,gxftpPassword);//获取上传路径和名称
                if (uploadObject == null) {
                    log.error(Msgagger.FILE_FAIL);
                    throw new ZtgeoBizException(Msgagger.FILE_FAIL);
                }
                Map<String, Object> map = (Map<String, Object>) uploadObject;
                log.info("path:" + map.get("path").toString());
                log.info("fileName" + map.get("fileName").toString());
                //覆盖原有url  名称
                SJ_File sj_file = new SJ_File();
                String adress=map.get("path").toString() + "\\" + map.get("fileName").toString();
                String fileAdress=adress.replaceAll("\\\\","/");
                log.info("fileAdress:"+fileAdress);
                sj_file.setFileAddress(fileAdress);
                sj_file.setFileName(map.get("fileName").toString());
                sj_file.setFileType(fileType);
                sj_file.setFileSequence(fileObject.getString("fileSequence"));
                sj_file.setpName(fileObject.getString("pName"));
                sjFileList.add(sj_file);
            }
        }
        return sjFileList;
    }


    public void HandleAttachementDiagram(List<SJ_Info_Bdcqlxgxx> sj_info_bdcqlxgxxList,com.alibaba.fastjson.JSONObject jsonObject,String slbh,String address, String port, String username, String password,String path) {
        com.alibaba.fastjson.JSONArray fileArray = null;
        List<SJ_Book_Pic_ext> sjBookPicExtList=new ArrayList<>();
        Object uploadObject = null;
        try {
        if (null != jsonObject) {
            if (null != jsonObject.getJSONArray("householdMapInfoList") && jsonObject.getJSONArray("householdMapInfoList").size() != 0) {
                fileArray = jsonObject.getJSONArray("householdMapInfoList");
                if (null != fileArray && fileArray.size() != 0) {
                    for (int i = 0; i < fileArray.size(); i++) {
                        com.alibaba.fastjson.JSONObject fileObject = fileArray.getJSONObject(i);
                        String fileAddress = fileObject.getString("fileAddress");
                        String fileType = fileObject.getString("fileType");
                        byte[] bytes = bdcFTPDownloadComponent.downFile(StrUtil.getFTPRemotePathByFTPPath(fileAddress), StrUtil.getFTPFileNameByFTPPath(fileAddress), null, address, port, username, password);//连接一窗受理平台ftp
                        uploadObject = toFTPUploadComponent.ycslUpload(bytes, StrUtil.getFTPFileNameByFTPPath(fileAddress), fileType, path, yftpAddress, yftpPort, yftpUsername, yftpPassword);//获取上传路径和名称
                        if (uploadObject == null) {
                            log.error(Msgagger.FILE_FAIL);
                            throw new ZtgeoBizException(Msgagger.FILE_FAIL);
                        }
                        Map<String, Object> map = (Map<String, Object>) uploadObject;
                        //覆盖原有url  名称
                        SJ_Book_Pic_ext sjBookPicExt = new SJ_Book_Pic_ext();
                        sjBookPicExt.setPicName(fileObject.getString("fileName"));
                        sjBookPicExt.setBdcdyh(fileObject.getString("bdcdyh"));
                        sjBookPicExt.setPicType(Msgagger.FCFHT);
                        sjBookPicExt.setInsertTime(null);
                        SJ_Fjfile sj_file = new SJ_Fjfile();
                        sj_file.setFtpPath(map.get("path").toString() + "\\" + map.get("fileName").toString());
                        sj_file.setFileName(map.get("fileName").toString());
                        sj_file.setFileSubmissionTime(new Date());
                        sj_file.setFileExt(fileType);
                        sjBookPicExt.setFile(sj_file);
                        sjBookPicExtList.add(sjBookPicExt);
                    }
                }
            }
        }
        if (null != jsonObject.getJSONArray("landMapInfoList") && jsonObject.getJSONArray("landMapInfoList").size() != 0) {
            fileArray = jsonObject.getJSONArray("landMapInfoList");
            for (int i = 0; i < fileArray.size(); i++) {
                com.alibaba.fastjson.JSONObject fileObject = fileArray.getJSONObject(i);
                String fileAddress = fileObject.getString("fileAddress");
                String fileType = fileObject.getString("fileType");
                byte[] bytes = bdcFTPDownloadComponent.downFile(StrUtil.getFTPRemotePathByFTPPath(fileAddress), StrUtil.getFTPFileNameByFTPPath(fileAddress), null, address, port, username, password);//连接一窗受理平台ftp
                uploadObject = toFTPUploadComponent.ycslUpload(bytes, StrUtil.getFTPFileNameByFTPPath(fileAddress), fileType, path, yftpAddress, yftpPort, yftpUsername, yftpPassword);//获取上传路径和名称
                if (uploadObject == null) {
                    log.error(Msgagger.FILE_FAIL);
                    throw new ZtgeoBizException(Msgagger.FILE_FAIL);
                }
                Map<String, Object> map = (Map<String, Object>) uploadObject;
                //覆盖原有url  名称
                SJ_Book_Pic_ext sjBookPicExt = new SJ_Book_Pic_ext();
                sjBookPicExt.setPicName(fileObject.getString("fileName"));
                sjBookPicExt.setBdcdyh(fileObject.getString("bdcdyh"));
                sjBookPicExt.setPicType(Msgagger.ZDT);
                sjBookPicExt.setInsertTime(null);
                SJ_Fjfile sj_file = new SJ_Fjfile();
                sj_file.setFtpPath(map.get("path").toString() + "\\" + map.get("fileName").toString());
                sj_file.setFileName(map.get("fileName").toString());
                sj_file.setFileExt(fileType);
                sj_file.setFileSubmissionTime(new Date());
                sjBookPicExt.setFile(sj_file);
                sjBookPicExtList.add(sjBookPicExt);
            }
        }
        }catch (Exception ex){
            sj_info_bdcqlxgxxList.get(0).setBookPics(null);
        }
        if (null != sjBookPicExtList  && sjBookPicExtList.size()!=0) {
            sj_info_bdcqlxgxxList.get(0).setBookPics(sjBookPicExtList);
        }
    }


    /**
     * 处理
     *ftpAddress,ftpPort,ftpUsername,ftpPassword
     * @param jsonObject
     * @return
     */
    public void getProcessingAnnex(com.alibaba.fastjson.JSONObject jsonObject ,Map<String, String> stringMap, List<FileInfoVo> fileInfoVoList, String address, String port, String username, String password,String path) {
        com.alibaba.fastjson.JSONArray fileArray=null;
        log.info("jsonObject"+jsonObject);
        log.info("address"+address);
        log.info("post"+port);
        log.info("username"+username);
        log.info("password"+password);
        List<SJ_File> sjFileList = new ArrayList<>();
        Object uploadObject=null;
        Object djptObject=null;
        if (null != jsonObject && null == fileInfoVoList) {
            fileArray = jsonObject.getJSONArray("fileInfoList");
            log.info("fileArraySize"+fileArray.size());
            for (int i = 0; i < fileArray.size(); i++) {
                com.alibaba.fastjson.JSONObject fileObject = fileArray.getJSONObject(i);
                String fileAddress = fileObject.getString("fileAddress");
                log.info("fileAdress:" + fileObject.getString("fileAddress"));
                log.info("fileType:" + fileObject.getString("fileType"));
                String fileType = fileObject.getString("fileType");
                byte[] bytes = bdcFTPDownloadComponent.downFile(StrUtil.getFTPRemotePathByFTPPath(fileAddress), StrUtil.getFTPFileNameByFTPPath(fileAddress), null, address, port, username, password);//连接一窗受理平台ftp
                uploadObject = toFTPUploadComponent.ycslUpload(bytes, StrUtil.getFTPFileNameByFTPPath(fileAddress), fileType,path,yftpAddress,yftpPort,yftpUsername,yftpPassword);//获取上传路径和名称
                if (uploadObject == null) {
                    log.error(Msgagger.FILE_FAIL);
                    throw new ZtgeoBizException(Msgagger.FILE_FAIL);
                }
                Map<String, Object> map = (Map<String, Object>) uploadObject;
                log.info("path:" + map.get("path").toString());
                log.info("fileName" + map.get("fileName").toString());
                //覆盖原有url  名称
                SJ_File sj_file = new SJ_File();
                sj_file.setFileAddress(map.get("path").toString() + "\\" + map.get("fileName").toString());
                sj_file.setFileName(map.get("fileName").toString());
                sj_file.setFileType(fileType);
                sj_file.setFileSequence(fileObject.getString("fileSequence"));
                sj_file.setpName(fileObject.getString("pName"));
                sjFileList.add(sj_file);
            }
        }else {
            fileArray = com.alibaba.fastjson.JSONArray.parseArray(com.alibaba.fastjson.JSONObject.toJSONString(fileInfoVoList));
            for (int i = 0; i < fileArray.size(); i++) {
                com.alibaba.fastjson.JSONObject fileObject = fileArray.getJSONObject(i);
                String fileAddress="";
                fileAddress  = fileObject.getString("fileAddress");
                if (StringUtils.isEmpty(fileAddress)){
                    fileAddress=fileObject.getString("fileAdress");
                }
                String fileType = fileAddress.substring(fileAddress.lastIndexOf(".") + 1);
                byte[] bytes = bdcFTPDownloadComponent.downFile(StrUtil.getFTPRemotePathByFTPPath(fileAddress), StrUtil.getFTPFileNameByFTPPath(fileAddress), null, address, port, username, password);//连接一窗受理平台ftp
                log.info("ftpAdress"+StrUtil.getFTPRemotePathByFTPPath(fileAddress));
                uploadObject = toFTPUploadComponent.ycslUpload(bytes, StrUtil.getFTPFileNameByFTPPath(fileAddress), fileType,path,yftpAddress,yftpPort,yftpUsername,yftpPassword);//获取上传路径和名称
                if (uploadObject == null) {
                    log.error(Msgagger.FILE_FAIL);
                    throw new ZtgeoBizException(Msgagger.FILE_FAIL);
                 }
                //登记平台的ftp
                djptObject = toFTPUploadComponent.ycslUpload(bytes, StrUtil.getFTPFileNameByFTPPath(fileAddress), fileType,path,ftpAddress,ftpPort,ftpUsername,ftpPassword);//获取上传路径和名称
                if (djptObject == null) {
                    log.error(Msgagger.FILE_FAIL);
                    throw new ZtgeoBizException(Msgagger.FILE_FAIL);
                }
//                log.info("path:" + map.get("path").toString());
//                log.info("fileName" + map.get("fileName").toString());
                //覆盖原有url  名称
//                SJ_File sj_file = new SJ_File();
//                sj_file.setFileAddress(map.get("path").toString() + "\\" + map.get("fileName").toString());
//                sj_file.setFileName(map.get("fileName").toString());
//                sj_file.setFileType(fileType);
//                sj_file.setFileSequence(fileObject.getString("fileSequence"));
//                sj_file.setpName(FileTypeEnum.Sc(fileObject.getString("fileType")));
//                sjFileList.add(sj_file);
//                log.info("sjFileList"+sjFileList.size());
            }
        }

        JSONArray jsonArray = JSONArray.fromObject(sjFileList);
        stringMap.put("fileVoList", jsonArray.toString());
    }


    public void GetReceiving(GetReceiving getReceiving, OutputStream outputStream) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ReturnVo returnVo = new ReturnVo();
        Map<String, String> mapParmeter = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            public String call() throws Exception { //建议抛出异常
                try {
                    com.alibaba.fastjson.JSONObject tokenObject;
                    String token="";
                    log.info("执行主线程");
                    if (StringUtils.isEmpty(getReceiving.getUserCodeYCSL()) || getReceiving.getUserCodeYCSL().equals("1")) {
                        log.info("获取用户"+tsryname+"token");
                        tokenObject = httpCallComponent.getTokenYcsl(tsryname, tsrypaaword);//获得token
                        token = getToken(tokenObject, "GetReceiving", getReceiving.getSlbh(), getReceiving.getMessageType(), null);
                    }else {
                        log.info("获取用户"+bsryname+"token");
                        tokenObject = httpCallComponent.getTokenYcsl(bsryname, bsrypassword);//获得token
                        token = getToken(tokenObject, "getRegistrationBureau", getReceiving.getSlbh(), getReceiving.getMessageType(), null);
                    }
                    if (token == null) {
                        return Msgagger.USER_LOGIN_BAD;
                    }
                    log.info("type"+getReceiving.getMessageType());
                    if (getReceiving.getMessageType().equals(Msgagger.VERIFYNOTICE)) {//审核
                        log.info("进入审核");
                        map.put("slbh", getReceiving.getSlbh());
                        //发送登记局获取数据整理发送一窗受理
                        String json = httpClientUtils.doGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetVerifyInfo", map, null);
                        log.info("json"+json);
                        JSONObject jsonObject = JSONObject.fromObject(json);
                        JSONArray verfyInfoArray = jsonObject.getJSONArray("verifyInfoVoList");
                        List<SJ_Info_Handle_Result> handleResultVoList = new ArrayList<>();
                        List<RespServiceData> serviceDatas = new ArrayList<>();
                        for (int i = 0; i < verfyInfoArray.size(); i++) {
                            JSONObject verfyInfoObject = verfyInfoArray.getJSONObject(i);
                            SJ_Info_Handle_Result handleResult = new SJ_Info_Handle_Result();
                            handleResult.setHandleText(verfyInfoObject.getString("verifyOpinion"));
                            if (null == verfyInfoObject.getString("registerSubType")){
                                handleResult.setHandleResult(Msgagger.ADOPT);
                            }
                            handleResult.setHandleResult(verfyInfoObject.getString("registerSubType") + Msgagger.ADOPT);
                            handleResult.setProvideUnit(Msgagger.REGISTRATION);
                            handleResult.setDataComeFromMode(Msgagger.SUCCESSFUL_INTERFACE);
                            handleResultVoList.add(handleResult);
                        }
                        RespServiceData respServiceData = new RespServiceData();
                        respServiceData.setServiceCode(immovableHandleResultService);
                        respServiceData.setServiceDataInfos(handleResultVoList);
                        serviceDatas.add(respServiceData);
                        JSONArray jsonArray = JSONArray.fromObject(serviceDatas);
                        System.out.println("servicedatas" + jsonArray);
                        mapParmeter.put("serviceDatas", jsonArray.toString());
                        mapParmeter.put("registerNumber", jsonObject.getString("slbh"));//受理编号)
                    } else if (getReceiving.getMessageType().equals(Msgagger.RESULTNOTICE)) {//登簿审核
                        log.info("进入登簿");
                        map.put("slbh", getReceiving.getSlbh());
                        log.info("获取token成功，为："+token);
                        //发送登记局获取数据整理发送一窗受理
                        String json = httpClientUtils.doGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetCertificateInfo", map, null);
                        log.info("获取登簿数据成功，为："+json);
                        JSONObject jsonObject = JSONObject.fromObject(json);
                        mapParmeter.put("immovableSite", jsonObject.getString("sit"));
                        mapParmeter.put("registerNumber", jsonObject.getString("slbh"));
                        //登簿返回信息
                        JSONArray ficateInfoArray = jsonObject.getJSONArray("certificateInfoVoList");
                        log.info("sit:"+jsonObject.getString("sit")+";slbh:"+jsonObject.getString("slbh")+"certificateInfoVoList:"+ficateInfoArray);
                        List<SJ_Info_Handle_Result> handleResultVoList = new ArrayList<>();
                        List<RespServiceData> respServiceDataList = new ArrayList<>();
                        List<String> stringList = new ArrayList<>();
                        RespServiceData resultServiceData=null;
                        String dbName="";
                        //获取受理编号信息
                        for (int i = 0; i < ficateInfoArray.size(); i++) {
                            JSONObject verfyInfoObject = ficateInfoArray.getJSONObject(i);
                            dbName=verfyInfoObject.getString("registerSubType")+",";
                            stringList.add(verfyInfoObject.getString("registerSubType"));
                            RespServiceData RealEstateBookData = new RespServiceData();
                            RespServiceData getRealEstateBooking = getRealEstateBooking(verfyInfoObject.getString("certificateType"),
                                    verfyInfoObject.getString("certificateId"), RealEstateBookData);
                            if (verfyInfoObject.getString("registerSubType").equals(Msgagger.DYZXDJ)){
                                getRealEstateBooking.setServiceCode(Msgagger.DYZXSERVICECODE);
                                resultServiceData=getRealEstateBooking;
                            }else if (StringUtils.isEmpty(getRealEstateBooking.getServiceCode())){
                                getRealEstateBooking.setServiceCode(Msgagger.DYZMHSERVICE_CODE);
                            }
                            respServiceDataList.add(getRealEstateBooking);//不动产展示登簿信息
                        }
                        JSONArray respService=JSONArray.fromObject(respServiceDataList);
                        mapParmeter.put("serviceData",respService.toString());
                        RespServiceData respServiceData = new RespServiceData();
                        respServiceData.setServiceCode(Msgagger.DBSERVICECODE);
                        String name = dbName.substring(0, dbName.length() - 1);
                        //获取登记小类信息
                        SJ_Info_Handle_Result handleResult = new SJ_Info_Handle_Result();
                        handleResult.setHandleResult(name + "登簿成功");
                        handleResult.setHandleText(Msgagger.SUCCESSFUL_DENGBUCG);
                        handleResult.setProvideUnit(Msgagger.REGISTRATION);
                        handleResult.setDataComeFromMode(Msgagger.SUCCESSFUL_INTERFACE);
                        handleResultVoList.add(handleResult);
                        respServiceData.setServiceDataInfos(handleResultVoList);
                        log.info("resultServiceData1"+resultServiceData);
                        log.info("resultServiceData2:\n"+respServiceData.getServiceDataInfos());
                        String [] clxx= name.split(",");
                        for (int i=0;i<clxx.length;i++) {
                            if (clxx.length==1 && clxx[i].equals("一般抵押权")){
                                log.info("抵押注销通知进来了");
                                List<MortgageService> mortgageServiceList = resultServiceData.getServiceDataInfos();
                                ResultNoticeReqVo resultNoticeReqVo = new ResultNoticeReqVo();
                                resultNoticeReqVo.setBusinessId(getReceiving.getSlbh());
                                ClNotice(resultNoticeReqVo, "REVOKE_REGISTER",Msgagger.DENGBU);
                                ClDdyxxNotice(mortgageServiceList, resultNoticeReqVo);
                                JSONObject bankObject=JSONObject.fromObject(resultNoticeReqVo);
                                log.info("bankObject"+bankObject.toString());
                                String resultJson= BankNotification(bankObject,"JSRCIS/sq/sqResultNotice.do");
                                log.info("银行返回json"+resultJson);
                            }
                        }
                        respServiceDataList.add(respServiceData);
                        JSONArray jsonArray = JSONArray.fromObject(respServiceDataList);
                        mapParmeter.put("serviceDatas", jsonArray.toString());
                    } else if (getReceiving.getMessageType().equals(Msgagger.ACCPETNOTICE)) {
                        log.info("执行受理操作");
                        mapParmeter.put("registerNumber", getReceiving.getSlbh());
                    } else if (getReceiving.getMessageType().equals(Msgagger.PROCESSING)) {//缮证
                        log.info("缮证");
                        map.put("slbh", getReceiving.getSlbh());
                        String json = httpClientUtils.doGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetCertificateInfo", map, null);
                        log.info("szJson"+json);
                        JSONObject jsonObject = JSONObject.fromObject(json);
                        JSONObject bankObject=null;
                        if (jsonObject.getJSONArray("certificateInfoVoList") == null){
                            log.error("登记平台无法查询到受理编号信息");
                        }
                        //登簿返回信息
                        JSONArray ficateInfoArray = jsonObject.getJSONArray("certificateInfoVoList");
                        List<ResultNoticeReqVo> resultNoticeReqVoList=new ArrayList<>();
                        log.info("1");
                        //获取受理编号信息
                        for (int i = 0; i < ficateInfoArray.size(); i++) {
                            JSONObject verfyInfoObject = ficateInfoArray.getJSONObject(i);
                            ResultNoticeReqVo resultNoticeReqVo = getDataProcessing(getReceiving.getSlbh(), verfyInfoObject.getString("certificateType"),
                                    verfyInfoObject.getString("certificateId"));
                            resultNoticeReqVoList.add(resultNoticeReqVo);
                        }
                        log.info("2");
                        List<MortgagorInfoVo> mortgagorInfoVoList=new ArrayList<>();
                        List<MortgageeInfoVo> mortgageeInfoVoList=new ArrayList<>();
                        ResultNoticeReqVo dyResultNotice=null;
                        if (resultNoticeReqVoList.size()>1){
                            Iterator<ResultNoticeReqVo> iterator = resultNoticeReqVoList.iterator();
                            while (iterator.hasNext()) {
                                ResultNoticeReqVo resultNoticeReqVo = iterator.next();
                                       //判断是否为
                                if (null != resultNoticeReqVo.getWarrantId() && !resultNoticeReqVo.getWarrantId().equals("")) {
                                    mortgagorInfoVoList = resultNoticeReqVo.getMortgagorInfoVoList();
                                    mortgageeInfoVoList = resultNoticeReqVo.getMortgageeInfoVoList();
                                    dyResultNotice = resultNoticeReqVo;
                                    iterator.remove();
                                }
                            }
                            log.info("3");
                        resultNoticeReqVoList.get(0).setMortgagorInfoVoList(mortgagorInfoVoList);
                        resultNoticeReqVoList.get(0).setMortgageeInfoVoList(mortgageeInfoVoList);
                        resultNoticeReqVoList.get(0).setMortagageLandArea(dyResultNotice.getMortagageLandArea());
                        resultNoticeReqVoList.get(0).setMortgageArea(dyResultNotice.getMortgageArea());
                        resultNoticeReqVoList.get(0).setWarrantId(dyResultNotice.getWarrantId());
                        resultNoticeReqVoList.get(0).setCreditAmount(dyResultNotice.getCreditAmount());
                        resultNoticeReqVoList.get(0).setMortgageTerm(dyResultNotice.getMortgageTerm());
                        }
                        log.info("resultNoticeReqVoList"+resultNoticeReqVoList.get(0).toString());
                        //获取附件信息
                        List<String> stringList=new ArrayList<>();
                        stringList.add(itemName);
                        EsfSdq esfSdq = new EsfSdq();
                        esfSdq.setAttDirList(stringList);
                        esfSdq.setSlbh(getReceiving.getSlbh());
                        JSONObject paramObject = JSONObject.fromObject(esfSdq);//整理参数信息
                        log.info("aa"+paramObject.toString());
                        //根据受理编号查询转移信息（水电气）
                        String zyxx = HttpClientUtils.getJsonData(paramObject, "http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetAttachList4Bdc");
                        log.info("zyxx"+zyxx);
                        if (!zyxx.equals("[]\n")) {
                            com.alibaba.fastjson.JSONArray jsonArray = (com.alibaba.fastjson.JSONArray) com.alibaba.fastjson.JSONArray.parse(zyxx);
                            List<com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.FileInfoVo> fileInfoVoList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.FileInfoVo fileInfoVo = new com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.FileInfoVo();
                                com.alibaba.fastjson.JSONObject fileObject = jsonArray.getJSONObject(i);
                                String fileAddress = fileObject.getString("fileAddress");
                                //覆盖原有url  名称
                                log.info("fileAdress:"+fileAddress);
                                fileInfoVo.setFileAdress(fileAddress);
                                fileInfoVo.setFileName(fileObject.get("fileName").toString());
                                log.info("fileAdress:"+fileInfoVo.getFileName());
                                fileInfoVo.setFileType(Msgagger.FILE_TYPE);
                                fileInfoVo.setFileSequence(fileObject.getString("fileSequence"));
                                fileInfoVoList.add(fileInfoVo);
                                log.info("fiileInfoVoList"+fileInfoVoList.size());
                            }
                            resultNoticeReqVoList.get(0).setFileInfoVoList(fileInfoVoList);
                        }
                        log.info("开始整理数据");
                        bankObject=JSONObject.fromObject(resultNoticeReqVoList.get(0));
                        log.info("bankObject"+bankObject.toString());
                        String resultJson= BankNotification(bankObject,"JSRCIS/sq/sqResultNotice.do");
                        log.info("resultJson"+resultJson);
                        return resultJson;
                    }
                    //返回数据到一窗受理平台保存受理编号和登记编号
                    String resultJson = preservationRegistryData(mapParmeter, token, "/api/biz/RecService/DealRecieveFromOuter2");
                   log.info("result is " + resultJson);
                    return resultJson;
                } catch (Exception e) {
                    log.error("e"+e);
                    e.printStackTrace();
                    throw new Exception("Callable terminated with Exception!"); // call方法可以抛出异常
                }
            }
        });
        executor.execute(future);
        long t = System.currentTimeMillis();
//        try {
        returnVo.setCode(200);
        returnVo.setMessage(Msgagger.CG);
        JSONObject object = JSONObject.fromObject(returnVo);
        outputStream.write(object.toString().getBytes("UTF-8"));
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
     *
     * @return
     */
    private RespServiceData getRealEstateBooking(String certificateType, String certificateId, RespServiceData respServiceData) throws Exception {
        ObjectRestResponse resultRV = new ObjectRestResponse();
            switch (certificateType) {
                case "DYZMH":
                    resultRV = realEstateMortgageComponent.getRealEstateMortgage(certificateId, null, true);
                    List<MortgageService> mortgageServiceList = (List<MortgageService>) resultRV.getData();
                    respServiceData.setServiceDataInfos(mortgageServiceList);
                    break;
                case "YGZMH":
                    resultRV = realEstateMortgageComponent.getMortgageCancellation(certificateId);
                    List<RealPropertyCertificate> realPropertyCertificateList = (List<RealPropertyCertificate>) resultRV.getData();
                    respServiceData.setServiceCode(Msgagger.YGZMSERVICE_CODE);
                    respServiceData.setServiceDataInfos(realPropertyCertificateList);
                    break;
                case "BDCZH":
                    ParametricData parametricData=new ParametricData();
                    parametricData.setBdczh(certificateId);
                    List<SJ_Info_Bdcqlxgxx> bdcqlxgxxList = exchangeToInnerComponent.getBdcQlInfoWithItsRights(parametricData);
                    for(SJ_Info_Bdcqlxgxx bdcqlxgxx:bdcqlxgxxList){
                        bdcqlxgxx.setItsRightVoList(new ArrayList<SJ_Its_Right>());
                    }
                    respServiceData.setServiceCode(Msgagger.BDCQZSDZF_SERVICE_CODE);
                    respServiceData.setServiceDataInfos(bdcqlxgxxList);
                    break;
            }
        return respServiceData;
    }

    /**
     *缮证
     */
    private ResultNoticeReqVo  getDataProcessing(String slbh,String certificateType, String certificateId)throws Exception{
        ObjectRestResponse resultRV = new ObjectRestResponse();
        ResultNoticeReqVo resultNoticeReqVo=new ResultNoticeReqVo();
        resultNoticeReqVo.setBusinessId(slbh);
        switch (certificateType) {
            case "DYZMH":
                resultRV = realEstateMortgageComponent.getRealEstateMortgage(certificateId,null,true);
                log.info("resultRV"+resultRV.getData());
                List<MortgageService> mortgageServiceList = (List<MortgageService>) resultRV.getData();
                //处理抵押
                ClNotice(resultNoticeReqVo,"MORTGAGE_REGISTER",Msgagger.SHANZHENG);
                ClDdyxxNotice(mortgageServiceList,resultNoticeReqVo);
                break;
            case "YGZMH":
                resultRV = realEstateMortgageComponent.getMortgageCancellation(certificateId);
                List<RealPropertyCertificate> realPropertyCertificateList = (List<RealPropertyCertificate>) resultRV.getData();
                ClNotice(resultNoticeReqVo,"MORTGAGE_REGISTER",Msgagger.SHANZHENG);
                ClYgxxNotice(realPropertyCertificateList,resultNoticeReqVo,certificateId);
                break;
        }
        return  resultNoticeReqVo;
    }

    private void ClNotice(ResultNoticeReqVo resultNoticeReqVo,String Type,String businessNodeName){
        resultNoticeReqVo.setBusinessStatus("ACEPTE_SUCESS");
        resultNoticeReqVo.setBusinessType(Type);//MORTGAGE_PERSONAL
        resultNoticeReqVo.setBusinessNodeCode("1");//业务节点编码
        resultNoticeReqVo.setBusinessNodeName(businessNodeName);//业务节点名称
        resultNoticeReqVo.setCompletionTime(DateUtils.dateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        resultNoticeReqVo.setCharset("UTF-8");
        resultNoticeReqVo.setOrgId("01398999999");//宿迁交通银行固定值
        resultNoticeReqVo.setVersion("1.0.0");
        resultNoticeReqVo.setReqDate(DateUtils.dateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        resultNoticeReqVo.setReqUniqueNo(IDUtil.getExceptionId());
        resultNoticeReqVo.setApiName("resultNotice");
    }

    private void ClYgxxNotice(List<RealPropertyCertificate> realPropertyCertificateList,ResultNoticeReqVo resultNoticeReqVo,String certificateId){
        List< RealEstateInfoVo > realEstateInfoVoList=new ArrayList<>();
        List<com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.RealEstateUnitInfoVo> realEstateUnitInfoVos=new ArrayList<>();
        RealEstateInfoVo realEstateInfoVo = new RealEstateInfoVo();
        for (RealPropertyCertificate realPropertyCertificate:
             realPropertyCertificateList ) {
            realEstateInfoVo.setRealEstateId(realPropertyCertificate.getImmovableCertificateNo());//不动产权证号
            realEstateInfoVo.setLandCertificate(realPropertyCertificate.getLandCertificateNo());//土地号
            realEstateInfoVo.setVormerkungId(certificateId);//预告证明号
            List<GlImmovable> glImmovableList = realPropertyCertificate.getGlImmovableVoList();
            //不动产单元信息
            ClNoticeFwInfo(glImmovableList,realEstateUnitInfoVos,realEstateInfoVoList,realEstateInfoVo);
            resultNoticeReqVo.setRealEstateInfoVoList(realEstateInfoVoList);
        }
    }


    private void ClNoticeFwInfo(List<GlImmovable> glImmovableList,List<com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.RealEstateUnitInfoVo> realEstateUnitInfoVoList,
                                List<RealEstateInfoVo> realEstateInfoVoList, RealEstateInfoVo realEstateInfoVo ){
        for (GlImmovable glmmovable:
                glImmovableList) {
            com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.RealEstateUnitInfoVo realEstateUnitInfoVo=new com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.RealEstateUnitInfoVo();
            FwInfo fwInfo=glmmovable.getFwInfo();
            realEstateUnitInfoVo.setRealEstateUnitId(fwInfo.getImmovableUnitNumber());
            realEstateUnitInfoVo.setSit(fwInfo.getHouseLocation());
            realEstateUnitInfoVo.setHouseholdId(fwInfo.getHouseholdNumber());
            realEstateUnitInfoVoList.add(realEstateUnitInfoVo);
            realEstateInfoVo.setRealEstateUnitInfoVoList(realEstateUnitInfoVoList);
            realEstateInfoVoList.add(realEstateInfoVo);
        }
    }


    /**
     * 处理银行通知抵押信息
     * @param mortgageServiceList
     * @param resultNoticeReqVo
     */
    private void ClDdyxxNotice(List<MortgageService> mortgageServiceList,ResultNoticeReqVo resultNoticeReqVo){
        List<RealEstateInfoVo> realEstateInfoVoList=new ArrayList<>();
        List<com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain.RealEstateUnitInfoVo> realEstateUnitInfoVoList=new ArrayList<>();
        List<MortgagorInfoVo> mortgagorInfoVoList=new ArrayList<>();
        List<MortgageeInfoVo> mortgageeInfoVoList=new ArrayList<>();
        RealEstateInfoVo realEstateInfoVo = new RealEstateInfoVo();
        for (MortgageService mortgageService:
                mortgageServiceList ) {
            //  MORTGAGE_NEW_TRAILER
            resultNoticeReqVo.setWarrantId(mortgageService.getMortgageCertificateNo());//抵押证明号
            resultNoticeReqVo.setMortgageArea(mortgageService.getMortgageArea().toString());
            if (mortgageService.getMortgageStartingDate()!=null && mortgageService.getMortgageEndingDate()!=null){
                resultNoticeReqVo.setMortgageTerm(getMonthDiff(mortgageService.getMortgageStartingDate(),mortgageService.getMortgageEndingDate()));
            }
            List<GlImmovable> glImmovableList = mortgageService.getGlImmovableVoList();
            realEstateInfoVo.setRealEstateId(mortgageService.getImmovableCertificateNo());
            //不动产单元信息
            ClNoticeFwInfo(glImmovableList,realEstateUnitInfoVoList,realEstateInfoVoList,realEstateInfoVo);
            resultNoticeReqVo.setRealEstateInfoVoList(realEstateInfoVoList);
            List<GlMortgagor> glMortgagorVoList=mortgageService.getGlMortgagorVoList();
            for (GlMortgagor glMortgagor: glMortgagorVoList) { //抵押人信息
                MortgagorInfoVo mortgagorInfoVo=new MortgagorInfoVo();
                mortgagorInfoVo.setMortgagorNmae(glMortgagor.getObligeeName());
                mortgagorInfoVo.setMortgagorId(glMortgagor.getRelatedPerson().getObligeeDocumentNumber());
                mortgagorInfoVo.setMortgagorIdType(MortgagorPtypeEnum.Sc(glMortgagor.getRelatedPerson().getObligeeDocumentType()));
                mortgagorInfoVoList.add(mortgagorInfoVo);
            }
            resultNoticeReqVo.setMortgagorInfoVoList(mortgagorInfoVoList);
            List<GlMortgageHolder> glMortgageHolderVoList=mortgageService.getGlMortgageHolderVoList();
            for (GlMortgageHolder glMortgageHolder: glMortgageHolderVoList) { //抵押权人信息
                MortgageeInfoVo mortgageeInfoVo=new MortgageeInfoVo();
                mortgageeInfoVo.setMortgageeName(glMortgageHolder.getObligeeName());
                mortgageeInfoVo.setMortgageeId(glMortgageHolder.getRelatedPerson().getObligeeDocumentNumber());
                mortgageeInfoVo.setMortgageeIdType(MortgagorPtypeEnum.Sc(glMortgageHolder.getRelatedPerson().getObligeeDocumentType()));
                mortgageeInfoVoList.add(mortgageeInfoVo);
            }
            resultNoticeReqVo.setMortgageeInfoVoList(mortgageeInfoVoList);
        }
    }



    /**
     * 获取两个日期相差的月数
     */
    public static String getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
        return String.valueOf(monthsDiff);
    }



    public String getToken(com.alibaba.fastjson.JSONObject jsonObject, String method, String registerNumber, String noticeType, String receiptNumber) {
        Integer status = jsonObject.getInteger("status");
        if (status != 200) {
            ExceptionRecord exceptionRecord = new ExceptionRecord();
            exceptionRecord.setId(IDUtil.getExceptionId());//主键id
            exceptionRecord.setRegisterNumber(registerNumber);
            exceptionRecord.setReceiptNumber(receiptNumber);
            exceptionRecord.setNoticeType(noticeType);
            exceptionRecord.setHappenTime(new Date());
            exceptionRecord.setNoticeText(jsonObject.toString());
            exceptionRecord.setHandleStatus(Msgagger.TO_BE_PROCESSED);
            exceptionRecord.setNoticeUrl("http://" + machineIp + ":" + machinePost + "/" + method);
            exceptionRecordMapper.insertSelective(exceptionRecord);
            //记录失败信息
            log.error("用户名或密码错误,找不到对应用户");
            return null;
        }
        String data = jsonObject.getString("data");
        return data;
    }


    public String getRegistrationSubcategory(String registrationName) {
        String serviceCode = "";
        //判断模糊小类输出serviceCode
        if (registrationName.equals("抵押权注销登记")) {
            serviceCode = Msgagger.DBSERVICECODE;
        } else if (registrationName.equals("预告证明号登记")) {
            serviceCode = "ImmovableElectronicCertificate";
        } else if (registrationName.equals("")) {
            serviceCode = "";
        } else if (registrationName.equals("")) {
            serviceCode = "";
        }
        return serviceCode;
    }


    public String preservationRegistryData(Map<String, String> map, String token, String url) {
        Map<String, String> header = new HashMap<String, String>();
        log.info("windowAcceptanceIp"+windowAcceptanceIp);
        log.info("windowAcceptanceSeam"+windowAcceptanceSeam);
        log.info("url"+url);
        header.put("Authorization", token);
        String json = ParamHttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost,
                "application/json",
                "http://" + windowAcceptanceIp + ":" + windowAcceptanceSeam + url,
                map, header);
        JSONObject jsonObject=JSONObject.fromObject(json);
        System.out.println("chenbin返回信息为：" + jsonObject);
        return json;
    }


    public Object getUrl(JSONObject jsonObject) throws  IOException{
        String url="/register/api/IDServices";
        return  BankNotification(jsonObject,url);
    }


    private String BankNotification(JSONObject jsonObject,String url) throws IOException{
        String json= HttpClientUtil.post("e4f0fbe9ac9449d3bad6edee1be5626e","Cm1AhVSR",jsonObject.toString(),"http://" + jtIp + ":" + jtPost +"/"+url);
        log.info("yinhang返回信息为：" + json);
        return json;
    }


}
