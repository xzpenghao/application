package com.springboot.service.chenbin.other.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.Msgagger;
import com.springboot.entity.chenbin.personnel.other.other_depart.BookReturnEntity;
import com.springboot.entity.chenbin.personnel.other.other_depart.JFEntity;
import com.springboot.entity.chenbin.personnel.other.other_depart.JSEntity;
import com.springboot.entity.chenbin.personnel.tra.TraRespBody;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.service.chenbin.other.ExchangeInterfaceService;
import com.springboot.util.SysPubDataDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service("tzBiz")
public class ExchangeInterfaceServiceImpl implements ExchangeInterfaceService {

    @Autowired
    private OuterBackFeign backFeign;

    @Override
    public String sdqg2Inner(String commonInterfaceAttributer) throws ParseException {
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        String receiptNumber = sjsq.getReceiptNumber();
        String registerNumber = receiptNumber.replaceAll("YCSL-","");
        String preservationMan ="";
        List<SJ_Info_Handle_Result> handleResultVoList = sjsq.getHandleResultVoList();
        if(handleResultVoList!=null && handleResultVoList.size()>0){
            preservationMan = handleResultVoList.get(0).getPreservationMan();
        }
        System.out.println("正在获取操作用户token");
        String token = "";
        if(StringUtils.isBlank(preservationMan)) {
            token = backFeign.getToken(new JwtAuthenticationRequest("tsdjj", "123456")).getData();
        } else {
            token = backFeign.getToken(new JwtAuthenticationRequest(preservationMan, "123456")).getData();
        }
        if(StringUtils.isNotBlank(token)){
            Map<String, String> mapParmeter = new HashMap<>();
            mapParmeter.put("receiptNumber", receiptNumber);
            mapParmeter.put("registerNumber", registerNumber);
            System.out.println("操作用户token获取成功");
            System.out.println("执行水电气广业务号挂接程序");
            ObjectRestResponse<String> rv = backFeign.dealRecieveFromOuter1(token,mapParmeter);
            if(rv.getStatus()==200) {
                System.out.println("水电气广业务号挂接成功");
            }else{
                System.out.println("水电气广业务号挂接失败");
                return rv.getMessage();
            }
        }
        log.info("二手房转移登记业务："+receiptNumber+"完成向水电气广转件，平行系统侧产生办件实例号："+registerNumber);
        return "流程提交成功";
    }

    @Override
    public String firstReg2Inner(String commonInterfaceAttributer,String yhbs) {
        JSONObject obj = JSONObject.parseObject(commonInterfaceAttributer);
        String receiptNumber = obj.getString("receiptNumber");
        String registerNumber = obj.getString("receiptNumber").replaceAll("YCSL-","");
        Map<String, String> mapParmeter = new HashMap<>();
        mapParmeter.put("receiptNumber", receiptNumber);
        mapParmeter.put("registerNumber", registerNumber);
        System.out.println("正在获取操作用户token");
        String token = "";
        if(StringUtils.isNotBlank(yhbs) && yhbs.equals("qs")){
            token = backFeign.getToken(new JwtAuthenticationRequest("testswj","123456")).getData();
        }else if(StringUtils.isNotBlank(yhbs) && yhbs.equals("jy")){
            token = backFeign.getToken(new JwtAuthenticationRequest("testjy","123456")).getData();
        }else{
            token = backFeign.getToken(new JwtAuthenticationRequest("tsdjj","123456")).getData();
        }
        if(StringUtils.isNotBlank(token)){
            System.out.println("操作用户token获取成功");
            System.out.println("执行内网业务号挂接程序");
            ObjectRestResponse<String> rv = backFeign.dealRecieveFromOuter1(token,mapParmeter);
            if(rv.getStatus()==200) {
                System.out.println("内网业务号挂接成功");
            }else{
                System.out.println("内网业务号挂接失败");
                return rv.getMessage();
            }
        }
        log.info("登记业务："+receiptNumber+"完成办件转办，平行部门侧产生办件实例号："+registerNumber);
        return "流程提交成功";
    }

    @Override
    public String receiptSuccess(String slbh) {
        Map<String, String> mapParmeter = new HashMap<>();
        mapParmeter.put("registerNumber",slbh);
        System.out.println("正在获取操作用户token");
        String token = backFeign.getToken(new JwtAuthenticationRequest("tsdjj","123456")).getData();
        log.info("受理提交成功");
        return dealOut2(mapParmeter,token);
    }

