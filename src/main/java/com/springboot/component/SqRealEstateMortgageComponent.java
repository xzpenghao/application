package com.springboot.component;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.HttpCallComponent;
import com.springboot.config.Msgagger;
import com.springboot.constant.penghao.BizOrBizExceptionConstant;
import com.springboot.entity.ParamEntity;
import com.springboot.entity.chenbin.personnel.other.bank.bankenum.*;
import com.springboot.entity.chenbin.personnel.other.bank.ReMortgageRegistrationVo;
import com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.MortgageRegistrationReqVo;
import com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.domain.*;
import com.springboot.entity.chenbin.personnel.other.bank.business.revok.RevokeRegistrationReqVo;
import com.springboot.entity.chenbin.personnel.other.bank.business.revok.RevokeRegistrationRespVo;
import com.springboot.entity.newPlat.settingTerm.FtpSettingTerm;
import com.springboot.entity.newPlat.settingTerm.FtpSettings;
import com.springboot.feign.OuterBackFeign;
import com.springboot.popj.FwInfo;
import com.springboot.popj.GlImmovable;
import com.springboot.popj.RelatedPerson;

import com.springboot.popj.netSign.BusinessContract;
import com.springboot.popj.netSign.GlHouseBuyer;
import com.springboot.popj.netSign.GlHouseSeller;
import com.springboot.popj.pub_data.*;
import com.springboot.util.DateUtils;
import com.springboot.util.HttpClientUtils;
import com.springboot.util.NetSignUtils;
import com.springboot.util.StrUtil;
import com.springboot.util.chenbin.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jboss.jandex.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 针对于宿迁流程Component
 */
@Component
@Slf4j
public class SqRealEstateMortgageComponent {
    @Value("${sq.jyht.api_id}")
    private String jyapiId;
    @Value("${sq.jyht.from_user}")
    private String jyfromUser;
    @Value("${sq.qsxx.api_id}")
    private String qsapiId;
    @Value("${sq.qsxx.from_user}")
    private String qsfromUser;
    @Value("${sq.jyht.ip}")
    private String ip;
    @Value("${sq.jyht.post}")
    private String post;
    @Value("${sq.jyht.spf.api_id}")
    private String jySpfApiId;
    @Value("${sq.jyht.spf.from_user}")
    private String jySpfFromUser;
    @Value("${sq.jyht.spf.ip}")
    private String jySpfIp;
    @Value("${sq.jyht.spf.post}")
    private String jySpfPost;
    @Value("${penghao.highestAuthority.username}")
    private String highestAuthorname;
    @Value("${penghao.highestAuthority.password}")
    private String highestAuthorpassword;
    @Value("${sq.qsxx.swjgdm}")
    private String swjgdm;
    @Value("${sq.qsxx.swrydm}")
    private String swrydm;
    @Value("${sq.qsxx.serviceMethod}")
    private String serviceMethod;
    @Value("${sq.bdcdydj}")
    private String bdcdydj;
    @Value("${sq.ygdy}")
    private String ygdy;
    @Value("${sq.dyzxdj}")
    private String dyzxdj;
    @Value("${sq.bank.jt.username}")
    private String bankBsryname;
    @Value("${sq.bank.jt.password}")
    private String bankBsrypassword;
    @Value("${httpclient.windowAcceptanceIp}")
    private String windowAcceptanceIp; //一窗受理ip
    @Value("${httpclient.windowAcceptanceSeam}")
    private String windowAcceptanceSeam; //一窗受理接口
    @Autowired
    private HttpCallComponent httpCallComponent;
    @Autowired
    private AnonymousInnerComponent anonymousInnerComponent;
    @Autowired
    private NetSignUtils netSignUtils;
    @Autowired
    private OuterBackFeign outerBackFeign;
    @Autowired
    private FtpSettings ftpSettings;


