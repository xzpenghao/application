package com.springboot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.config.ZtgeoBizException;
import com.springboot.constant.penghao.BizOrBizExceptionConstant;
import com.springboot.entity.chenbin.personnel.pub_use.SJ_Sjsq_User_Ext;
import com.springboot.popj.json_data.*;
import com.springboot.popj.pub_data.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class SysPubDataDealUtil {

    public static SJ_Sjsq parseReceiptData(String receiptData, String serviceCode, String serviceDataTo, String receiptNumber) throws ParseException {
        JSONReceiptData sjsq_JSON_str = JSON.parseObject(receiptData, JSONReceiptData.class);//json转实体类
        System.out.println(JSONObject.toJSONString(sjsq_JSON_str));
        SJ_Sjsq sjsq = parseReceiptData(sjsq_JSON_str, serviceCode, serviceDataTo, receiptNumber);
        return sjsq;
    }

    public static SJ_Sjsq parseReceiptData(JSONReceiptData sjsq_JSON_str, String serviceCode, String serviceDataTo, String receiptNumber) {
        String sc_temp = serviceCode;
        String sdt_temp = serviceDataTo;
        String JSON_serviceDatas = sjsq_JSON_str.getServiceDatas();
        String JSON_userExtVoList = sjsq_JSON_str.getUserExtVoList();
        sjsq_JSON_str.setServiceDatas(null);
        sjsq_JSON_str.setUserExtVoList(null);
        SJ_Sjsq sjsq = copyJSONReceiptDataToSjsq(sjsq_JSON_str);//信息拷贝至收件申请对象中
//        System.out.println("2:"+JSONObject.toJSONString(sjsq));
        if (sjsq == null) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_INFO_NULL_ERROR);
        }
        if (StringUtils.isNotBlank(receiptNumber) && StringUtils.isBlank(sjsq.getReceiptNumber())) {//处理收件编号
            sjsq.setReceiptNumber(receiptNumber);
        }
        if (StringUtils.isBlank(sjsq.getReceiptNumber())) {//验证收件编号
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_NUMBER_NULL_ERROR);
        }
        if(StringUtils.isNotBlank(JSON_userExtVoList)){//处理用户扩展
            List<SJ_Sjsq_User_Ext> userExts = JSONArray.parseArray(JSON_userExtVoList,SJ_Sjsq_User_Ext.class);
            if(userExts!=null){
                for(SJ_Sjsq_User_Ext userExt:userExts){
                    if(StringUtils.isBlank(userExt.getReceiptNumber())){
                        userExt.setReceiptNumber(sjsq.getReceiptNumber());
                    } else if(!userExt.getReceiptNumber().equals(sjsq.getReceiptNumber())){
                        throw new ZtgeoBizException("对接系统用户非法注入");
                    }
                }
            }
            sjsq.setUserExtVoList(userExts);
        }
        if (JSON_serviceDatas != null && JSON_serviceDatas.length() > 0) {
            List<JSONServiceData> serviceDatas = JSONArray.parseArray(JSON_serviceDatas, JSONServiceData.class);
            for (JSONServiceData serviceData : serviceDatas) {
                serviceCode = sc_temp;
                serviceDataTo = sdt_temp;
                String serviceCode_temp = serviceData.getServiceCode();
                String serviceDataTo_temp = serviceData.getServiceDataTo();
                if (serviceCode_temp != null && serviceCode_temp.length() > 0) {
                    serviceCode = serviceCode_temp;
                }
                if (serviceDataTo_temp != null && serviceDataTo_temp.length() > 0) {
                    serviceDataTo = serviceDataTo_temp;
                }
                String JSON_serviceDataInfos = serviceData.getServiceDataInfos();
                switch (serviceDataTo) {
                    //使用操作表作为判断条件
                    case BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE:
                        dealBdcqlxxInfos(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    case BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_RECEIPT_SERVICE:
                        dealBdcdyxxInfos(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    case BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE:
                        dealJyhtxxInfos(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    case BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE:
                        dealDyhtxxInfos(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    case BizOrBizExceptionConstant.TAXATION_RECEIPT_SERVICE:
                        dealQsxxInfos(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    case BizOrBizExceptionConstant.HANDLE_RESULT_SERVICE:
                        dealHandleResults(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    default:
                        log.error("入库表标识为：" + serviceDataTo + "的表标识常量未定义");
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_TABLE_ERROR_MSG);
                }
            }
        }
        return sjsq;
    }

    //处理权利信息
    public static void dealBdcqlxxInfos(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode) {

        List<JSONBdcqlxgxx> qlxgxxs = JSONArray.parseArray(JSON_serviceDataInfos, JSONBdcqlxgxx.class);//证书权利集合的JSON对象
        List<SJ_Info_Bdcqlxgxx> sj_qlxgxxs = sjsq.getImmovableRightInfoVoList() == null ? new ArrayList<SJ_Info_Bdcqlxgxx>() : sjsq.getImmovableRightInfoVoList();//证书权利集合

        for (JSONBdcqlxgxx qlxgxx : qlxgxxs) {
            boolean HaveOrNot = true;   //是否传入了义务人的情况
            System.out.println("11:" + qlxgxx.getRegistrationDate());
            String JSON_glImmovableVoList = qlxgxx.getGlImmovableVoList();
            String JSON_glObligeeVoList = qlxgxx.getGlObligeeVoList();
            String JSON_glObligorVoList = qlxgxx.getGlObligorVoList();
            String JSON_glAgentVoList = qlxgxx.getGlAgentVoList();
            String JSON_itsRightVoList = qlxgxx.getItsRightVoList();
            String JSON_glAgentObligorVoList = qlxgxx.getGlAgentObligorVoList();//义务代理人
            String JSON_eBookCert = qlxgxx.getEBookCert();
            String JSON_bookPics = qlxgxx.getBookPics();

            if (JSON_glObligorVoList == null || JSON_glObligorVoList.length() <= 0) {
                HaveOrNot = false;  //判断义务人是否必须
            }

            qlxgxx.setGlImmovableVoList(null);
            qlxgxx.setGlObligeeVoList(null);
            qlxgxx.setGlObligorVoList(null);
            qlxgxx.setGlAgentVoList(null);
            qlxgxx.setItsRightVoList(null);
            qlxgxx.setGlAgentObligorVoList(null);
            qlxgxx.setEBookCert(null);
            qlxgxx.setBookPics(null);

            //反转出不动产权利信息
            SJ_Info_Bdcqlxgxx sj_qlxgxx = JSON.parseObject(JSON.toJSONString(qlxgxx), SJ_Info_Bdcqlxgxx.class);
            baseSetting(sj_qlxgxx, serviceCode, sjsq.getReceiptNumber());

            /*
             *  不动产图属关联信息处理
             *  证书中关联的不动产
             */
            List<SJ_Bdc_Gl> sj_bdcgls = copyJSONBdcToSJBdc(JSON_glImmovableVoList, BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE, sj_qlxgxx, sjsq);

            /*
             *  不动产权利人关联信息处理
             */
            List<SJ_Qlr_Gl> sj_qlrgls = copyJSONQlrToSJQlr(JSON_glObligeeVoList, BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR);

            /*
             *  不动产义务人关联信息处理
             */
            if (HaveOrNot) {
                List<SJ_Qlr_Gl> sj_ywrgls = copyJSONQlrToSJQlr(JSON_glObligorVoList, BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_YWR);
                sj_qlxgxx.setGlObligorVoList(sj_ywrgls);
            }

            if(StringUtils.isNotBlank(JSON_glAgentVoList)){
                List<SJ_Qlr_Gl> sj_dlrgl = copyJSONQlrToSJQlr(JSON_glAgentVoList,BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR_DLR);
                sj_qlxgxx.setGlAgentVoList(sj_dlrgl);
            }

            if(StringUtils.isNotBlank(JSON_glAgentObligorVoList)){
                List<SJ_Qlr_Gl> sj_ywdlrgl = copyJSONQlrToSJQlr(JSON_glAgentObligorVoList,BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_YWR_DLR);
                sj_qlxgxx.setGlAgentObligorVoList(sj_ywdlrgl);
            }

            if(StringUtils.isNotBlank(JSON_itsRightVoList)){
                List<SJ_Its_Right> itsRightVoList = JSONArray.parseArray(JSON_itsRightVoList,SJ_Its_Right.class);
                sj_qlxgxx.setItsRightVoList(itsRightVoList);
            }
            //PDF
            if(StringUtils.isNotBlank(JSON_eBookCert)){
                SJ_Book_Cert eBookCert = JSONObject.parseObject(JSON_eBookCert,SJ_Book_Cert.class);
                sj_qlxgxx.setEBookCert(eBookCert);
            }
            //分层分户图
            if(StringUtils.isNotBlank(JSON_bookPics)){
                List<SJ_Book_Pic_ext> bookPics = JSONArray.parseArray(JSON_bookPics,SJ_Book_Pic_ext.class);
                sj_qlxgxx.setBookPics(bookPics);
            }
            sj_qlxgxx.setGlImmovableVoList(sj_bdcgls);
            sj_qlxgxx.setGlObligeeVoList(sj_qlrgls);

            sj_qlxgxxs.add(sj_qlxgxx);
        }
        sjsq.setImmovableRightInfoVoList(sj_qlxgxxs);
    }

    //处理抵押信息
    public static void dealBdcdyxxInfos(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode) {
        List<JSONBdcdyxgxx> dyxgxxs = JSONArray.parseArray(JSON_serviceDataInfos, JSONBdcdyxgxx.class);
        List<Sj_Info_Bdcdyxgxx> sj_dyxgxxs = sjsq.getImmovableCurrentMortgageInfoVoList() == null ? new ArrayList<Sj_Info_Bdcdyxgxx>() : sjsq.getImmovableCurrentMortgageInfoVoList();
        for (JSONBdcdyxgxx dyxgxx : dyxgxxs) {
            String JSON_glImmovableVoList = dyxgxx.getGlImmovableVoList();//不动产json
            String JSON_glMortgagorVoList = dyxgxx.getGlMortgagorVoList();//抵押人json
            String JSON_glMortgageHolderVoList = dyxgxx.getGlMortgageHolderVoList();//抵押权人json
            String JSON_glObligorInfoVoList=dyxgxx.getGlObligorInfoVoList(); //债务人
            dyxgxx.setGlImmovableVoList(null);
            dyxgxx.setGlMortgagorVoList(null);
            dyxgxx.setGlMortgageHolderVoList(null);
            dyxgxx.setGlObligorInfoVoList(null);
            //反转出不动产抵押信息
            Sj_Info_Bdcdyxgxx sj_dyxgxx = JSON.parseObject(JSON.toJSONString(dyxgxx), Sj_Info_Bdcdyxgxx.class);
            baseSetting(sj_dyxgxx, serviceCode, sjsq.getReceiptNumber());

            /*
             *  不动产抵押关联信息处理
             *  证明中不动产关联集合对象
             */
            List<SJ_Bdc_Gl> sj_bdcgls = copyJSONBdcToSJBdc(JSON_glImmovableVoList, BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_RECEIPT_SERVICE, sj_dyxgxx, sjsq);

            /*
             *  不动产抵押权人关联信息处理
             */
            List<SJ_Qlr_Gl> sj_dyqrgls = copyJSONQlrToSJQlr(JSON_glMortgageHolderVoList, BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYQR);

            //债务人处理
            if(StringUtils.isNotBlank(JSON_glObligorInfoVoList)) {
                List<SJ_Qlr_Gl> sj_zwrgls = copyJSONQlrToSJQlr(JSON_glObligorInfoVoList, BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_ZWR);
                sj_dyxgxx.setGlObligorInfoVoList(sj_zwrgls);
            }

            /*
             *  不动产抵押人关联信息处理
             */
            List<SJ_Qlr_Gl> sj_dyrgls = copyJSONQlrToSJQlr(JSON_glMortgagorVoList, BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYR);

            sj_dyxgxx.setGlImmovableVoList(sj_bdcgls);
            sj_dyxgxx.setGlMortgageHolderVoList(sj_dyqrgls);
            sj_dyxgxx.setGlMortgagorVoList(sj_dyrgls);

            sj_dyxgxxs.add(sj_dyxgxx);
        }
        sjsq.setImmovableCurrentMortgageInfoVoList(sj_dyxgxxs);
    }

    public static void dealJyhtxxInfos(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode) {
        List<JSONJyhtxx> jyhtxxs = JSONArray.parseArray(JSON_serviceDataInfos, JSONJyhtxx.class);
        List<Sj_Info_Jyhtxx> sj_jyhtxxs = new ArrayList<Sj_Info_Jyhtxx>();
        for (JSONJyhtxx jyhtxx : jyhtxxs) {
            String JSON_glImmovableVoList = jyhtxx.getGlImmovableVoList();
            String JSON_glHouseBuyerVoList = jyhtxx.getGlHouseBuyerVoList();
            String JSON_glHouseSellerVoList = jyhtxx.getGlHouseSellerVoList();
            String JSON_glAgentVoList = jyhtxx.getGlAgentVoList();
            String JSON_glAgentSellerVoList = jyhtxx.getGlAgentSellerVoList();
            String JSON_htDetail = jyhtxx.getHtDetail();
            jyhtxx.setGlHouseBuyerVoList(null);
            jyhtxx.setGlHouseSellerVoList(null);
            jyhtxx.setGlImmovableVoList(null);
            jyhtxx.setGlAgentVoList(null);
            jyhtxx.setGlAgentSellerVoList(null);
            jyhtxx.setHtDetail(null);
            //反转出合同信息
            Sj_Info_Jyhtxx sj_jyhtxx = JSON.parseObject(JSON.toJSONString(jyhtxx), Sj_Info_Jyhtxx.class);
            baseSetting(sj_jyhtxx, serviceCode, sjsq.getReceiptNumber());
            //不动产关联信息处理
            List<SJ_Bdc_Gl> sj_bdcgls = copyJSONBdcToSJBdc(JSON_glImmovableVoList, BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE, sj_jyhtxx, sjsq);
            //买房人处理
            List<SJ_Qlr_Gl> sj_mfrgls = copyJSONQlrToSJQlr(JSON_glHouseBuyerVoList, BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_GFZ);
            //卖方人处理
            List<SJ_Qlr_Gl> sj_sellergls = copyJSONQlrToSJQlr(JSON_glHouseSellerVoList, BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_SFZ);
            //代理人
            if(StringUtils.isNotBlank(JSON_glAgentVoList)){
                List<SJ_Qlr_Gl> sj_dlrgl = copyJSONQlrToSJQlr(JSON_glAgentVoList,BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR_DLR);
                sj_jyhtxx.setGlAgentVoList(sj_dlrgl);
            }
            //合同细节
            if(StringUtils.isNotBlank(JSON_htDetail)) {
                SJ_Jyht_Detail sjJyhtDetail = JSON.parseObject(JSON_htDetail, SJ_Jyht_Detail.class);
                sj_jyhtxx.setHtDetail(sjJyhtDetail);
            }
            /*
             *  卖方代理人关联信息处理
             */
            if(StringUtils.isNotBlank(JSON_glAgentSellerVoList)){
                List<SJ_Qlr_Gl> sj_ywdlrgl = copyJSONQlrToSJQlr(JSON_glAgentSellerVoList,BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_YWR_DLR);
                sj_jyhtxx.setGlAgentSellerVoList(sj_ywdlrgl);
            }
            sj_jyhtxx.setGlImmovableVoList(sj_bdcgls);
            sj_jyhtxx.setGlHouseBuyerVoList(sj_mfrgls);
            sj_jyhtxx.setGlHouseSellerVoList(sj_sellergls);
            sj_jyhtxxs.add(sj_jyhtxx);
        }
        //应该只有一个合同
        if (sj_jyhtxxs != null && sj_jyhtxxs.size() > 1) {
            log.error("交易合同数目异常");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_TRANSACTION_CONTRACT_COUNT_ERROR);
        }
        if (sj_jyhtxxs != null && sj_jyhtxxs.size() == 1) {
            sjsq.setTransactionContractInfo(sj_jyhtxxs.get(0));
        }
    }

    public static void dealDyhtxxInfos(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode) {
        List<JSONDyhtxx> dyhtxxs = JSONArray.parseArray(JSON_serviceDataInfos, JSONDyhtxx.class);
        List<Sj_Info_Dyhtxx> sj_dyhtxxs = new ArrayList<Sj_Info_Dyhtxx>();
        for (JSONDyhtxx dyhtxx : dyhtxxs) {
            String JSON_glImmovableVoList = dyhtxx.getGlImmovableVoList();
            String JSON_glMortgageHolderVoList = dyhtxx.getGlMortgageHolderVoList();//抵押权人
            String JSON_glMortgagorVoList = dyhtxx.getGlMortgagorVoList();//抵押人
            String JSON_glMortgageeAgentInfoVoList = dyhtxx.getGlMortgageeAgentInfoVoList();//抵押权代理人
            String JSON_glMortgagorAgentInfoVoList = dyhtxx.getGlMortgagorAgentInfoVoList();//抵押代理人
            String JSON_glObligorInfoVoList=dyhtxx.getGlObligorInfoVoList(); //债务人
            dyhtxx.setGlMortgagorVoList(null);
            dyhtxx.setGlMortgageHolderVoList(null);
            dyhtxx.setGlImmovableVoList(null);
            dyhtxx.setGlObligorInfoVoList(null);
            dyhtxx.setGlMortgageeAgentInfoVoList(null);
            dyhtxx.setGlMortgagorAgentInfoVoList(null);
            //反转出合同信息
            Sj_Info_Dyhtxx sj_dyhtxx = JSON.parseObject(JSON.toJSONString(dyhtxx), Sj_Info_Dyhtxx.class);
            baseSetting(sj_dyhtxx, serviceCode, sjsq.getReceiptNumber());
            //不动产关联信息处理
            List<SJ_Bdc_Gl> sj_bdcgls = copyJSONBdcToSJBdc(JSON_glImmovableVoList, BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE, sj_dyhtxx, sjsq);
            //抵押权人处理
            List<SJ_Qlr_Gl> sj_dyqrgls = copyJSONQlrToSJQlr(JSON_glMortgageHolderVoList, BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYQR);
            //抵押人处理
            List<SJ_Qlr_Gl> sj_dyrgls = copyJSONQlrToSJQlr(JSON_glMortgagorVoList, BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYR);
            //抵押权代理人处理
            if(StringUtils.isNotBlank(JSON_glMortgageeAgentInfoVoList)) {
                List<SJ_Qlr_Gl> sj_dyqdlrgls = copyJSONQlrToSJQlr(JSON_glMortgageeAgentInfoVoList, BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_WTDYQRDLR);
                sj_dyhtxx.setGlMortgageeAgentInfoVoList(sj_dyqdlrgls);
            }
            //债务人处理
            if(StringUtils.isNotBlank(JSON_glMortgageeAgentInfoVoList)) {
                List<SJ_Qlr_Gl> sj_zwrgls = copyJSONQlrToSJQlr(JSON_glObligorInfoVoList, BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_ZWR);
                sj_dyhtxx.setGlObligorInfoVoList(sj_zwrgls);
            }
            //抵押代理人处理
            if(StringUtils.isNotBlank(JSON_glMortgagorAgentInfoVoList)) {
                List<SJ_Qlr_Gl> sj_dydlrgls = copyJSONQlrToSJQlr(JSON_glMortgagorAgentInfoVoList, BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE, BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_WTDYRDLR);
                sj_dyhtxx.setGlMortgagorAgentInfoVoList(sj_dydlrgls);
            }
            sj_dyhtxx.setGlImmovableVoList(sj_bdcgls);
            sj_dyhtxx.setGlMortgageHolderVoList(sj_dyqrgls);
            sj_dyhtxx.setGlMortgagorVoList(sj_dyrgls);

            sj_dyhtxxs.add(sj_dyhtxx);
        }
        //应该只有一个合同
        if (sj_dyhtxxs != null && sj_dyhtxxs.size() > 1) {
            log.error("抵押合同数目异常");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_MORTGAGE_CONTRACT_COUNT_ERROR);
        }
        if (sj_dyhtxxs != null && sj_dyhtxxs.size() == 1) {
            sjsq.setMortgageContractInfo(sj_dyhtxxs.get(0));
        }
    }

    public static void dealQsxxInfos(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode) {
        List<JSONQsxx> qsxxs = JSONArray.parseArray(JSON_serviceDataInfos, JSONQsxx.class);
        List<Sj_Info_Qsxx> sj_qsxxs = new ArrayList<Sj_Info_Qsxx>();
        for (JSONQsxx qsxx : qsxxs) {
            Sj_Info_Qsxx sj_qsxx = JSON.parseObject(JSON.toJSONString(qsxx), Sj_Info_Qsxx.class);
            baseSetting(sj_qsxx, serviceCode, sjsq.getReceiptNumber());
            sj_qsxxs.add(sj_qsxx);
        }
        //只有一条地税信息
//        if (sj_qsxxs != null && sj_qsxxs.size() > 1) {
//            log.error("地税信息条数异常");
//            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_TAXATION_COUNT_ERROR);
//        }
        if(sj_qsxxs!=null){
            sjsq.setTaxInfoVoList(sj_qsxxs);
        }
    }

    public static void dealHandleResults(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode) {
        List<JSONHandleResult> handleResults = JSONArray.parseArray(JSON_serviceDataInfos, JSONHandleResult.class);
        List<SJ_Info_Handle_Result> sj_handleResults = sjsq.getHandleResultVoList() == null ? new ArrayList<SJ_Info_Handle_Result>() : sjsq.getHandleResultVoList();
        for (JSONHandleResult handleResult : handleResults) {
            SJ_Info_Handle_Result sj_handleResult = JSON.parseObject(JSON.toJSONString(handleResult), SJ_Info_Handle_Result.class);
            baseSetting(sj_handleResult, serviceCode, sjsq.getReceiptNumber());
            sj_handleResults.add(sj_handleResult);
        }
        sjsq.setHandleResultVoList(sj_handleResults);
    }

    private static SJ_Sjsq copyJSONReceiptDataToSjsq(JSONReceiptData sjsq_JSON_str) {
        SJ_Sjsq sjsq = JSON.parseObject(JSON.toJSONString(sjsq_JSON_str), SJ_Sjsq.class);
//        System.out.println("1:"+JSONObject.toJSONString(sjsq));
        return sjsq;
    }

    //关联不动产信息反转
    private static List<SJ_Bdc_Gl> copyJSONBdcToSJBdc(String JSON_glImmovableVoList, String dataFrom, SJ_Information sj_info, SJ_Sjsq sjsq) {
        List<SJ_Bdc_Gl> sj_bdcgls = new ArrayList<SJ_Bdc_Gl>();//证书中不动产关联集合对象
        if (StringUtils.isNotBlank(JSON_glImmovableVoList)) {
            List<JSONGLBdc> bdcgls = JSONArray.parseArray(JSON_glImmovableVoList, JSONGLBdc.class);//证书中不动产关联集合JSON对象
            for (JSONGLBdc bdcgl : bdcgls) {
                bdcgl.setInfoTableIdentification(dataFrom);
                SJ_Bdc_Fw_Info sj_bdc_fw_info = null;
                SJ_Bdc_Zd_Info sj_bdc_zd_info = null;
                if (StringUtils.isBlank(bdcgl.getImmovableType())) {
                    if (StringUtils.isBlank(sjsq.getImmovableType())) {
                        log.error(sj_info.getInfoId() + "号证书/证明/合同关联的不动产的类型为空，已终止保存并记录错误信息");
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_TYPE_NOT_CLEAR);
                    }
                    bdcgl.setImmovableType(sjsq.getImmovableType());
                }
                if (bdcgl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_FD)) {
                    if (StringUtils.isNotBlank(bdcgl.getZdInfo())) {
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_TYPE_NOT_MATCH_ON_FD);
                    }
                    String fwInfo = bdcgl.getFwInfo();
                    if (StringUtils.isNotBlank(fwInfo)) {
                        sj_bdc_fw_info = JSON.parseObject(fwInfo, SJ_Bdc_Fw_Info.class);
                    }
                    bdcgl.setFwInfo(null);
                } else if (bdcgl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_JD)) {
                    if (StringUtils.isNotBlank(bdcgl.getFwInfo())) {
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_TYPE_NOT_MATCH_ON_JD);
                    }
                    String zdInfo = bdcgl.getZdInfo();
                    if (StringUtils.isNotBlank(zdInfo)) {
                        sj_bdc_zd_info = JSON.parseObject(zdInfo, SJ_Bdc_Zd_Info.class);
                    }
                    bdcgl.setZdInfo(null);
                } else {
                    log.error(sj_info.getInfoId() + "号证书/证明/合同关联的不动产的类型未定义，传入为：" + bdcgl.getImmovableType() + "，已终止保存并记录错误信息");
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_TYPE_NOT_CLEAR);
                }
                SJ_Bdc_Gl sj_bdc_gl = JSON.parseObject(JSON.toJSONString(bdcgl), SJ_Bdc_Gl.class);
                sj_bdc_gl.setFwInfo(sj_bdc_fw_info);
                sj_bdc_gl.setZdInfo(sj_bdc_zd_info);
                sj_bdcgls.add(sj_bdc_gl);
            }
        }

        return sj_bdcgls;
    }

    private static List<SJ_Qlr_Gl> copyJSONQlrToSJQlr(String JSON_glQlrVoList, String dataFrom, String qlrlx) {
        List<SJ_Qlr_Gl> sj_qlrgls = new ArrayList<SJ_Qlr_Gl>();//证书中权利人关联集合对象
        if (StringUtils.isNotBlank(JSON_glQlrVoList)) {
            List<JSONGLQlr> qlrgls = JSONArray.parseArray(JSON_glQlrVoList, JSONGLQlr.class);
            for (JSONGLQlr qlrgl : qlrgls) {
                SJ_Qlr_Gl sj_qlr_gl = null;
                qlrgl.setInfoTableIdentification(dataFrom);
                String relatedPerson = qlrgl.getRelatedPerson();
                SJ_Qlr_Info sj_qlr_info = null;
                if (StringUtils.isNotBlank(relatedPerson)) {
                    sj_qlr_info = JSON.parseObject(relatedPerson, SJ_Qlr_Info.class);
                }
                qlrgl.setRelatedPerson(null);
                sj_qlr_gl = JSON.parseObject(JSON.toJSONString(qlrgl), SJ_Qlr_Gl.class);
                sj_qlr_gl.setObligeeType(qlrlx);
                sj_qlr_gl.setRelatedPerson(sj_qlr_info);
                sj_qlrgls.add(sj_qlr_gl);
            }
        }
        return sj_qlrgls;
    }

    private static void baseSetting(SJ_Information sj_info, String serviceCode, String receiptNumber) {
        //inserttime在这里将不做补充
//        if(sj_info.getInsertTime()==null) {
//            sj_info.setInsertTime(new Date());
//        }
        sj_info.setServiceCode(serviceCode);
        sj_info.setReceiptNumber(receiptNumber);
    }


}