    @Override
    public String JFSuccess(String slbh) {
        System.out.println("执行缴费外部触发");
        Map<String, String> mapParmeter = new HashMap<>();
        mapParmeter.put("registerNumber",slbh);
        List<SJ_Info_Handle_Result> handleResultVoList = new ArrayList<>();
        List<RespServiceData> serviceDatas = new ArrayList<>();
        RespServiceData serviceData = new RespServiceData();
        SJ_Info_Handle_Result handleResult = new SJ_Info_Handle_Result();
        JFEntity jf = new JFEntity("登记费","交费成功","￥420","测试开发商公司","2019-10-24 14:05:32");
        handleResult.setHandleText(JSONObject.toJSONString(jf));
        handleResult.setHandleResult(slbh+"号办件交费" + Msgagger.CG);
        handleResult.setProvideUnit("web+");
        handleResult.setDataComeFromMode("接口");
        handleResultVoList.add(handleResult);
        serviceData.setServiceCode("PaymentResultService");
        serviceData.setServiceDataInfos(handleResultVoList);
        serviceDatas.add(serviceData);
        mapParmeter.put("serviceDatas", JSONArray.toJSONString(serviceDatas));
        System.out.println("正在获取操作用户token");
        String token = backFeign.getToken(new JwtAuthenticationRequest("tsdjj","123456")).getData();
        String result = dealOut2(mapParmeter,token);
        log.info("执行缴费外部提交成功");
        return result;
    }

    @Override
    public String JSSuccess(String slbh) {
        return null;
    }