    public void sqJgdyzx(RevokeRegistrationReqVo revokeRegistrationRespVo, OutputStream outputStream) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Map<String, String> mapParmeter = new HashMap<>();
        JSONObject reObject = new JSONObject();
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            public String call() throws Exception {
                try {
                    if (null != revokeRegistrationRespVo.getFileInfoVoList() && revokeRegistrationRespVo.getFileInfoVoList().size() != 0) {
                        String path = StrUtil.getFTPUrl(StrUtil.getFTPRemotePathByFTPPath(revokeRegistrationRespVo.getFileInfoVoList().get(0).getFileAdress()));
                        FtpSettingTerm ftpSettingTerm = ftpSettings.gainTermByKey("jhpt");
                        anonymousInnerComponent.getProcessingAnnex(
                                null, mapParmeter, revokeRegistrationRespVo.getFileInfoVoList(),
                                ftpSettingTerm.getFtpAddress(),
                                ftpSettingTerm.getFtpPort(),
                                ftpSettingTerm.getFtpUsername(),
                                ftpSettingTerm.getFtpPassword(),
                                path
                        );
                    }
                } catch (Exception e) {
                    log.info("e" + e);
                }
                return null;
            }
        });
        executor.execute(future);
        long t = System.currentTimeMillis();
        JSONObject jsonObject1 = JSONObject.fromObject(revokeRegistrationRespVo);
        log.info("jsonObject1" + jsonObject1);
        RevokeRegistrationRespVo registrationReqV = new RevokeRegistrationRespVo();
        try {
            if (null == revokeRegistrationRespVo) {
                Xyfzbad(null, registrationReqV, "000002", "接口编码有误");
                ClReturnJgdyzx(Msgagger.JSBAD,reObject,outputStream,null,registrationReqV);
            }
            List<Sj_Info_Bdcdyxgxx> immovableCurrentMortgageInfoVoList = new ArrayList<>();
            SJ_Sjsq sj_sjsq = new SJ_Sjsq();
            sj_sjsq.setNotifiedPersonName(revokeRegistrationRespVo.getContacts());
            sj_sjsq.setNotifiedPersonAddress(revokeRegistrationRespVo.getContactsAdress());
            sj_sjsq.setNotifiedPersonTelephone(revokeRegistrationRespVo.getContactsPhone());
            sj_sjsq.setReceiptTime(new Date());
            sj_sjsq.setBusinessType(Msgagger.ZXDJ);
            sj_sjsq.setDistrictCode(DistrictCodeEnum.Sc(revokeRegistrationRespVo.getBusinessAreas()));//办理区域
            if (null != revokeRegistrationRespVo.getRealEstateInfoVoList()) {
                List<RespServiceData> respServiceDataList = new ArrayList<>();
                for (com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain.RealEstateInfoVo realEstateInfoV : revokeRegistrationRespVo.getRealEstateInfoVoList()) {
                    Cldyxx(immovableCurrentMortgageInfoVoList, realEstateInfoV, revokeRegistrationRespVo);
                }
                RespServiceData respServiceData = new RespServiceData();
                respServiceData.setServiceDataInfos(immovableCurrentMortgageInfoVoList);//权属信息
                respServiceData.setServiceCode(Msgagger.BDCDYXXSJSERVICE_CODE);
                respServiceDataList.add(respServiceData);
                sj_sjsq.setServiceDatas(respServiceDataList);
                log.info("respServiceDataList" + respServiceDataList.size());
            }
            mapParmeter.put("modelId", dyzxdj);
            //附件上传
            JSONObject sjsqObject = JSONObject.fromObject(sj_sjsq);
            mapParmeter.put("SJ_Sjsq", sjsqObject.toString());//收件
            mapParmeter.put("returnSlbh", "1");
            com.alibaba.fastjson.JSONObject tokenObject = httpCallComponent.getTokenYcsl(bankBsryname, bankBsrypassword);//获得token
            String token = anonymousInnerComponent.getToken(tokenObject, "sqJgdyzx", null, null, null);
            ObjectRestResponse<String> objectRestResponse =  outerBackFeign.getBankToken(token,revokeRegistrationRespVo.getOrgId(),mapParmeter.get("modelId"));
            if (StringUtils.isEmpty(objectRestResponse.getData())){
                log.error("无法获取该银行操作或没有配置收件银行人员");
                return;
            }
            log.info("token" + token);
            mapParmeter.put("Authorization", token);
            if (null != revokeRegistrationRespVo.getFileInfoVoList() && revokeRegistrationRespVo.getFileInfoVoList().size() != 0) {
                List<SJ_File> fjfileList = new ArrayList<>();
                for (FileInfoVo fileInfoVo : revokeRegistrationRespVo.getFileInfoVoList()) {
                    SJ_File sj_file = new SJ_File();
                    String adress = "";
                    if (ftpSettings.getIsDealFtp().getJhpt()) {
                        adress = StrUtil.getFTPAdress(fileInfoVo.getFileAdress());
                    } else {
                        //替换地址为设定的路径(不同ftp时)
                    }
                    String fileAdress = adress.replaceAll("/", "\\\\");
                    String hz = fileInfoVo.getFileAdress().substring(fileInfoVo.getFileAdress().lastIndexOf(".") + 1);
                    sj_file.setFileName(fileAdress);
                    sj_file.setFileType(hz);
                    sj_file.setFileSequence(fileInfoVo.getFileSequence());
                    sj_file.setpName(FileTypeEnum.Sc(fileInfoVo.getFileType()));
                    log.info("银行发送的附件条目" + sj_file.getpName());
                    fjfileList.add(sj_file);
                }
                JSONArray jsonArray = JSONArray.fromObject(fjfileList);
                log.info("附件条数" + jsonArray.size());
                mapParmeter.put("fileVoList", jsonArray.toString());
            }
            //发送一窗受理进行启动流程
            String json = anonymousInnerComponent.preservationRegistryData(mapParmeter, objectRestResponse.getData(), "/api/biz/RecService/DealRecieveFromOuter5");
            log.info("ycsl\n" + json);
            log.info("modelId" + mapParmeter.get("modelId"));
            SJ_Sjsq sjsq = null;
            JSONObject jsonObject = JSONObject.fromObject(json);
            System.out.println("jsonObject" + jsonObject.toString());
            if (jsonObject.getString("status").equals(20500)) {
                Xyfzbad(revokeRegistrationRespVo, registrationReqV, "000004", jsonObject.getString("message"));
                ClReturnJgdyzx(null,reObject,outputStream,null,registrationReqV);
            } else {
                sjsq = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getString("data"), SJ_Sjsq.class);
            }
            if (StringUtils.isNotEmpty(sjsq.getReceiptNumber()) && StringUtils.isNotEmpty(sjsq.getRegisterNumber())) {
                log.info("流程启动成功并发送至登记平台");
                Xyfzbad(revokeRegistrationRespVo, registrationReqV, "000000", "");
                registrationReqV.setRevokeApplyId(revokeRegistrationRespVo.getRevokeApplyId());
                registrationReqV.setRevokeAcceptId(sjsq.getReceiptNumber());
                ClReturnJgdyzx(Msgagger.JSCG,reObject,outputStream,null,registrationReqV);
            } else if ((Integer) jsonObject.get("status") == 200 && StringUtils.isEmpty(sjsq.getRegisterNumber())) {
                log.error("发送登记局失败");
                Xyfzbad(revokeRegistrationRespVo, registrationReqV, "000004", "发送登记局失败");
                ClReturnJgdyzx(Msgagger.JSBAD,reObject,outputStream,null,registrationReqV);
            } else {
                log.error("流程未开启");
                Xyfzbad(revokeRegistrationRespVo, registrationReqV, "000004", "流程未开启");

            }
        } catch (Exception e) {
            e.getMessage();
        }
        executor.shutdown();
    }

    private void ClReturnJgdyzx(String status,JSONObject reObject,OutputStream outputStream,ReMortgageRegistrationVo reMortgageRegistrationVo,RevokeRegistrationRespVo registrationReqV) throws IOException{
        if (registrationReqV !=null){
            registrationReqV.setAcceptStatus(status);
            reObject = JSONObject.fromObject(registrationReqV);
            outputStream.write(reObject.toString().getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        }else {
            reMortgageRegistrationVo.setAcceptStatus(status);
            reObject = JSONObject.fromObject(reMortgageRegistrationVo);
            outputStream.write(reObject.toString().getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        }
    }


    /**
     * @param immovableCurrentMortgageInfoVoList
     * @param realEstateInfoVo
     * @param revokeRegistrationRespVo
     */
    private void Cldyxx(List<Sj_Info_Bdcdyxgxx> immovableCurrentMortgageInfoVoList, com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain.RealEstateInfoVo realEstateInfoVo, RevokeRegistrationReqVo revokeRegistrationRespVo) {
        Sj_Info_Bdcdyxgxx sj_info_bdcdyxgxx = new Sj_Info_Bdcdyxgxx();
        List<SJ_Bdc_Gl> glImmovableVoList = new ArrayList<>();
        sj_info_bdcdyxgxx.setMortgageCertificateNo(revokeRegistrationRespVo.getWarrantId());//抵押证明号
        sj_info_bdcdyxgxx.setMortgageReason(ReasonsCancellationEnum.Sc(revokeRegistrationRespVo.getRemoveReason()));
        if (null != realEstateInfoVo.getRealEstateUnitInfoVoList()) {
            for (com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain.RealEstateUnitInfoVo realEstateUnitInfoVo : realEstateInfoVo.getRealEstateUnitInfoVoList()) {
                SJ_Bdc_Gl bdcGl = new SJ_Bdc_Gl();
                bdcGl.setImmovableType(Msgagger.FANGDI);
                SJ_Bdc_Fw_Info fwInfo = new SJ_Bdc_Fw_Info();
                fwInfo.setImmovableUnitNumber(realEstateUnitInfoVo.getRealEstateUnitId());
                fwInfo.setHouseholdMark(realEstateUnitInfoVo.getHouseholdId());
                bdcGl.setFwInfo(fwInfo);
                glImmovableVoList.add(bdcGl);
            }
        }
        List<SJ_Qlr_Gl> glMortgagorVoList = new ArrayList<>();
        for (MortgagorInfoVo mortgagorInfoVo : revokeRegistrationRespVo.getMortgagorInfoVoList()) {
            ClDyr(mortgagorInfoVo, glMortgagorVoList);
        }
        List<SJ_Qlr_Gl> glMortgageeVoList = new ArrayList<>();
        for (MortgageeInfoVo mortgageeInfoVo : revokeRegistrationRespVo.getMortgageeInfoVoList()) {
            ClDyqr(mortgageeInfoVo, glMortgageeVoList);
        }
        sj_info_bdcdyxgxx.setGlMortgageHolderVoList(glMortgageeVoList);
        sj_info_bdcdyxgxx.setGlMortgagorVoList(glMortgagorVoList);
        List<SJ_Qlr_Gl> glAgentInfoVoList = new ArrayList<>();
        if (null != revokeRegistrationRespVo.getMortgagorAgentInfoVoList() && revokeRegistrationRespVo.getMortgagorAgentInfoVoList().size() != 0) {
            for (MortgagorAgentInfoVo mortgagorAgentInfoVo : revokeRegistrationRespVo.getMortgagorAgentInfoVoList()) {
                ClDyrWtdlr(mortgagorAgentInfoVo, glAgentInfoVoList);
            }
            sj_info_bdcdyxgxx.setGlAgentInfoVoList(glAgentInfoVoList);
        }
        sj_info_bdcdyxgxx.setGlImmovableVoList(glImmovableVoList);
        immovableCurrentMortgageInfoVoList.add(sj_info_bdcdyxgxx);
    }


    /**
     * 机构调用接口不动产抵押登记（含两证） 和新建商品房预告和预告抵押
     *
     * @param
     * @return
     */
    public void sqJgdyjk(MortgageRegistrationReqVo mortgageRegistrationReqVo, OutputStream outputStream) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Map<String, String> mapParmeter = new HashMap<>();
        JSONObject reObject = new JSONObject();
        Map<String, Object> map = new HashMap<>();
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            public String call() throws Exception {
                try {
                    if (null != mortgageRegistrationReqVo.getFileInfoVoList() && mortgageRegistrationReqVo.getFileInfoVoList().size() != 0) {
                        String path = StrUtil.getFTPUrl(StrUtil.getFTPRemotePathByFTPPath(mortgageRegistrationReqVo.getFileInfoVoList().get(0).getFileAdress()));
                        FtpSettingTerm ftpSettingTerm = ftpSettings.gainTermByKey("jhpt");
                        anonymousInnerComponent.getProcessingAnnex(
                                null, mapParmeter, mortgageRegistrationReqVo.getFileInfoVoList(),
                                ftpSettingTerm.getFtpAddress(),
                                ftpSettingTerm.getFtpPort(),
                                ftpSettingTerm.getFtpUsername(),
                                ftpSettingTerm.getFtpPassword(),
                                path
                        );
                    }
                } catch (Exception e) {
                    log.info("e" + e);
                }
                return null;
            }
        });
        executor.execute(future);
        long t = System.currentTimeMillis();
        JSONObject jsonObject1 = JSONObject.fromObject(mortgageRegistrationReqVo);
        log.info("jsonObject1" + jsonObject1);
        ReMortgageRegistrationVo reMortgageRegistrationVo = new ReMortgageRegistrationVo();
        //转成对象
        try {
            // MortgageRegistrationReqVo mortgageRegistrationReqVo= com.alibaba.fastjson.JSONObject.parseObject(data,MortgageRegistrationReqVo.class);
            if (null == mortgageRegistrationReqVo) {
                XySj(reMortgageRegistrationVo, null, "000003", "接口参数合法性验证失败");
                reMortgageRegistrationVo.setAcceptStatus(Msgagger.JSBAD);
                ClReturnJgdyzx(Msgagger.JSBAD,reObject,outputStream,reMortgageRegistrationVo,null);
            }
            log.info("1");
            SJ_Sjsq sj_sjsq = new SJ_Sjsq();
            Clsjsq(mortgageRegistrationReqVo, sj_sjsq);
            List<RespServiceData> respServiceDataList = new ArrayList<>();
            if (null != mortgageRegistrationReqVo.getApiName() && !mortgageRegistrationReqVo.getApiName().equals("mortgageRegister") || StringUtils.isEmpty(mortgageRegistrationReqVo.getApiName())) {
                XySj(reMortgageRegistrationVo, mortgageRegistrationReqVo, "000002", "接口编码有误");
                ClReturnJgdyzx(null,reObject,outputStream,reMortgageRegistrationVo,null);
            }
            if (StringUtils.isEmpty(mortgageRegistrationReqVo.getMortgageType())) {
                XySj(reMortgageRegistrationVo, mortgageRegistrationReqVo, "000003", "抵押类型不可为空");
                ClReturnJgdyzx(null,reObject,outputStream,reMortgageRegistrationVo,null);
            }
            //处理抵押登记含两证
            if (MortgageTypeEnum.Sc(mortgageRegistrationReqVo.getMortgageType()).equals(Msgagger.DYDJGR)) {
                mapParmeter.put("modelId", bdcdydj);
                ClDydjxx(mortgageRegistrationReqVo, sj_sjsq, respServiceDataList);
            } else if (MortgageTypeEnum.Sc(mortgageRegistrationReqVo.getMortgageType()).equals(Msgagger.XJSPFYGDYDJ)) {
                mapParmeter.put("modelId", ygdy);
                ClYgydxx(mortgageRegistrationReqVo, sj_sjsq, respServiceDataList);
            }
            if (null == respServiceDataList || respServiceDataList.size() == 0) {
                XySj(reMortgageRegistrationVo, mortgageRegistrationReqVo, "000004", "请检验信息是否传入正确");
                ClReturnJgdyzx(Msgagger.JSBAD,reObject,outputStream,reMortgageRegistrationVo,null);
            }
            log.info("respServiceDataList" + respServiceDataList.size());
            mapParmeter.put("returnSlbh", "1");
            com.alibaba.fastjson.JSONObject tokenObject = httpCallComponent.getTokenYcsl(highestAuthorname, highestAuthorpassword);
            //获得token
            String token = anonymousInnerComponent.getToken(tokenObject, "sqJgdyjk", null, null, null);
            //通过管理员拉取银行机构部门所对应的岗位人员的token
            ObjectRestResponse<String> objectRestResponse =  outerBackFeign.getBankToken(token,mortgageRegistrationReqVo.getOrgId(),mapParmeter.get("modelId"));
            if (StringUtils.isEmpty(objectRestResponse.getData())){
                log.error("无法获取该银行操作或没有配置收件银行人员");
                return;
            }
            log.info("token" + objectRestResponse.getData());
            mapParmeter.put("Authorization", token);
            JSONObject sjsqObject = JSONObject.fromObject(sj_sjsq);
            log.info("sjsqObject" + sjsqObject.toString());
            mapParmeter.put("SJ_Sjsq", sjsqObject.toString());//收件
            if (null != mortgageRegistrationReqVo.getFileInfoVoList() && mortgageRegistrationReqVo.getFileInfoVoList().size() != 0) {
                List<SJ_File> fjfileList = new ArrayList<>();
                for (FileInfoVo fileInfoVo : mortgageRegistrationReqVo.getFileInfoVoList()) {
                    SJ_File sj_file = new SJ_File();
                    //先把/转成\
                    String fileAdress = fileInfoVo.getFileAdress().replaceAll("/", "\\\\");
                    String hz = fileInfoVo.getFileAdress().substring(fileInfoVo.getFileAdress().lastIndexOf(".") + 1);
                    sj_file.setFileAddress(fileAdress);
                    sj_file.setFileName(fileInfoVo.getFileName());
                    sj_file.setFileType(hz);
                    sj_file.setFileSequence(fileInfoVo.getFileSequence());
                    sj_file.setpName(FileTypeEnum.Sc(fileInfoVo.getFileType()));
                    log.info("银行发送的附件条目" + sj_file.getpName());
                    fjfileList.add(sj_file);
                }
                JSONArray jsonArray = JSONArray.fromObject(fjfileList);
                log.info("附件条数" + jsonArray.size());
                mapParmeter.put("fileVoList", jsonArray.toString());
            }
            //发送一窗受理进行启动流程
            String json = anonymousInnerComponent.preservationRegistryData(mapParmeter, objectRestResponse.getData(), "/api/biz/RecService/DealRecieveFromOuter5");
            JSONObject jsonObject = JSONObject.fromObject(json);
            log.info("流程返回" + json);
            log.info("返回结果" + jsonObject.get("status").toString());
            if ((Integer) jsonObject.get("status") == 20500) {
                log.error(jsonObject.getString("message"));
                XySj(reMortgageRegistrationVo, mortgageRegistrationReqVo, "000004", jsonObject.getString("message"));
                ClReturnJgdyzx(Msgagger.JSBAD,reObject,outputStream,reMortgageRegistrationVo,null);
            }
            SJ_Sjsq sjsq = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getString("data"), SJ_Sjsq.class);
            if ((Integer) jsonObject.get("status") == 200 && StringUtils.isNotEmpty(sjsq.getReceiptNumber()) && StringUtils.isNotEmpty(sjsq.getRegisterNumber())) {
                log.info("流程启动成功并发送至登记平台");
                XySj(reMortgageRegistrationVo, mortgageRegistrationReqVo, "000000", "");
                reMortgageRegistrationVo.setMortgageApplyId(mortgageRegistrationReqVo.getMortgageApplyId());
                reMortgageRegistrationVo.setMortgageAcceptId(sjsq.getReceiptNumber());
                reMortgageRegistrationVo.setBusinessId(sjsq.getRegisterNumber());
                ClReturnJgdyzx(Msgagger.JSCG,reObject,outputStream,reMortgageRegistrationVo,null);
            } else if ((Integer) jsonObject.get("status") == 200 && StringUtils.isEmpty(sjsq.getRegisterNumber())) {
                log.error("发送登记局失败");
                XySj(reMortgageRegistrationVo, mortgageRegistrationReqVo, "000004", "发送登记局失败");
                ClReturnJgdyzx(Msgagger.JSBAD,reObject,outputStream,reMortgageRegistrationVo,null);
            } else {
                log.error("流程未开启");
                XySj(reMortgageRegistrationVo, mortgageRegistrationReqVo, "000004", "流程未开启");
                ClReturnJgdyzx(Msgagger.JSBAD,reObject,outputStream,reMortgageRegistrationVo,null);
            }
        } catch (IOException e) {
            e.getMessage();
        }
        executor.shutdown();
    }

    private void ClYgydxx(MortgageRegistrationReqVo mortgageRegistrationReqVo, SJ_Sjsq sj_sjsq, List<RespServiceData> respServiceDataList) {
        ClDydjxx(mortgageRegistrationReqVo, sj_sjsq, respServiceDataList);//抵押合同
        ClJyht(mortgageRegistrationReqVo, sj_sjsq, respServiceDataList);//交易合同信息
    }

    /**
     * 封装失败参数
     *
     * @param reMortgageRegistrationVo
     * @param mortgageRegistrationReqVo
     * @param message                   -
     */
    private void XySj(ReMortgageRegistrationVo reMortgageRegistrationVo, MortgageRegistrationReqVo mortgageRegistrationReqVo, String code, String message) {
        if (null == mortgageRegistrationReqVo) {
            reMortgageRegistrationVo.setApiName(mortgageRegistrationReqVo.getApiName());
            reMortgageRegistrationVo.setOrgId(mortgageRegistrationReqVo.getOrgId());
        }
        reMortgageRegistrationVo.setRespDate(DateUtils.getNowSystemDatetimeString());
        reMortgageRegistrationVo.setCharst("UTF-8");
        reMortgageRegistrationVo.setVersion("1.0.0");
        reMortgageRegistrationVo.setRespCode(code);
        reMortgageRegistrationVo.setRespMsg(message);
    }


    /**
     * 抵押注销登记封装失败参数
     *
     * @param registrationReqVo
     * @param revokeRegistrationRespVo
     * @param message
     */
    private void Xyfzbad(RevokeRegistrationReqVo registrationReqVo, RevokeRegistrationRespVo revokeRegistrationRespVo, String code, String message) {
        revokeRegistrationRespVo.setCharst("UTF-8");
        if (null == registrationReqVo) {
            revokeRegistrationRespVo.setApiName(registrationReqVo.getApiName());
            revokeRegistrationRespVo.setOrgId(registrationReqVo.getOrgId());
        }
        revokeRegistrationRespVo.setVersion("1.0.0");
        revokeRegistrationRespVo.setRespCode(code);
        revokeRegistrationRespVo.setRespMsg(message);
        revokeRegistrationRespVo.setRespDate(DateUtils.getNowSystemDatetimeString());
    }


    private void Clsjsq(MortgageRegistrationReqVo mortgageRegistrationReqVo, SJ_Sjsq sj_sjsq) {
        sj_sjsq.setNotifiedPersonName(mortgageRegistrationReqVo.getContacts());
        sj_sjsq.setNotifiedPersonTelephone(mortgageRegistrationReqVo.getContactsPhone());
        sj_sjsq.setNotifiedPersonAddress(mortgageRegistrationReqVo.getContactsAdress());
        log.info("收件申请地址:" + sj_sjsq.getNotifiedPersonAddress());
        sj_sjsq.setReceiptMan(mortgageRegistrationReqVo.getOperatorName());
        sj_sjsq.setReceiptTime(DateUtils.parseDate(new Date(), mortgageRegistrationReqVo.getMortgageApplyDate()));
        sj_sjsq.setRegistrationCategory(MortgageTypeEnum.Sc(mortgageRegistrationReqVo.getMortgageType()));//登记大类
        sj_sjsq.setDistrictCode(DistrictCodeEnum.Sc(mortgageRegistrationReqVo.getBusinessAreas()));//办理区域

        //sj_sjsq.setImmovableType(mortgageRegistrationReqVo.getMortgageType());//一般类型
    }


    /**
     * 交易合同信息
     *
     * @param mortgageRegistrationReqVo
     * @param
     */
    private void ClJyht(MortgageRegistrationReqVo mortgageRegistrationReqVo, SJ_Sjsq sj_sjsq, List<RespServiceData> respServiceDataList) {
        List<Sj_Info_Jyhtxx> infoJyhtxxList = new ArrayList<>();
        Sj_Info_Jyhtxx transactionContractInfo = new Sj_Info_Jyhtxx();
        List<SJ_Bdc_Gl> glImmovableVoList = new ArrayList<>();
        if (null != mortgageRegistrationReqVo.getRealEstateInfoVoList() && mortgageRegistrationReqVo.getRealEstateInfoVoList().size() != 0) {
            for (RealEstateInfoVo realEstateInfoVo : mortgageRegistrationReqVo.getRealEstateInfoVoList()) {
                if (null != realEstateInfoVo.getRealEstateUnitInfoVoList() && realEstateInfoVo.getRealEstateUnitInfoVoList().size() != 0) {
                    for (RealEstateUnitInfoVo realEstateUnitInfoVo :
                            realEstateInfoVo.getRealEstateUnitInfoVoList()) {
                        ClFwInfo(glImmovableVoList, realEstateUnitInfoVo);
                    }
                    transactionContractInfo.setGlImmovableVoList(glImmovableVoList);
                }
                //买房信息
                if (null != realEstateInfoVo.getObligeeInfoVoList() && realEstateInfoVo.getObligeeInfoVoList().size() != 0) {
                    List<SJ_Qlr_Gl> glObligeeVoList = new ArrayList<>();
                    for (ObligeeInfoVo obligeeInfoVo : realEstateInfoVo.getObligeeInfoVoList()) {
                        ClQlr(obligeeInfoVo, glObligeeVoList, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_GFZ);
                    }
                    transactionContractInfo.setGlHouseBuyerVoList(glObligeeVoList);
                }
                //卖房信息
                if (null != realEstateInfoVo.getSalerInfoVoList() && realEstateInfoVo.getSalerInfoVoList().size() != 0) {
                    List<SJ_Qlr_Gl> glObligeeVoList = new ArrayList<>();
                    for (SalerInfoVo salerInfoVo : realEstateInfoVo.getSalerInfoVoList()) {
                        ClMfr(salerInfoVo, glObligeeVoList);
                    }
                    transactionContractInfo.setGlHouseSellerVoList(glObligeeVoList);
                }
                infoJyhtxxList.add(transactionContractInfo);
            }
        }
        RespServiceData respServiceData = new RespServiceData();
        respServiceData.setServiceDataInfos(infoJyhtxxList);//权属信息
        respServiceData.setServiceCode(Msgagger.JYTHSERVICE_CODE);
        respServiceDataList.add(respServiceData);
        sj_sjsq.setServiceDatas(respServiceDataList);
        sj_sjsq.setTransactionContractInfo(transactionContractInfo);
    }


    //处理抵押登记
    private void ClDydjxx(MortgageRegistrationReqVo mortgageRegistrationReqVo, SJ_Sjsq sj_sjsq, List<RespServiceData> respServiceDataList) {
        try {
            Sj_Info_Dyhtxx sjInfoDyhtxx = new Sj_Info_Dyhtxx();
            if (StringUtils.isNotEmpty(mortgageRegistrationReqVo.getMortgageArea())) {
                sjInfoDyhtxx.setMortgageArea(new BigDecimal(mortgageRegistrationReqVo.getMortgageArea()));
            }
            if (StringUtils.isNotEmpty(mortgageRegistrationReqVo.getCreditAmount())) {
                sjInfoDyhtxx.setCreditAmount(new BigDecimal(mortgageRegistrationReqVo.getCreditAmount()));
                sjInfoDyhtxx.setMortgageAmount(new BigDecimal(mortgageRegistrationReqVo.getCreditAmount()));
            }
            if (StringUtils.isNotEmpty(mortgageRegistrationReqVo.getEvaluationValue())) {
                sjInfoDyhtxx.setValuationValue(new BigDecimal(mortgageRegistrationReqVo.getEvaluationValue()));
            }
            sjInfoDyhtxx.setMortgagePeriod(mortgageRegistrationReqVo.getMortgageTerm());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sjInfoDyhtxx.setMortgageStartingDate(formatter.parse(mortgageRegistrationReqVo.getMortgageStartDate()));
            sjInfoDyhtxx.setMortgageEndingDate(formatter.parse(mortgageRegistrationReqVo.getMortgageEndDate()));
            sjInfoDyhtxx.setMortgageReason(MortgageReasonEnum.Sc(mortgageRegistrationReqVo.getMortgageReason()));
            sjInfoDyhtxx.setApplyTime(DateUtils.parseDate(new Date(), mortgageRegistrationReqVo.getMortgageApplyDate()));
            if (StringUtils.isNotEmpty(mortgageRegistrationReqVo.getAbsoluteFact())) {
                sjInfoDyhtxx.setMaximumClaimConfirm(mortgageRegistrationReqVo.getAbsoluteFact());
            }
            sjInfoDyhtxx.setMortgageMode(MortgageModeEnum.Sc(mortgageRegistrationReqVo.getMortgageWay()));
            if (StringUtils.isNotEmpty(mortgageRegistrationReqVo.getHighestClaimAmount())) {
                sjInfoDyhtxx.setMaximumClaimAmount(new BigDecimal(mortgageRegistrationReqVo.getHighestClaimAmount()));
            }
            //抵押权人
            if (null != mortgageRegistrationReqVo.getMortgageeInfoVoList() && mortgageRegistrationReqVo.getMortgageeInfoVoList().size() != 0) {
                List<SJ_Qlr_Gl> glMortgageHolderVoList = new ArrayList<>();
                for (MortgageeInfoVo mortgageeInfoVo :
                        mortgageRegistrationReqVo.getMortgageeInfoVoList()) {
                    ClDyqr(mortgageeInfoVo, glMortgageHolderVoList);
                }
                sjInfoDyhtxx.setGlMortgageHolderVoList(glMortgageHolderVoList);
            }
            //抵押人
            if (null != mortgageRegistrationReqVo.getMortgagorInfoVoList() && mortgageRegistrationReqVo.getMortgagorInfoVoList().size() != 0) {
                List<SJ_Qlr_Gl> glMortgagorVoList = new ArrayList<>();
                for (MortgagorInfoVo mortgagorInfoVo :
                        mortgageRegistrationReqVo.getMortgagorInfoVoList()) {
                    ClDyr(mortgagorInfoVo, glMortgagorVoList);
                }
                sjInfoDyhtxx.setGlMortgagorVoList(glMortgagorVoList);
            }
            //债务人
            if (null != mortgageRegistrationReqVo.getObligorInfoVoList() && mortgageRegistrationReqVo.getObligorInfoVoList().size() != 0) {
                List<SJ_Qlr_Gl> glObligorInfoVoVoList = new ArrayList<>();
                for (ObligorInfoVo obligorInfoVo :
                        mortgageRegistrationReqVo.getObligorInfoVoList()) {
                    ClZwr(obligorInfoVo, glObligorInfoVoVoList);
                }
                sjInfoDyhtxx.setGlObligorInfoVoList(glObligorInfoVoVoList);
            }
            //抵押人委托代理人
            if (null != mortgageRegistrationReqVo.getMortgagorAgentInfoVoList() && mortgageRegistrationReqVo.getMortgagorAgentInfoVoList().size() != 0) {
                List<SJ_Qlr_Gl> AgentInfoVoList = new ArrayList<>();
                for (MortgagorAgentInfoVo agentInfoVo :
                        mortgageRegistrationReqVo.getMortgagorAgentInfoVoList()) {
                    ClDyrWtdlr(agentInfoVo, AgentInfoVoList);
                }
                sjInfoDyhtxx.setGlMortgagorAgentInfoVoList(AgentInfoVoList);
            }

            //抵押权人委托代理人
            if (null != mortgageRegistrationReqVo.getMortgageeAgentInfoVoList() && mortgageRegistrationReqVo.getMortgageeAgentInfoVoList().size() != 0) {
                List<SJ_Qlr_Gl> AgentInfoVoList = new ArrayList<>();
                for (MortgageeAgentInfoVo mortgageeAgentInfoVo :
                        mortgageRegistrationReqVo.getMortgageeAgentInfoVoList()) {
                    ClDyqrWtdlr(mortgageeAgentInfoVo, AgentInfoVoList);
                }
                sjInfoDyhtxx.setGlMortgageeAgentInfoVoList(AgentInfoVoList);
            }

            //不动产抵押数据
            if (null != mortgageRegistrationReqVo.getRealEstateInfoVoList() && mortgageRegistrationReqVo.getRealEstateInfoVoList().size() != 0) {
                List<SJ_Info_Bdcqlxgxx> immovableRightInfoVoList = new ArrayList<>();
                if (!MortgageTypeEnum.Sc(mortgageRegistrationReqVo.getMortgageType()).equals(Msgagger.XJSPFYGDYDJ)) {
                    for (RealEstateInfoVo realEstateInfoVo : mortgageRegistrationReqVo.getRealEstateInfoVoList()) {
                        ClQsxx(realEstateInfoVo, immovableRightInfoVoList);
                    }
                    //serviceDatas
                    RespServiceData respServiceData = new RespServiceData();
                    respServiceData.setServiceDataInfos(immovableRightInfoVoList);//权属信息
                    respServiceData.setServiceCode(Msgagger.ESFSDQSERVICE_CODE);
                    respServiceDataList.add(respServiceData);
                    //sj_sjsq.setImmovableRightInfoVoList(immovableRightInfoVoList);
                }
            }
            List<Sj_Info_Dyhtxx> sjInfoDyhtxxList = new ArrayList<>();
            sjInfoDyhtxxList.add(sjInfoDyhtxx);
            RespServiceData respServiceData = new RespServiceData();
            respServiceData.setServiceDataInfos(sjInfoDyhtxxList);//合同信息
            respServiceData.setServiceCode(Msgagger.DYHTSERVICE_CODE);
            respServiceDataList.add(respServiceData);
            sj_sjsq.setServiceDatas(respServiceDataList);
            //respServiceDataList(sjInfoDyhtxx);
        } catch (Exception e) {
            log.error("e" + e);
            e.getMessage();
        }
    }

    /**
     * 权属信息绑定
     *
     * @param realEstateInfoVo
     * @param immovableRightInfoVoList
     */
    private void ClQsxx(RealEstateInfoVo realEstateInfoVo, List<SJ_Info_Bdcqlxgxx> immovableRightInfoVoList) {
        SJ_Info_Bdcqlxgxx sj_info_bdcqlxgxx = new SJ_Info_Bdcqlxgxx();
        if (null != realEstateInfoVo.getRealEstateId()) {
            sj_info_bdcqlxgxx.setImmovableCertificateNo(realEstateInfoVo.getRealEstateId());//不动产权证
            sj_info_bdcqlxgxx.setCertificateType(Msgagger.BDCQZ);
            if (null != realEstateInfoVo.getRealEstateUnitInfoVoList() && realEstateInfoVo.getRealEstateUnitInfoVoList().size() != 0) {
                List<SJ_Bdc_Gl> glImmovableVoList = new ArrayList<>();
                for (RealEstateUnitInfoVo realEstateUnitInfoVo : realEstateInfoVo.getRealEstateUnitInfoVoList()) {
                    sj_info_bdcqlxgxx.setImmovableSite(realEstateUnitInfoVo.getSit());
                    if (null != realEstateUnitInfoVo.getArchitectureAera() && !realEstateUnitInfoVo.getArchitectureAera().equals("")) {
                        sj_info_bdcqlxgxx.setArchitecturalArea(new BigDecimal(realEstateUnitInfoVo.getArchitectureAera()));
                    }
                    ClFwInfo(glImmovableVoList, realEstateUnitInfoVo);
                }
                sj_info_bdcqlxgxx.setGlImmovableVoList(glImmovableVoList);
            }
        } else if (null != realEstateInfoVo.getLandCertificate()) {
            SJ_Info_Bdcqlxgxx bdcqlxgxx = new SJ_Info_Bdcqlxgxx();
            bdcqlxgxx.setLandCertificateNo(realEstateInfoVo.getLandCertificate());//土地证号
            bdcqlxgxx.setCertificateType(Msgagger.TDZ);
            immovableRightInfoVoList.add(bdcqlxgxx);
        }
        //权利人
        if (null != realEstateInfoVo.getObligeeInfoVoList() && realEstateInfoVo.getObligeeInfoVoList().size() != 0) {
            List<SJ_Qlr_Gl> glObligeeVoList = new ArrayList<>();
            for (ObligeeInfoVo obligeeInfoVo : realEstateInfoVo.getObligeeInfoVoList()) {
                ClQlr(obligeeInfoVo, glObligeeVoList, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR);
            }
            sj_info_bdcqlxgxx.setGlObligeeVoList(glObligeeVoList);
        }
        immovableRightInfoVoList.add(sj_info_bdcqlxgxx);
    }

    /**
     * 处理房屋信息
     *
     * @param glImmovableVoList
     * @param realEstateUnitInfoVo
     */
    private void ClFwInfo(List<SJ_Bdc_Gl> glImmovableVoList, RealEstateUnitInfoVo realEstateUnitInfoVo) {
        SJ_Bdc_Gl sjBdcGl = new SJ_Bdc_Gl();
        sjBdcGl.setImmovableType(Msgagger.FANGDI);
        SJ_Bdc_Fw_Info sjBdcFwInfo = new SJ_Bdc_Fw_Info();
        sjBdcFwInfo.setImmovableUnitNumber(realEstateUnitInfoVo.getRealEstateUnitId());
        sjBdcFwInfo.setHouseholdNumber(realEstateUnitInfoVo.getHouseholdId());
        sjBdcFwInfo.setSeatNumber(realEstateUnitInfoVo.getBuildingId());
        sjBdcFwInfo.setHouseholdMark(realEstateUnitInfoVo.getAccountId());
        sjBdcFwInfo.setHouseLocation(realEstateUnitInfoVo.getSit());
        sjBdcFwInfo.setRoomMark(realEstateUnitInfoVo.getRoomId());
        sjBdcFwInfo.setUnitMark(realEstateUnitInfoVo.getUnitId());
        sjBdcFwInfo.setProjectName(realEstateUnitInfoVo.getProjectName());
        sjBdcFwInfo.setArchitecturalArea(new BigDecimal(realEstateUnitInfoVo.getArchitectureAera()));
        sjBdcFwInfo.setBuildingName(realEstateUnitInfoVo.getArchitectureName());
        sjBdcGl.setFwInfo(sjBdcFwInfo);
        glImmovableVoList.add(sjBdcGl);
    }


    private void ClMfr(SalerInfoVo salerInfoVo, List<SJ_Qlr_Gl> glMortgagorVoList) {
        SJ_Qlr_Gl sj_qlr_gl = new SJ_Qlr_Gl();
        sj_qlr_gl.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_SFZ);
        sj_qlr_gl.setObligeeName(salerInfoVo.getSalerName());
        SJ_Qlr_Info relatedPerson = new SJ_Qlr_Info();
        relatedPerson.setObligeeDocumentType(salerInfoVo.getSalerIdType());
        relatedPerson.setObligeeDocumentNumber(salerInfoVo.getSalerId());
        relatedPerson.setObligeeName(salerInfoVo.getSalerName());
        sj_qlr_gl.setRelatedPerson(relatedPerson);
        glMortgagorVoList.add(sj_qlr_gl);
    }

    private void ClQlr(ObligeeInfoVo obligeeInfoVo, List<SJ_Qlr_Gl> glObligeeVoList, String type) {
        SJ_Qlr_Gl sj_qlr_gl = new SJ_Qlr_Gl();
        sj_qlr_gl.setObligeeType(type);
        sj_qlr_gl.setObligeeName(obligeeInfoVo.getObligeeName());
        SJ_Qlr_Info relatedPerson = new SJ_Qlr_Info();
        relatedPerson.setObligeeDocumentType(MortgageDocumentType.Sc(obligeeInfoVo.getObligeeIdType()));
        relatedPerson.setObligeeDocumentNumber(obligeeInfoVo.getObligeeId());
        sj_qlr_gl.setSharedMode(CommonmodeEnum.Sc(obligeeInfoVo.getCommonWay()));
        String share = "";
        if (null == obligeeInfoVo.getSharedShare()) {
            sj_qlr_gl.setSharedValue(share);
        } else {
            if (obligeeInfoVo.getSharedShare().contains("%")) {
                share = obligeeInfoVo.getSharedShare().replace("%", "");
            }
            sj_qlr_gl.setSharedValue(share);
        }
        relatedPerson.setObligeeName(obligeeInfoVo.getObligeeName());
        sj_qlr_gl.setRelatedPerson(relatedPerson);
        glObligeeVoList.add(sj_qlr_gl);
    }

    private void ClDyqr(MortgageeInfoVo mortgageeInfoVo, List<SJ_Qlr_Gl> glMortgageHolderVoList) {
        SJ_Qlr_Gl sj_qlr_gl = new SJ_Qlr_Gl();
        sj_qlr_gl.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYQR);
        sj_qlr_gl.setObligeeName(mortgageeInfoVo.getMortgageeName());
        SJ_Qlr_Info relatedPerson = new SJ_Qlr_Info();
        relatedPerson.setObligeeDocumentType(MortgageDocumentType.Sc(mortgageeInfoVo.getMortgageeIdType()));
        relatedPerson.setObligeeDocumentNumber(mortgageeInfoVo.getMortgageeId());
        relatedPerson.setObligeeName(mortgageeInfoVo.getMortgageeName());
        sj_qlr_gl.setRelatedPerson(relatedPerson);
        glMortgageHolderVoList.add(sj_qlr_gl);
    }

    private void ClDyr(MortgagorInfoVo mortgagorInfoVo, List<SJ_Qlr_Gl> glMortgagorVoList) {
        SJ_Qlr_Gl sj_qlr_gl = new SJ_Qlr_Gl();
        sj_qlr_gl.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYR);
        sj_qlr_gl.setObligeeName(mortgagorInfoVo.getMortgagorName());
        SJ_Qlr_Info relatedPerson = new SJ_Qlr_Info();
        relatedPerson.setObligeeDocumentType(MortgageDocumentType.Sc(mortgagorInfoVo.getMortgagorIdType()));
        relatedPerson.setObligeeDocumentNumber(mortgagorInfoVo.getMortgagorId());
        relatedPerson.setObligeeName(mortgagorInfoVo.getMortgagorName());
        sj_qlr_gl.setRelatedPerson(relatedPerson);
        glMortgagorVoList.add(sj_qlr_gl);
    }

    private void ClZwr(ObligorInfoVo obligorInfoVo, List<SJ_Qlr_Gl> glObligorVoList) {
        SJ_Qlr_Gl sj_qlr_gl = new SJ_Qlr_Gl();
        sj_qlr_gl.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_ZWR);
        sj_qlr_gl.setObligeeName(obligorInfoVo.getObligorName());
        SJ_Qlr_Info relatedPerson = new SJ_Qlr_Info();
        relatedPerson.setObligeeDocumentType(MortgageDocumentType.Sc(obligorInfoVo.getObligorIdType()));
        relatedPerson.setObligeeDocumentNumber(obligorInfoVo.getObligorId());
        relatedPerson.setObligeeName(obligorInfoVo.getObligorName());
        sj_qlr_gl.setRelatedPerson(relatedPerson);
        glObligorVoList.add(sj_qlr_gl);
    }

    //抵押人代理人
    private void ClDyrWtdlr(MortgagorAgentInfoVo mortgagorAgentInfoVo, List<SJ_Qlr_Gl> glObligorVoList) {
        SJ_Qlr_Gl sj_qlr_gl = new SJ_Qlr_Gl();
        sj_qlr_gl.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_WTDYRDLR);
        sj_qlr_gl.setObligeeName(mortgagorAgentInfoVo.getMortgagorAgentName());
        SJ_Qlr_Info relatedPerson = new SJ_Qlr_Info();
        relatedPerson.setObligeeDocumentType(MortgageDocumentType.Sc(mortgagorAgentInfoVo.getMortgagorAgentIdType()));
        relatedPerson.setObligeeDocumentNumber(mortgagorAgentInfoVo.getMortgagorAgentId());
        relatedPerson.setObligeeName(mortgagorAgentInfoVo.getMortgagorAgentName());
        sj_qlr_gl.setRelatedPerson(relatedPerson);
        glObligorVoList.add(sj_qlr_gl);
    }

    //抵押人代理人
    private void ClDyqrWtdlr(MortgageeAgentInfoVo mortgageeAgentInfoVo, List<SJ_Qlr_Gl> glObligorVoList) {
        SJ_Qlr_Gl sj_qlr_gl = new SJ_Qlr_Gl();
        sj_qlr_gl.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_WTDYQRDLR);
        sj_qlr_gl.setObligeeName(mortgageeAgentInfoVo.getMortgageeAgentName());
        SJ_Qlr_Info relatedPerson = new SJ_Qlr_Info();
        relatedPerson.setObligeeDocumentType(MortgageDocumentType.Sc(mortgageeAgentInfoVo.getMortgageeAgentIdType()));
        relatedPerson.setObligeeDocumentNumber(mortgageeAgentInfoVo.getMortgageeAgentId());
        relatedPerson.setObligeeName(mortgageeAgentInfoVo.getMortgageeAgentName());
        sj_qlr_gl.setRelatedPerson(relatedPerson);
        glObligorVoList.add(sj_qlr_gl);
    }


    /**
     * 地税信息处理
     *
     * @param
     * @return
     */
    public ObjectRestResponse sqTaxation(String htbh) throws Exception {
        ObjectRestResponse resultRV = new ObjectRestResponse();
        Map<String, String> header = new HashMap<>();
        header.put("api_id", qsapiId);
        header.put("from_user", qsfromUser);
        Map<String, Object> param = new HashMap<>();
        param.put("htbh", htbh);
        List<Sj_Info_Qsxx> qsxxList = new ArrayList<>();
        param.put("swjgdm", swjgdm);
        param.put("swrydm", swrydm);
        param.put("serviceMethod", serviceMethod);
        String result = netSignUtils.clfDsxx(serviceMethod, swjgdm, swrydm, htbh, "http://" + ip + ":" + post + "/WxfcjyJmssendAction.do", qsfromUser, qsapiId);
        JSONObject resultObject = JSONObject.fromObject(result);
        try {
            if (resultObject.getJSONArray("griddata") != null) {
                JSONArray jsonArray = resultObject.getJSONArray("griddata");
                for (int i = 0; i < jsonArray.size(); i++) {
                    clTaxation(jsonArray.getJSONObject(i), qsxxList);
                }
                return resultRV.data(qsxxList);
            }
        } catch (Exception ex) {
            resultRV.setMessage(Msgagger.DATA_FAILURE);
            resultRV.setStatus(20500);
            return resultRV;
        }
        return resultRV.data(qsxxList);
    }

    /**
     * 二手房交易合同处理
     *
     * @param paramEntity
     * @return
     */
    public ObjectRestResponse sqTransactionContract(ParamEntity paramEntity) throws IOException {
        paramEntity.setOrgId(1);
        String param = com.alibaba.fastjson.JSONObject.toJSONString(paramEntity, SerializerFeature.PrettyFormat);
        String resultJosn = HttpClientUtil.post(jyfromUser, jyapiId, param, "http://" + ip + ":" + post + "/sqservice/sh/secondInfo");
        return ClHtxx(resultJosn);
    }

    /**
     * 一手房交易合同处理
     *
     * @param paramEntity
     * @return
     */
    public ObjectRestResponse spfTransactionContract(ParamEntity paramEntity) throws IOException {
        paramEntity.setOrgId(1);
        String param = com.alibaba.fastjson.JSONObject.toJSONString(paramEntity, SerializerFeature.PrettyFormat);
//        String resultJosn=HttpClientUtil.post(jySpfFromUser,jySpfApiId,param,"http://"+jySpfIp+":"+jySpfPost+"/sqservice/sh/firstInfo");
        String resultJosn = "{\n" +
                "    \"dataInfo\": {\n" +
                "        \"sInfo\": \"[{\\\"BDCDYH\\\":null,\\\"DJ\\\":2957.72,\\\"DLMC\\\":null,\\\"DZHTFullUrl\\\":\\\"http://222.187.193.194:8053/sqfc/docx/TNAV?action=runtime.Preview&tmpid=3&docid=13085929&syflag=1\\\",\\\"FWCX\\\":null,\\\"FWDY\\\":null,\\\"FWDZ\\\":\\\"宿城区隆城颐和2#1806\\\",\\\"FWFH\\\":\\\"1806\\\",\\\"FWJG\\\":\\\"钢混\\\",\\\"FWLX\\\":null,\\\"FWQDSJ\\\":null,\\\"FWXZ\\\":\\\"动迁房\\\",\\\"FWYT\\\":\\\"住宅\\\",\\\"FWZH\\\":1,\\\"GMFDHHM\\\":\\\"13812301110\\\",\\\"GMFDZ\\\":\\\"宿迁市宿城区隆城颐和2栋1806\\\",\\\"GMFGJ\\\":\\\"中国\\\",\\\"GMFXM\\\":\\\"包银珍\\\",\\\"GMFZJHM\\\":\\\"320824194208210048\\\",\\\"GMFZJLX\\\":\\\"身份证\\\",\\\"HTBAH\\\":\\\"BA-1507310022\\\",\\\"HTJE\\\":232033,\\\"HTQDRQ\\\":\\\"2015-07-31\\\",\\\"JDXZ\\\":null,\\\"JZMJ\\\":78.45,\\\"LCZS\\\":21,\\\"MFDHHM\\\":\\\"15850908245\\\",\\\"MFDZ\\\":\\\"宿迁市洪泽湖路140号建设大厦内\\\",\\\"MFGJ\\\":\\\"中国\\\",\\\"MFXM\\\":\\\"宿迁市城市建设投资（集团）有限公司\\\",\\\"MFZJHM\\\":\\\"913213007317410215\\\",\\\"MFZJLX\\\":\\\"统一社会信用代码\\\",\\\"PGJG\\\":null,\\\"SZLC\\\":19,\\\"XQMC\\\":\\\"隆城颐和小区一期\\\",\\\"XZQH\\\":\\\"老城区\\\",\\\"YWLX\\\":\\\"商品房网签合同备案\\\",\\\"ZJLXDH\\\":\\\"15850908245\\\"}]\"\n" +
                "    },\n" +
                "    \"success\": true,\n" +
                "    \"errorCode\": \"0\"\n" +
                "}";
        return ClHtxx(resultJosn);
    }


    /**
     * 处理交易合同返回数据
     *
     * @param resultJosn
     * @return
     */
    private ObjectRestResponse ClHtxx(String resultJosn) {
        ObjectRestResponse resultRV = new ObjectRestResponse();
        List<BusinessContract> businessContractList = new ArrayList<>();
        JSONObject jyObject = JSONObject.fromObject(resultJosn);
        if (resultJosn.equals("0")) {
            resultRV.setMessage(Msgagger.INTERCE_NULL);
            resultRV.setStatus(20500);
            return resultRV;
        } else if (jyObject.getBoolean("success") == false) {
            resultRV.setMessage(Msgagger.DYINTERCE_K);
            resultRV.setStatus(20500);
            return resultRV;
        }
        try {
            JSONObject dataInfo = jyObject.getJSONObject("dataInfo");
            JSONArray sInfoArray = dataInfo.getJSONArray("sInfo");
            if (null != sInfoArray) {
                for (int i = 0; i < sInfoArray.size(); i++) {
                    clTransactionContract(sInfoArray.getJSONObject(i), businessContractList);
                }
            }
        } catch (Exception e) {
            resultRV.setMessage(Msgagger.INTERCE_NULL);
            resultRV.setStatus(20500);
            return resultRV;
        }
        return resultRV.data(businessContractList);
    }


    private void clTaxation(JSONObject jsonObject, List<Sj_Info_Qsxx> sj_info_qsxxList) {
        Sj_Info_Qsxx sjInfoQsxx = new Sj_Info_Qsxx();
        sjInfoQsxx.setXtsphm(jsonObject.getString("XTSPHM"));
        sjInfoQsxx.setHtbh(jsonObject.getString("HTBH"));
        sjInfoQsxx.setNsrsbh(jsonObject.getString("NSRSBH"));
        sjInfoQsxx.setNsrmc(jsonObject.getString("NSRMC"));
        sjInfoQsxx.setZrfcsfbz(jsonObject.getString("ZRFCSFBZ"));
        sjInfoQsxx.setDzsphm(jsonObject.getString("DZSPHM"));
        sjInfoQsxx.setPzzlDm(jsonObject.getString("PZZL_DM"));
        sjInfoQsxx.setPzzgDm(jsonObject.getString("PZZG_DM"));
        sjInfoQsxx.setPzhm(jsonObject.getString("PZHM"));
        sjInfoQsxx.setSkssqq(jsonObject.getString("SKSSQQ"));
        sjInfoQsxx.setSkssqz(jsonObject.getString("SKSSQZ"));
        sjInfoQsxx.setZsxmDm(jsonObject.getString("ZSXM_DM"));
        sjInfoQsxx.setZspmDm(jsonObject.getString("ZSPM_DM"));
        sjInfoQsxx.setZszmDm(jsonObject.getString("ZSZM_DM"));
        sjInfoQsxx.setZsxmMc(jsonObject.getString("ZSXM_MC"));
        sjInfoQsxx.setZspmMc(jsonObject.getString("ZSPM_MC"));
        sjInfoQsxx.setZszmMc(jsonObject.getString("ZSZM_MC"));
        sjInfoQsxx.setJsyj(jsonObject.getString("JSYJ"));
        sjInfoQsxx.setSl(jsonObject.getString("SL_1"));
        sjInfoQsxx.setSjje(jsonObject.getLong("SJJE"));
        sjInfoQsxx.setZgswskfjDm(jsonObject.getString("ZSSWJG_DM"));
        sjInfoQsxx.setZsswjgDm(jsonObject.getString("SKSSSWJG_DM"));
        sjInfoQsxx.setSkssswjgDm(jsonObject.getString("ZGSWSKFJ_MC"));
        sjInfoQsxx.setZgswskfjMc(jsonObject.getString("ZSSWJG_MC"));
        sjInfoQsxx.setZsswjgMc(jsonObject.getString("SKSSSWJG_MC"));
        sjInfoQsxx.setKjrq(jsonObject.getString("kjrq"));
        sjInfoQsxx.setBz(jsonObject.getString("BZ"));
        sjInfoQsxx.setProvideUnit(Msgagger.GXPT);
        sjInfoQsxx.setDataComeFromMode(Msgagger.INTERCE_DY);
        sjInfoQsxx.setDataJson(jsonObject.toString());
        sj_info_qsxxList.add(sjInfoQsxx);
    }


    /**
     * 处理数据
     *
     * @param jsonObject
     * @param businessContractList
     */
    private void clTransactionContract(JSONObject jsonObject, List<BusinessContract> businessContractList) {
        BusinessContract businessContract = new BusinessContract();
        businessContract.setContractRecordNumber(jsonObject.getString("HTBAH"));
        businessContract.setContractSignTime(jsonObject.getString("HTQDRQ"));
        businessContract.setContractAmount(jsonObject.getString("HTJE"));
        businessContract.setProvideUnit(Msgagger.GXPT);
        businessContract.setDataComeFromMode(Msgagger.INTERCE_DY);
        businessContract.setDataJson(jsonObject.toString());
        List<GlImmovable> glImmovableList = new ArrayList<>();
        GlImmovable glImmovable = new GlImmovable();
        glImmovable.setImmovableType(Msgagger.FANGDI);
        FwInfo fwInfo = new FwInfo();
        fwInfo.setSeatNumber(jsonObject.getString("FWZH")); //房屋证号
        fwInfo.setRoomMark(jsonObject.getString("FWFH"));
        fwInfo.setTotalStorey(jsonObject.getString("LCZS"));
        fwInfo.setLocationStorey(jsonObject.getString("SZLC"));
        if (StringUtils.isNotEmpty(jsonObject.getString("JZMJ"))) {
            fwInfo.setArchitecturalArea(new BigDecimal(jsonObject.getString("JZMJ")));
        }
        fwInfo.setHouseType(jsonObject.getString("FWLX")); //房屋类型
        fwInfo.setHouseNature(jsonObject.getString("FWXZ"));//房屋性质
        fwInfo.setHouseStructure(jsonObject.getString("FWJG")); //房屋结构
        fwInfo.setHouseLocation(jsonObject.getString("FWDZ"));//房屋地址
        fwInfo.setImmovableUnitNumber(jsonObject.getString("BDCDYH")); //不动产单元号
        fwInfo.setImmovablePlanningUse(jsonObject.getString("FWYT"));//房屋用途
        glImmovable.setFwInfo(fwInfo);
        glImmovableList.add(glImmovable);
        businessContract.setGlImmovableVoList(glImmovableList);
        //权利人
        List<GlHouseSeller> glHouseSellerVoList = new ArrayList<>();
        if (StringUtils.isNotBlank(jsonObject.getString("MFXM"))) {
            if (jsonObject.getString("MFXM").contains(",")) {
                String[] obligorName = jsonObject.getString("MFXM").split(",");
                String[] obligorZjhm = jsonObject.getString("MFZJHM").split(",");
                String[] obligorZjlx = jsonObject.getString("MFZJLX").split(",");
                for (int i = 0; i < obligorName.length; i++) {
                    GlHouseSeller glHouseSeller = new GlHouseSeller();
                    glHouseSeller.setObligeeName(obligorName[i]);
                    glHouseSeller.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR);
                    RelatedPerson relatedPerson = new RelatedPerson();
                    relatedPerson.setObligeeDocumentType(obligorZjlx[i]);
                    relatedPerson.setObligeeName(obligorName[i]);
                    relatedPerson.setObligeeDocumentNumber(obligorZjhm[i]);
                    glHouseSeller.setRelatedPerson(relatedPerson);
                    glHouseSellerVoList.add(glHouseSeller);
                }
            } else {
                GlHouseSeller glHouseSeller = new GlHouseSeller();
                glHouseSeller.setObligeeName(jsonObject.getString("MFXM"));
                glHouseSeller.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR);
                RelatedPerson relatedPerson = new RelatedPerson();
                relatedPerson.setObligeeDocumentType(jsonObject.getString("MFZJLX"));
                relatedPerson.setObligeeName(jsonObject.getString("MFXM"));
                relatedPerson.setObligeeDocumentNumber(jsonObject.getString("MFZJHM"));
                glHouseSeller.setRelatedPerson(relatedPerson);
                glHouseSellerVoList.add(glHouseSeller);
            }
            businessContract.setGlHouseSellerVoList(glHouseSellerVoList);
        }
        //购买人
        List<GlHouseBuyer> glHouseBuyerList = new ArrayList<>();
        if (StringUtils.isNotBlank(jsonObject.getString("MFXM"))) {
            if (jsonObject.getString("GMFXM").contains(",")) {
                String[] obligeeName = jsonObject.getString("GMFXM").split(",");
                String[] obligeeZjhm = jsonObject.getString("GMFZJHM").split(",");
                String[] obligeeZjhx = jsonObject.getString("GMFZJLX").split(",");
                for (int i = 0; i < obligeeName.length; i++) {
                    GlHouseBuyer glHouseBuyer = new GlHouseBuyer();
                    glHouseBuyer.setObligeeName(obligeeName[i]);
                    glHouseBuyer.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_GFZ);
                    RelatedPerson gmrelatedPerson = new RelatedPerson();
                    gmrelatedPerson.setObligeeDocumentType(obligeeZjhx[i]);
                    gmrelatedPerson.setObligeeName(obligeeName[i]);
                    gmrelatedPerson.setObligeeDocumentNumber(obligeeZjhm[i]);
                    glHouseBuyer.setRelatedPerson(gmrelatedPerson);
                    glHouseBuyerList.add(glHouseBuyer);
                }
            } else {
                GlHouseBuyer glHouseBuyer = new GlHouseBuyer();
                glHouseBuyer.setObligeeName(jsonObject.getString("GMFXM"));
                glHouseBuyer.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_GFZ);
                RelatedPerson gmrelatedPerson = new RelatedPerson();
                gmrelatedPerson.setObligeeDocumentType(jsonObject.getString("GMFZJLX"));
                gmrelatedPerson.setObligeeName(jsonObject.getString("GMFXM"));
                gmrelatedPerson.setObligeeDocumentNumber(jsonObject.getString("GMFZJHM"));
                glHouseBuyer.setRelatedPerson(gmrelatedPerson);
                glHouseBuyerList.add(glHouseBuyer);
            }
        }
        businessContract.setGlHouseBuyerVoList(glHouseBuyerList);
        businessContractList.add(businessContract);
    }


}