    @Override
    public String JFAndJSSuccess(String slbh) {
        System.out.println("正在获取操作用户token");
        String token = backFeign.getToken(new JwtAuthenticationRequest("testdjj","123456")).getData();

        System.out.println("执行缴税外部触发");
        Map<String, String> mapParmeter = new HashMap<>();
        mapParmeter.put("registerNumber",slbh);
        List<RespServiceData> serviceDatas = new ArrayList<>();

        RespServiceData serviceData1 = new RespServiceData();
        List<SJ_Info_Handle_Result> handleResultVoList1 = new ArrayList<>();
        SJ_Info_Handle_Result handleResult1 = new SJ_Info_Handle_Result();
        JFEntity jf = new JFEntity("登记费","交费成功","￥120","赵六","2019-10-24 14:05:32");
        handleResult1.setHandleText(JSONObject.toJSONString(jf));
        handleResult1.setHandleResult(slbh+"号办件交费" + Msgagger.CG);
        handleResult1.setProvideUnit("web+");
        handleResult1.setDataComeFromMode("接口");
        handleResultVoList1.add(handleResult1);
        serviceData1.setServiceCode("PaymentResultService");
        serviceData1.setServiceDataInfos(handleResultVoList1);
        serviceDatas.add(serviceData1);

        RespServiceData serviceData2 = new RespServiceData();
        List<SJ_Info_Handle_Result> handleResultVoList2 = new ArrayList<>();
        SJ_Info_Handle_Result handleResult2 = new SJ_Info_Handle_Result();
        List<JSEntity> jss = new ArrayList<JSEntity>();
        JSEntity js1 = new JSEntity("￥1200000.00","0.03","0.01","￥24000.00","契税","缴税成功","姜志伟",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        JSEntity js2 = new JSEntity("￥1200000.00","0.01","0.00","￥12000.00","个人所得税","缴税成功","张三，李思",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        jss.add(js1);
        jss.add(js2);
        handleResult2.setHandleText(JSONArray.toJSONString(jss));
        handleResult2.setHandleResult(slbh+"号办件税款缴纳" + Msgagger.CG);
        handleResult2.setProvideUnit("web+");
        handleResult2.setDataComeFromMode("接口");
        handleResultVoList2.add(handleResult2);
        serviceData2.setServiceCode("TaxPaymentResultService");
        serviceData2.setServiceDataInfos(handleResultVoList2);
        serviceDatas.add(serviceData2);

        mapParmeter.put("serviceDatas", JSONArray.toJSONString(serviceDatas));
        String result = dealOut2(mapParmeter,token);
        log.info("执行缴费外部提交成功");
        return result;
//        return JSONObject.toJSONString(mapParmeter);
    }

    @Override
    public String bookSuccess(BookReturnEntity bookRequest) {
        String slbh = bookRequest.getSlbh();
        System.out.println("正在获取操作用户token");
        String token = backFeign.getToken(new JwtAuthenticationRequest("testdjj","123456")).getData();

        List<SJ_Info_Handle_Result> handleResultVoList = new ArrayList<>();
        List<RespServiceData> serviceDatas = new ArrayList<>();
        RespServiceData serviceData = new RespServiceData();
        SJ_Info_Handle_Result handleResult = new SJ_Info_Handle_Result();

        handleResult.setHandleText("受理号为："+slbh+"的登记申请已登簿");
        handleResult.setHandleResult(slbh+"号办件登簿" + Msgagger.CG);
        handleResult.setDataComeFromMode("接口");
        handleResult.setProvideUnit("不动产登记中心");
        handleResult.setRemarks("受理号为："+slbh+"的登记申请已登簿，登簿详情不做具体展示，登记的证书或证明请前往不动产登记中心领取。");

        handleResultVoList.add(handleResult);
        serviceData.setServiceCode("ImmovableBookingResultService");
        serviceData.setServiceDataInfos(handleResultVoList);
        serviceDatas.add(serviceData);

        doGetBookServiceData(bookRequest,serviceDatas);

        Map<String, String> mapParmeter = new HashMap<>();
        mapParmeter.put("registerNumber",slbh);
        mapParmeter.put("serviceDatas", JSONArray.toJSONString(serviceDatas));

        String result = dealOut2(mapParmeter,token);
        return result;
    }

    @Override
    public List<Sj_Info_Jyhtxx> getHtInfo(String htbah) {
        log.info("进入合同信息获取详情");
        List<Sj_Info_Jyhtxx> businessContracts = new ArrayList<Sj_Info_Jyhtxx>();
        Sj_Info_Jyhtxx businessContract = doGetHtxx(htbah,null,"");
        businessContracts.add(businessContract);
        System.out.println("获取得到的合同信息详情为："+JSONArray.toJSONString(businessContracts));
        return businessContracts;
    }

    @Override
    public List<Sj_Info_Jyhtxx> getSecondHtInfo(String htbah) {
        log.info("进入二手房合同信息获取详情");
        List<Sj_Info_Jyhtxx> businessContracts = new ArrayList<Sj_Info_Jyhtxx>();
        Sj_Info_Jyhtxx businessContract = doGetHtxx(htbah,"second","xxx房产中介有限公司");
        businessContracts.add(businessContract);
        System.out.println("获取得到的合同信息详情为："+JSONArray.toJSONString(businessContracts));
        return businessContracts;
    }

    @Override
    public List<SJ_Info_Bdcqlxgxx> getBDCQLInfo(ParametricData parametricData) throws ParseException {
        log.info("进入不动产权属信息获取详情");
        return initBdcql("201910260002","ql",parametricData.getBdczh(),null);
    }

    @Override
    public Object examineSuccess1(String slbh) {
        System.out.println("正在获取操作用户token");
        String token = backFeign.getToken(new JwtAuthenticationRequest("tsdjj","123456")).getData();

        Map<String, String> mapParmeter = new HashMap<>();
        mapParmeter.put("registerNumber",slbh);

        List<RespServiceData> serviceDatas = new ArrayList<>();
        RespServiceData serviceData = new RespServiceData();

        List<SJ_Info_Handle_Result> handleResultVoList = getHandleResults(slbh,"准予登记",slbh+"号办件审核" + Msgagger.ADOPT,"受理号为："+slbh+"的登记申请已审核，审核结果为：材料真实，充足，准予登记");
        serviceData.setServiceCode("ImmovableHandleResultService");
        serviceData.setServiceDataInfos(handleResultVoList);
        serviceDatas.add(serviceData);
        mapParmeter.put("serviceDatas", JSONArray.toJSONString(serviceDatas));

        String result = dealOut2(mapParmeter,token);
        return result;
    }

    private Sj_Info_Qsxx initQsxx(String slbh, String qlrlx, String qlrmc, String nsrsbh){
        Sj_Info_Qsxx qsxx = new Sj_Info_Qsxx();
        qsxx.setBdcdyh("320312106031GB00540F00010021");
        qsxx.setDataComeFromMode("接口");
        qsxx.setPreservationMan("tsswj");
        qsxx.setProvideUnit("xx市区契税部门");
        qsxx.setServiceCode("PayingTaxService");
        if(qlrlx.equals("义务人")) {
            qsxx.setXtsphm("321xxx404000309999");
            qsxx.setZrfcsfbz("0");
            qsxx.setDzsphm("321xxx404000309999");
            qsxx.setPzhm("99999991");
            qsxx.setZsxmDm("10106");
            qsxx.setZsxmMc("个人所得税");
            qsxx.setZspmDm("101060902");
            qsxx.setZspmMc("个人房屋转让所得");
            qsxx.setZszmDm("1010609022320001");
            qsxx.setZszmMc("房屋转让附征1% ");

            qsxx.setSl("0.01");
            qsxx.setSjje(12000l);
            qsxx.setBz("不动产单元号:320312106031GB00540F00010021 房屋坐落地址:泉山区 华美和园（四期） A17号楼 1--112 权属转移面积:134.00平米 合同日期:"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }else{
            qsxx.setXtsphm("321xxx404000308888");
            qsxx.setZrfcsfbz("1");
            qsxx.setDzsphm("321xxx404000308888");
            qsxx.setPzhm("99999992");
            qsxx.setZsxmDm("10119");
            qsxx.setZsxmMc("契税");
            qsxx.setZspmDm("101191211");
            qsxx.setZspmMc("存量房（商品住房买卖）");

            qsxx.setSl("0.03");
            qsxx.setSjje(24000l);
            qsxx.setBz("共有人：李冬梅，滕景斯 不动产单元号:320312106031GB00540F00010021 房屋坐落地址:泉山区 华美和园（四期） A17号楼 1--112 权属转移面积:134.00平米 合同日期:"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        qsxx.setHtbh("TZ0059240");
        qsxx.setNsrsbh(nsrsbh);
        qsxx.setNsrmc(qlrmc);
        qsxx.setPzzlDm("000001031");
        qsxx.setPzzgDm("2321031171");
        qsxx.setSkssqq(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        qsxx.setSkssqz(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        qsxx.setJsyj("1200000");
        qsxx.setZgswskfjDm("1320xxx1600");
        qsxx.setZsswjgDm("1320xxx0000");
        qsxx.setSkssswjgDm("国家税务总局xx市xx区税务局税源管理一科");
        qsxx.setZgswskfjMc("国家税务总局xx市xx区税务局第一税务所");
        qsxx.setZsswjgMc("国家税务总局xx市xx区税务局");
        qsxx.setKjrq(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return qsxx;
    }

    @Override
    public Object examineSuccess3(String slbh,String water) {
        dealGSHandle(slbh,"水","W-"+new SimpleDateFormat("yyyyMMdd").format(new Date())+"0001",water);//受理号，部门标识，新的业务户号
        dealGSHandle(slbh,"电","E-"+new SimpleDateFormat("yyyyMMdd").format(new Date())+"0002",null);//受理号，部门标识，新的业务户号
        dealGSHandle(slbh,"气","G-"+new SimpleDateFormat("yyyyMMdd").format(new Date())+"0003",null);//受理号，部门标识，新的业务户号
        dealGSHandle(slbh,"广","R-"+new SimpleDateFormat("yyyyMMdd").format(new Date())+"0004",null);//受理号，部门标识，新的业务户号
        return "水、电、气、广过户业务处理成功";
    }

    private String dealGSHandle(String slbh,String bmbs,String xywh,String water){
        String token = "";

        Map<String, String> mapParmeter = new HashMap<>();
        mapParmeter.put("registerNumber",slbh);

        List<RespServiceData> serviceDatas = new ArrayList<>();
        RespServiceData serviceData = new RespServiceData();
        List<SJ_Info_Handle_Result> handles = new ArrayList<SJ_Info_Handle_Result>();
        SJ_Info_Handle_Result handle = new SJ_Info_Handle_Result();
        handle.setAcceptanceNumber(xywh);
        handle.setHandleResult("过户完成");
        handle.setHandleText(bmbs+"过户完成，新业务户号："+xywh);
        handle.setRemarks("已同意"+bmbs+"业务过户，生成使用新的业务号为："+xywh);
        handle.setDataComeFromMode("接口");

        switch (bmbs){
            case "水":
                serviceData.setServiceCode("WaterHandleResultService");
                handle.setServiceCode("WaterHandleResultService");
                if(StringUtils.isNotBlank(water) && water.equals("1")) {
                    token = backFeign.getToken(new JwtAuthenticationRequest("testzls1", "123456")).getData();
                    handle.setProvideUnit("xx市自来水公司1");
                    handle.setPreservationMan("testzls1");
                }else{
                    token = backFeign.getToken(new JwtAuthenticationRequest("testzls2", "123456")).getData();
                    handle.setProvideUnit("xx市自来水公司2");
                    handle.setPreservationMan("testzls2");
                }
                break;
            case "电":
                serviceData.setServiceCode("ElectricHandleResultService");
                handle.setServiceCode("ElectricHandleResultService");
                token = backFeign.getToken(new JwtAuthenticationRequest("testdlgs", "123456")).getData();
                handle.setProvideUnit("xx市供电公司");
                handle.setPreservationMan("testdlgs");
                break;
            case "气":
                serviceData.setServiceCode("GasHandleResultService");
                handle.setServiceCode("GasHandleResultService");
                token = backFeign.getToken(new JwtAuthenticationRequest("testtrq", "123456")).getData();
                handle.setProvideUnit("xx市区燃气公司");
                handle.setPreservationMan("testtrq");
                break;
            case "广":
                serviceData.setServiceCode("CableTvHandleResultService");
                handle.setServiceCode("CableTvHandleResultService");
                token = backFeign.getToken(new JwtAuthenticationRequest("testyxds", "123456")).getData();
                handle.setProvideUnit("xx市广播电视部门");
                handle.setPreservationMan("testyxds");
                break;
        }

        handles.add(handle);
        serviceData.setServiceDataInfos(handles);
        serviceDatas.add(serviceData);
        mapParmeter.put("serviceDatas", JSONArray.toJSONString(serviceDatas));

        String result = dealOut2(mapParmeter,token);
        System.out.println(bmbs+"提交成功");
        return bmbs+" "+result;
    }

    private Sj_Info_Jyhtxx doGetHtxx(String htbah, String bs, String dw){
        Sj_Info_Jyhtxx businessContract = new Sj_Info_Jyhtxx();
        businessContract.setContractId("85327621-240b-4b60-a7ea-27c5eb7a2510");
        businessContract.setContractNumber("TZ0059240");
        businessContract.setContractRecordNumber(htbah);
        List<SJ_Qlr_Gl> qlrgls1 = new ArrayList<SJ_Qlr_Gl>();
        List<SJ_Qlr_Gl> qlrgls2 = new ArrayList<SJ_Qlr_Gl>();
        if(StringUtils.isNotBlank(bs) && bs.equals("second")) {
            businessContract.setContractType("二手房交易合同");
            SJ_Qlr_Gl qlrgl1 = getQlrGl("张三","身份证","320xxx199107085638","售房者","共同共有",null,1);
            SJ_Qlr_Gl qlrgl2 = getQlrGl("李思","身份证","320xxx199208125668","售房者","共同共有",null,2);
            qlrgls2.add(qlrgl1);
            qlrgls2.add(qlrgl2);
            SJ_Qlr_Gl qlrgl3 = getQlrGl("赵六","身份证","320xxx198704277154","购房者","单独所有",null,1);
            qlrgls1.add(qlrgl3);
        }else{
            businessContract.setContractType("商品房交易合同");
            SJ_Qlr_Gl qlrgl1 = getQlrGl("张三","身份证","320xxx199107085638","购房者","共同共有",null,1);
            SJ_Qlr_Gl qlrgl2 = getQlrGl("李思","身份证","320xxx199208125668","购房者","共同共有",null,2);
            qlrgls1.add(qlrgl1);
            qlrgls1.add(qlrgl2);
            SJ_Qlr_Gl qlrgl3 = getQlrGl("xxx房地产开发有限公司","统一社会信用代码","9154yy56489","售房者",null,null,1);
            qlrgls2.add(qlrgl3);
        }
        try {
            businessContract.setContractRecordTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-07-28 15:24:54"));
            businessContract.setContractSignTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-07-27 09:50:07"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        businessContract.setContractAmount(new BigDecimal(2400339.00));
        businessContract.setDataComeFromMode("接口");
        businessContract.setProvideUnit(dw);
        List<SJ_Bdc_Gl> bdcgls = getBDCGLs("房地","jyhtxx");
        businessContract.setGlImmovableVoList(bdcgls);
        businessContract.setGlHouseBuyerVoList(qlrgls1);
        businessContract.setGlHouseSellerVoList(qlrgls2);

        return businessContract;
    }

    private List<SJ_Info_Handle_Result> getHandleResults(String slbh, String handleText, String result, String remark){
        List<SJ_Info_Handle_Result> handleResultVoList = new ArrayList<>();
        SJ_Info_Handle_Result handleResult = new SJ_Info_Handle_Result();

        handleResult.setHandleText(handleText);
        handleResult.setHandleResult(result);
        handleResult.setDataComeFromMode("接口");
        handleResult.setProvideUnit("不动产登记中心");
        handleResult.setRemarks(remark);

        handleResultVoList.add(handleResult);
        return handleResultVoList;
    }

    private String dealOut2(Map<String,String> mapParmeter,String token){
        if(StringUtils.isNotBlank(token)){
            System.out.println("操作用户token获取成功");
            System.out.println("执行内网业务号挂接程序");
            ObjectRestResponse<String> rv = backFeign.DealRecieveFromOuter2(token,mapParmeter);
            if(rv.getStatus()==200) {
                System.out.println("内网业务号挂接成功");
            }else{
                System.out.println("内网业务号挂接失败");
                return rv.getMessage();
            }
        }
        return "提交成功";
    }

    private void doGetBookServiceData(BookReturnEntity bookRequest,List<RespServiceData> serviceDatas){
        String slbh = bookRequest.getSlbh();
        String isLoadBook = bookRequest.getIsLoadBook();
        List<String> ywlxs = bookRequest.getDzzslxs();
        if(!(StringUtils.isNotBlank(isLoadBook) && StringUtils.isNotBlank(isLoadBook.replaceAll(" ","")) && isLoadBook.replaceAll(" ","").equals("1"))){
            if(ywlxs!=null){
                for(String ywlx:ywlxs){
                    switch (ywlx){
                        case "0":                   //电子证书
                            serviceDatas.add(getDzzs(slbh));
                            break;
                        case "1":                   //预告电子证明
                            serviceDatas.add(getYgdzzm(slbh));
                            break;
                        case "2":                   //抵押电子证明
                            serviceDatas.add(getDydzzm(slbh));
                            break;
                        case "3":                   //注销电子证书
                            serviceDatas.add(getDzzsZx(slbh));
                            break;
                        case "4":                   //注销预告电子证明
                            serviceDatas.add(getYgdzzmZx(slbh));
                            break;
                        case "5":                   //注销抵押电子证明
                            serviceDatas.add(getDydzzmZx(slbh));
                            break;
                    }
                }
            }
        }
    }

    private RespServiceData getDzzs(String slbh){
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode("ImmovableElectronicCertificate");
        List<SJ_Info_Bdcqlxgxx> serviceDataInfos = null;
        try {
            serviceDataInfos = initBdcql(slbh,"ql",null,"newOne");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        serviceData.setServiceDataInfos(serviceDataInfos);
        return serviceData;
    }
    private RespServiceData getYgdzzm(String slbh){
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode("ForecastElectronicCertificationService");
        List<SJ_Info_Bdcqlxgxx> serviceDataInfos = null;
        try {
            serviceDataInfos = initBdcql(slbh,"yg",null,null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        serviceData.setServiceDataInfos(serviceDataInfos);
        return serviceData;
    }
    private RespServiceData getDydzzm(String slbh){
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode("MortgageElectronicCertificate");
        List<Sj_Info_Bdcdyxgxx> serviceDataInfos = initBdcdy(slbh,"dy");
        serviceData.setServiceDataInfos(serviceDataInfos);
        return serviceData;
    }
    private RespServiceData getDzzsZx(String slbh){
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode("ImmovableElectronicCertCancellation");
        List<SJ_Info_Bdcqlxgxx> serviceDataInfos = null;
        try {
            serviceDataInfos = initBdcql(slbh,"ql_zx",null,null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        serviceData.setServiceDataInfos(serviceDataInfos);
        return serviceData;
    }
    private RespServiceData getYgdzzmZx(String slbh){
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode("ForecastElectronicCertCancellation");
        List<SJ_Info_Bdcqlxgxx> serviceDataInfos = null;
        try {
            serviceDataInfos = initBdcql(slbh,"yg_zx",null,null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        serviceData.setServiceDataInfos(serviceDataInfos);
        return serviceData;
    }
    private RespServiceData getDydzzmZx(String slbh){
        RespServiceData serviceData = new RespServiceData();
        serviceData.setServiceCode("MortgageElectronicCertCancellation");
        List<Sj_Info_Bdcdyxgxx> serviceDataInfos = initBdcdy(slbh,"dy_zx");
        serviceData.setServiceDataInfos(serviceDataInfos);
        return serviceData;
    }

    private List<SJ_Info_Bdcqlxgxx> initBdcql(String slbh, String zlType, String zh, String newOne) throws ParseException {
        List<SJ_Info_Bdcqlxgxx> serviceDataInfos = new ArrayList<SJ_Info_Bdcqlxgxx>();
        SJ_Info_Bdcqlxgxx bdcqlxgxx = new SJ_Info_Bdcqlxgxx();

        bdcqlxgxx.setAcceptanceNumber(slbh);
        bdcqlxgxx.setImmovableSite("xx市xx区长安路第108号开元四季新城(测试)13幢1单元101室");
        bdcqlxgxx.setRegistrationDate(new Date());
        bdcqlxgxx.setCertificateType("不动产权证");
        bdcqlxgxx.setArchitecturalArea(new BigDecimal(134));
        bdcqlxgxx.setHouseArchitecturalArea(new BigDecimal(112));
        bdcqlxgxx.setApportionmentArchitecturalArea(new BigDecimal(22));
        bdcqlxgxx.setHouseObtainingWays("交易（购买取得）");
        bdcqlxgxx.setHousePlanningPurpose("10");
        bdcqlxgxx.setHouseRightType("4");
        bdcqlxgxx.setHouseRightNature("0");
        bdcqlxgxx.setLandRightNature("101");
        bdcqlxgxx.setLandUseRightEndingDate(new SimpleDateFormat("yyyy-MM-dd").parse("2053-12-11"));
        if(zlType.equals("ql")) {
            if(StringUtils.isBlank(zh)) {
                bdcqlxgxx.setImmovableCertificateNo("苏(2019)xx市不动产权第9999999号");
                bdcqlxgxx.setServiceCode("ImmovableElectronicCertificate");
            }else{
                bdcqlxgxx.setImmovableCertificateNo("苏(2019)xx市不动产权第"+zh+"号");
                bdcqlxgxx.setServiceCode("OwnershipCertificateService");
            }
        } else if(zlType.equals("ql_zx")){
            bdcqlxgxx.setImmovableCertificateNo("苏(2019)xx市不动产权第9999999号");
            bdcqlxgxx.setServiceCode("ImmovableElectronicCertCancellation");
        } else if(zlType.equals("yg")){
            bdcqlxgxx.setImmovableCertificateNo("苏(2019)xx市不动产证明第9999999号");
            bdcqlxgxx.setServiceCode("ForecastElectronicCertificationService");
        } else {
            bdcqlxgxx.setImmovableCertificateNo("苏(2019)xx市不动产证明第9999999号");
            bdcqlxgxx.setServiceCode("ForecastElectronicCertCancellation");
        }
        bdcqlxgxx.setDataComeFromMode("接口");

        //不动产关联信息
        List<SJ_Bdc_Gl> bdcgls = getBDCGLs("房地","bdcqlxgxx");
        bdcqlxgxx.setGlImmovableVoList(bdcgls);

        //权利人关联
        List<SJ_Qlr_Gl> qlrgls = new ArrayList<SJ_Qlr_Gl>();
        if(StringUtils.isNotBlank(newOne) && newOne.equals("newOne")){
            SJ_Qlr_Gl qlrgl1 = getQlrGl("赵六", "身份证", "320xxx198704277154", "权利人", "单独所有", null, 1);
            qlrgls.add(qlrgl1);
        }else {
            SJ_Qlr_Gl qlrgl1 = getQlrGl("张三", "身份证", "320xxx199107085638", "权利人", "共同共有", null, 1);
            SJ_Qlr_Gl qlrgl2 = getQlrGl("李思", "身份证", "320xxx199208125668", "权利人", "共同共有", null, 2);
            qlrgls.add(qlrgl1);
            qlrgls.add(qlrgl2);
        }
        bdcqlxgxx.setGlObligeeVoList(qlrgls);

        serviceDataInfos.add(bdcqlxgxx);
        return serviceDataInfos;
    }
    private List<Sj_Info_Bdcdyxgxx> initBdcdy(String slbh, String zlType){
        List<Sj_Info_Bdcdyxgxx> serviceDataInfos = new ArrayList<Sj_Info_Bdcdyxgxx>();
        Sj_Info_Bdcdyxgxx bdcdyxgxx = new Sj_Info_Bdcdyxgxx();
        bdcdyxgxx.setAcceptanceNumber(slbh);
        bdcdyxgxx.setImmovableSite("（测试地址）长安路108号，开元四季新城(测试)13幢1单元101室");
        bdcdyxgxx.setMortgageCertificateNo("苏(2019)xx市不动产证明第8888888号");
        bdcdyxgxx.setRegistrationDate(new Date());
        bdcdyxgxx.setMortgageMode("预告抵押登记");
        bdcdyxgxx.setMortgageArea("134");
        bdcdyxgxx.setCreditAmount("700000.00");
        bdcdyxgxx.setMortgageAmount("0.00");
        bdcdyxgxx.setValuationValue("2400339.00");
        bdcdyxgxx.setRemarks("购买商品房贷款");
        bdcdyxgxx.setDataComeFromMode("接口");
        if(zlType.equals("dy")){
            bdcdyxgxx.setServiceCode("MortgageElectronicCertificate");
        }else{
            bdcdyxgxx.setServiceCode("MortgageElectronicCertCancellation");
        }

        //不动产关联信息
        List<SJ_Bdc_Gl> bdcgls = getBDCGLs("房地","bdcdyxgxx");
        bdcdyxgxx.setGlImmovableVoList(bdcgls);

        //权利人关联
        List<SJ_Qlr_Gl> qlrgls1 = new ArrayList<SJ_Qlr_Gl>();
        SJ_Qlr_Gl qlrgl1 = getQlrGl("张三","身份证","320xxx199107085638","抵押人",null,null,1);
        SJ_Qlr_Gl qlrgl2 = getQlrGl("李思","身份证","320xxx199208125668","抵押人",null,null,2);
        qlrgls1.add(qlrgl1);
        qlrgls1.add(qlrgl2);
        bdcdyxgxx.setGlMortgagorVoList(qlrgls1);

        List<SJ_Qlr_Gl> qlrgls2 = new ArrayList<SJ_Qlr_Gl>();
        SJ_Qlr_Gl qlrgl3 = getQlrGl("xx市xxxxx银行测试支行","统一社会信用代码","9123456XX59","抵押权人",null,null,1);
        qlrgls2.add(qlrgl3);
        bdcdyxgxx.setGlMortgageHolderVoList(qlrgls2);

        serviceDataInfos.add(bdcdyxgxx);
        return serviceDataInfos;
    }

    private List<SJ_Bdc_Gl> getBDCGLs(String immovableType, String table){
        //不动产关联信息
        List<SJ_Bdc_Gl> bdcgls = new ArrayList<SJ_Bdc_Gl>();
        SJ_Bdc_Gl bdcgl = new SJ_Bdc_Gl();
        bdcgl.setImmovableType(immovableType);
        bdcgl.setInfoTableIdentification(table);
        SJ_Bdc_Fw_Info fw = getFw();
        bdcgl.setFwInfo(fw);
        bdcgls.add(bdcgl);
        return bdcgls;
    }

    private SJ_Bdc_Fw_Info getFw(){
        SJ_Bdc_Fw_Info fw = new SJ_Bdc_Fw_Info();
        fw.setHouseholdId("B0295ED4-E35A-4A71-E044-013F74F8663F");
        fw.setSeatId("B0295ED4-E35A-4A71-E044-013F");
        fw.setBuildingNumber("0013");
        fw.setHouseNumber("0001-101");
        fw.setImmovableUnicode("B0295ED4-E35A-4A71-E044-013FADB97946");
        fw.setImmovablePlanningUse("市场化商品房");
        fw.setHouseLocation("（测试地址）长安路108号，开元四季新城(测试)13幢1单元101室");
        fw.setImmovableUnitNumber("320xxx007031GB00013F00131101");
        fw.setSeatNumber("0013");
        fw.setHouseholdMark("1101");
        fw.setRoomMark("101");
        fw.setUnitMark("1");
        fw.setTotalStorey("6");
        fw.setLocationStorey("1");
        fw.setProjectName("开元四季新城(测试)");
        fw.setArchitecturalArea(new BigDecimal(134));
        fw.setHouseArchitecturalArea(new BigDecimal(112));
        fw.setApportionmentArchitecturalArea(new BigDecimal(22));
        fw.setBuildingName("开元四季新城(测试)13幢");
        return fw;
    }

    private SJ_Qlr_Gl getQlrGl(String name, String zjlx, String zjhm, String qlrlx, String gyfs, Integer gyfe, int soap){
        SJ_Qlr_Gl qlrgl = new SJ_Qlr_Gl();
        SJ_Qlr_Info qlr = new SJ_Qlr_Info();
        qlr.setObligeeName(name);
        qlr.setObligeeDocumentType(zjlx);
        qlr.setObligeeDocumentNumber(zjhm);
        qlrgl.setRelatedPerson(qlr);
        qlrgl.setObligeeName(name);
        qlrgl.setObligeeType(qlrlx);
        qlrgl.setSharedMode(gyfs);
        qlrgl.setSharedValue(Integer.toString(gyfe));
        qlrgl.setObligeeOrder(soap);
        return qlrgl;
    }
}
