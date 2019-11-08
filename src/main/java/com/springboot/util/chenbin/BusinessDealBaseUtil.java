package com.springboot.util.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.chenbin.personnel.pub_use.*;
import com.springboot.entity.chenbin.personnel.tax.TaxParamBody;
import com.springboot.entity.chenbin.personnel.tra.TraParamBody;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.registration.*;
import com.springboot.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    数据处理基本UTIL工具类
 */
public class BusinessDealBaseUtil {
    //处理基本信息
    public static RegistrationBureau dealBaseInfo(SJ_Sjsq sjsq, String pid, boolean isSubmit, String bizType, String dealPerson, String areaNo) {
        RegistrationBureau registrationBureau = new RegistrationBureau();
        registrationBureau.setPid(pid);//测试数据
        registrationBureau.setSubmitFlow(isSubmit);
        registrationBureau.setBizType(bizType);//#（抵押注销(个人)）
        registrationBureau.setOperatorName(dealPerson);//测试后续需要设置
        registrationBureau.setContactsAdress(StringUtils.isBlank(sjsq.getImmovableSite()) ? "测试地址" : sjsq.getImmovableSite());//测试数据
        registrationBureau.setContacts(sjsq.getNotifiedPersonName());
        registrationBureau.setContactsPhone(sjsq.getNotifiedPersonTelephone());
        registrationBureau.setBusinessAreas(StringUtils.isNotBlank(sjsq.getDistrictCode()) ? sjsq.getDistrictCode() : areaNo);
        return registrationBureau;
    }

    //处理MortgageBizInfo，通过两个合同
    public static MortgageBizInfo getMortgageBizInfoByContract(Sj_Info_Jyhtxx jyht, Sj_Info_Dyhtxx dyht, String idType) {
        MortgageBizInfo mortgageBizInfo = new MortgageBizInfo();
        mortgageBizInfo.setMortgageApplyDate(StringUtils.isNotBlank(dyht.getApplyTime()) ? dyht.getApplyTime() : TimeUtil.getTimeString(new Date()));
        mortgageBizInfo.setMortgageWay(dyht.getMortgageMode());
        mortgageBizInfo.setCreditAmount(dyht.getCreditAmount() == null ? null : dyht.getCreditAmount().toString());
        mortgageBizInfo.setEvaluationValue(dyht.getValuationValue() == null ? null : dyht.getValuationValue().toString());
        mortgageBizInfo.setMortgageTerm(dyht.getMortgagePeriod());
        mortgageBizInfo.setMortgageStartDate(dyht.getMortgageStartingDate());
        mortgageBizInfo.setMortgageEndDate(dyht.getMortgageEndingDate());
        mortgageBizInfo.setMortgageReason(StringUtils.isNotBlank(dyht.getMortgageReason()) ? dyht.getMortgageReason() : "购买商品房");
        mortgageBizInfo.setAbsoluteFact(dyht.getMaximumClaimConfirm());
        mortgageBizInfo.setHighestClaimAmount(dyht.getMaximumClaimAmount() == null ? null : dyht.getMaximumClaimAmount().toString());
        mortgageBizInfo.setHtbh(jyht.getContractRecordNumber());

        List<SJ_Qlr_Gl> dyqrs = dyht.getGlMortgageHolderVoList();
        List<SJ_Qlr_Gl> dyrs = dyht.getGlMortgagorVoList();
        List<DyqrGlMortgator> mortgageeInfoVoList = getMortgageeInfoVoList(dyqrs, idType);
        List<DyrGlMortgator> mortgagorInfoVoList = getMortgagorInfoVoList(dyrs, idType);
        List<RealEstateInfo> realEstateInfoVoList = getRealEstateInfoVoListByJyht(jyht, idType);
        mortgageBizInfo.setMortgageeInfoVoList(mortgageeInfoVoList);
        mortgageBizInfo.setMortgagorInfoVoList(mortgagorInfoVoList);
        mortgageBizInfo.setRealEstateInfoVoList(realEstateInfoVoList);
        return mortgageBizInfo;
    }

    //抵押权人
    public static List<DyqrGlMortgator> getMortgageeInfoVoList(List<SJ_Qlr_Gl> dyqrs, String idType) {
        List<DyqrGlMortgator> mortgageeInfoVoList = new ArrayList<DyqrGlMortgator>();
        for (SJ_Qlr_Gl dyqr : dyqrs) {
            SJ_Qlr_Info person = dyqr.getRelatedPerson();
            DyqrGlMortgator mortgageeInfo = new DyqrGlMortgator();
            mortgageeInfo.setMortgageeName(person.getObligeeName());
            mortgageeInfo.setMortgageeIdType(getIdTypeNumber(person.getObligeeDocumentType(), idType));
            mortgageeInfo.setMortgageeId(person.getObligeeDocumentNumber());
            mortgageeInfoVoList.add(mortgageeInfo);
        }
        return mortgageeInfoVoList;
    }

    //抵押人
    public static List<DyrGlMortgator> getMortgagorInfoVoList(List<SJ_Qlr_Gl> dyrs, String idType) {
        List<DyrGlMortgator> mortgagorInfoVoList = new ArrayList<DyrGlMortgator>();
        for (SJ_Qlr_Gl dyr : dyrs) {
            SJ_Qlr_Info person = dyr.getRelatedPerson();
            DyrGlMortgator mortgagorInfo = new DyrGlMortgator();
            mortgagorInfo.setMortgagorId(person.getObligeeDocumentNumber());
            mortgagorInfo.setMortgagorIdType(getIdTypeNumber(person.getObligeeDocumentType(), idType));
            mortgagorInfo.setMortgagorName(person.getObligeeName());
            mortgagorInfoVoList.add(mortgagorInfo);
        }
        return mortgagorInfoVoList;
    }

    //抵押的不动产信息
    public static List<RealEstateInfo> getRealEstateInfoVoListByJyht(Sj_Info_Jyhtxx jyht, String idType) {
        List<RealEstateInfo> realEstateInfoVoList = new ArrayList<RealEstateInfo>();
        RealEstateInfo realEstateInfo = new RealEstateInfo();
        List<SJ_Qlr_Gl> buyers = jyht.getGlHouseBuyerVoList();
        List<SJ_Qlr_Gl> sellers = jyht.getGlHouseSellerVoList();
        List<SJ_Bdc_Gl> bdc_gls = jyht.getGlImmovableVoList();
        realEstateInfo.setObligeeInfoVoList(getObligeeInfoVoList(buyers, idType));
        realEstateInfo.setSalerInfoVoList(getSalerInfoVoList(sellers, idType));
        realEstateInfo.setRealEstateUnitInfoVoList(getRealEstateUnitInfoVoList(bdc_gls));
        realEstateInfoVoList.add(realEstateInfo);
        return realEstateInfoVoList;
    }

    //权利人信息
    public static List<QlrGlMortgator> getObligeeInfoVoList(List<SJ_Qlr_Gl> buyers, String idType) {
        List<QlrGlMortgator> obligeeInfoVoList = new ArrayList<QlrGlMortgator>();
        for (SJ_Qlr_Gl buyer : buyers) {
            SJ_Qlr_Info person = buyer.getRelatedPerson();
            QlrGlMortgator obligeeInfo = new QlrGlMortgator();
            obligeeInfo.setObligeeName(person.getObligeeName());
            obligeeInfo.setObligeeIdType(getIdTypeNumber(person.getObligeeDocumentType(), idType));
            obligeeInfo.setObligeeId(person.getObligeeDocumentNumber());
            obligeeInfo.setCommonWay(buyer.getSharedMode());
            obligeeInfo.setSharedSharel(buyer.getSharedValue() == null ? null : buyer.getSharedValue().toString());
            obligeeInfoVoList.add(obligeeInfo);
        }
        return obligeeInfoVoList;
    }

    //义务人信息
    public static List<SalerInfo> getSalerInfoVoList(List<SJ_Qlr_Gl> sellers, String idType) {
        List<SalerInfo> salerInfoVoList = new ArrayList<SalerInfo>();
        for (SJ_Qlr_Gl seller : sellers) {
            SJ_Qlr_Info person = seller.getRelatedPerson();
            System.out.println(JSONObject.toJSONString(person));
            SalerInfo salerInfo = new SalerInfo();
            salerInfo.setSalerName(person.getObligeeName());
            salerInfo.setSalerIdType(getIdTypeNumber(person.getObligeeDocumentType(), idType));
            salerInfo.setSalerId(person.getObligeeDocumentNumber());
            salerInfoVoList.add(salerInfo);
        }
        return salerInfoVoList;
    }

    //不动产单元
    public static List<RealEstateUnitInfo> getRealEstateUnitInfoVoList(List<SJ_Bdc_Gl> bdc_gls) {
        List<RealEstateUnitInfo> realEstateUnitInfoVoList = new ArrayList<RealEstateUnitInfo>();
        for (SJ_Bdc_Gl bdc_gl : bdc_gls) {
            RealEstateUnitInfo realEstateUnitInfo = new RealEstateUnitInfo();
            switch (bdc_gl.getImmovableType()) {
                case "房地":
                    SJ_Bdc_Fw_Info fw = bdc_gl.getFwInfo();
                    realEstateUnitInfo.setHouseholdId(fw.getHouseholdId());
                    realEstateUnitInfo.setRealEstateUnitId(fw.getImmovableUnitNumber());
                    realEstateUnitInfo.setSit(fw.getHouseLocation());
                    break;
                case "宗地":
                    SJ_Bdc_Zd_Info zd = bdc_gl.getZdInfo();
                    realEstateUnitInfo.setHouseholdId(zd.getParcelUnicode());
                    realEstateUnitInfo.setSit(zd.getParcelLocation());
                    realEstateUnitInfo.setRealEstateUnitId(zd.getImmovableUnitNumber());
                    break;
                default:
                    throw new ZtgeoBizException("不动产类型未选择或类型不合法");
            }
            realEstateUnitInfoVoList.add(realEstateUnitInfo);
        }
        return realEstateUnitInfoVoList;
    }

    public static String getIdTypeNumber(String idName, String idTypess) {
        if (StringUtils.isBlank(idName)) {
            return "1";//或者抛异常
        }
        String idd = "99";
        String[] idTypes = idTypess.split("\\$");
        System.err.println(idTypes);
        for (int i = 0; i < idTypes.length; i++) {
            String idType = idTypes[i];
            String[] id_ = idType.split(",");
            String idTypeName = id_[0];
            if (idType.contains(idName)) {
                System.out.println("为：" + id_[1]);
                idd = id_[1];
            }
        }
        return idd;
    }

    public static int getFileXh(SJ_Fjfile file, List<SJ_Fjfile> fileVoList) {
        int i = 0;
        for (SJ_Fjfile fjfile : fileVoList) {
            if (fjfile.getLogicPath().equals(file.getLogicPath()))
                i++;
            if (fjfile.getFileId().equals(file.getFileId())) {
                return i;
            }
        }
        return i;
    }

    public static TaxParamBody dealParamForTax(SJ_Sjsq sjsq){
        TaxParamBody taxParam = new TaxParamBody();
        List<SJ_Info_Bdcqlxgxx> bdcqls =  sjsq.getImmovableRightInfoVoList();
        Sj_Info_Jyhtxx jyht = sjsq.getTransactionContractInfo();
        String CFZT = "0";
        String YYZT = "0";
        String DYZT = "0";
        List<QSXX> QSXX = new ArrayList<QSXX>();
        for(SJ_Info_Bdcqlxgxx bdcql:bdcqls){
            List<SJ_Its_Right> itsRightVoList = bdcql.getItsRightVoList();
            if(itsRightVoList!=null){
                for(SJ_Its_Right itsRight:itsRightVoList){
                    switch (itsRight.getItsRightType()){
                        case "抵押":
                            DYZT = "1";
                            break;
                        case "查封":
                            CFZT = "1";
                            break;
                        case "异议":
                            YYZT = "1";
                            break;
                    }
                }
            }

            //权属信息
            QSXX qsxx = new QSXX();
            qsxx.setBDCZH(bdcql.getImmovableCertificateNo());
            qsxx.setJZMJ(bdcql.getArchitecturalArea());
            qsxx.setTNMJ(bdcql.getHouseArchitecturalArea());
            qsxx.setYT(bdcql.getHousePlanningPurpose());
            qsxx.setZL(bdcql.getImmovableSite());
            qsxx.setTDSYQR(bdcql.getLandUseRightOwner());
            qsxx.setTDHQFS(bdcql.getLandObtainWay());
            List<FWXX> FWXX = new ArrayList<FWXX>();
            List<SJ_Bdc_Gl> bdcgls = bdcql.getGlImmovableVoList();
            if(bdcgls!=null) {
                for (SJ_Bdc_Gl bdcgl : bdcgls) {
                    if(bdcgl.getImmovableType().equals("房地")) {
                        SJ_Bdc_Fw_Info fw = bdcgl.getFwInfo();
                        FWXX fwxx = new FWXX();
                        fwxx.setBDCDYH(fw.getImmovableUnitNumber());
                        fwxx.setFTMJ(fw.getApportionmentArchitecturalArea());
                        fwxx.setFWDY(fw.getUnitMark());
                        fwxx.setFWFH(fw.getRoomMark());
                        fwxx.setFWJG(fw.getHouseStructure());
                        fwxx.setFWLX(fw.getHouseType());
                        fwxx.setFWXZ(fw.getHouseNature());
                        fwxx.setFWZL(fw.getHouseLocation());
                        fwxx.setJZMJ(fw.getArchitecturalArea());
                        fwxx.setTNMJ(fw.getHouseArchitecturalArea());
                        fwxx.setXMMC(fw.getProjectName());
                        fwxx.setYFCBH(fw.getOldHouseCode());
                        fwxx.setYT(fw.getImmovablePlanningUse());
                        fwxx.setZL(fw.getHouseLocation());
                        fwxx.setZCS(fw.getTotalStorey());
                        fwxx.setSZC(fw.getLocationStorey());
                        FWXX.add(fwxx);
                    }
                }
            }
            qsxx.setFWXX(FWXX);
            QSXX.add(qsxx);
        }

        List<SJ_Qlr_Gl> buyergls = jyht.getGlHouseBuyerVoList();
        List<SJ_Qlr_Gl> sellergls = jyht.getGlHouseSellerVoList();
        List<JYQLRXX> JYQLRXX = new ArrayList<JYQLRXX>();
        for(SJ_Qlr_Gl buyergl:buyergls){
            JYQLRXX jyqlrxx = getJyqlr(buyergl);
            jyqlrxx.setQLRBS("1");
            JYQLRXX.add(jyqlrxx);
        }
        for(SJ_Qlr_Gl sellergl:sellergls){
            JYQLRXX jyqlrxx = getJyqlr(sellergl);
            jyqlrxx.setQLRBS("0");
            JYQLRXX.add(jyqlrxx);
        }

        List<SJ_Qlr_Gl> buyerAgentgls = jyht.getGlAgentVoList();
        List<SJ_Qlr_Gl> sellerAgentgls = jyht.getGlAgentSellerVoList();
        List<JYQLRXX> JYDLRXX = new ArrayList<JYQLRXX>();
        if(buyerAgentgls!=null){//权利代理人整理
            for(SJ_Qlr_Gl buyerAgentgl:buyerAgentgls) {
                JYQLRXX ydlrxx = getJyqlr(buyerAgentgl);
                ydlrxx.setQLRBS("1");
                JYDLRXX.add(ydlrxx);
            }
        }
        if(sellerAgentgls!=null){//义务代理人整理
            for(SJ_Qlr_Gl sellerAgentgl:sellerAgentgls) {
                JYQLRXX jdlrxx = getJyqlr(sellerAgentgl);
                jdlrxx.setQLRBS("0");
                JYQLRXX.add(jdlrxx);
            }
        }

        DecimalFormat df = new DecimalFormat("#.00");
        SJ_Jyht_Detail htDetail = jyht.getHtDetail();
        HTXX HTXX = new HTXX();
        HTXX.setHTJE(jyht.getContractAmount()!=null?df.format(jyht.getContractAmount()):null);//合同金额
        HTXX.setFSSS(htDetail.getHouseProperties());//附属设施
        HTXX.setSFCZ(StringUtils.isNotBlank(htDetail.getIsHire())?Integer.parseInt(htDetail.getIsHire()):null);//是否出租
        HTXX.setCZSM(htDetail.getHireInstructions());//出租说明
        HTXX.setSFBHFS(StringUtils.isNotBlank(htDetail.getDoesIncludeHouseProperties())?Integer.parseInt(htDetail.getDoesIncludeHouseProperties()):null);//是否包含附属设施 1 包含，0 不包含
        HTXX.setSFTG(StringUtils.isNotBlank(jyht.getFundTrusteeship())?Integer.parseInt(jyht.getFundTrusteeship()):null);//是否资金托管 1 是，0 否
        HTXX.setTGKHH(htDetail.getFundOpenBank());//资金托管开户行
        HTXX.setTGZH(htDetail.getFundAccount());//托管账户
        HTXX.setTGMFZFNR(htDetail.getFundBuyerPaysContent());//买方支付内容
        HTXX.setZFFS(StringUtils.isNotBlank(jyht.getPaymentMethod())?Integer.parseInt(jyht.getPaymentMethod()):null);//支付方式 1 一次性付款，2 分期付款，3 贷款付款，4 其它付款方式
        HTXX.setFKRQ(htDetail.getFullPaymentDate());//付款日期（付款方式1）
        HTXX.setFQFKRQ1(htDetail.getStagePaymentDate1());//分期付款日期1（付款方式2）
        HTXX.setFQFKJE1(htDetail.getStagePaymentAmount1()!=null?df.format(htDetail.getStagePaymentAmount1()):null);//分期付款金额1（付款方式2）//保留两位小数
        HTXX.setFQFKRQ2(htDetail.getStagePaymentDate2());//分期付款日期2（付款方式2）
        HTXX.setFQFKJE2(htDetail.getStagePaymentAmount2()!=null?df.format(htDetail.getStagePaymentAmount2()):null);//分期付款金额2（付款方式2）
        HTXX.setFQFKRQ3(htDetail.getStagePaymentDate3());//分期付款日期3（付款方式2）
        HTXX.setFQFKJE3(htDetail.getStagePaymentAmount3()!=null?df.format(htDetail.getStagePaymentAmount3()):null);//分期付款金额3（付款方式2）
        HTXX.setDKFS(StringUtils.isNotBlank(htDetail.getLoanMode())?Integer.parseInt(htDetail.getLoanMode()):null); //贷款方式，1 银行按揭，2 公积金贷款（付款方式3）
        HTXX.setSFKRQ(htDetail.getFirstPaymentDate());//首付款日期（付款方式3）
        HTXX.setSFKJE(htDetail.getFirstPaymentAmount()!=null?df.format(htDetail.getFirstPaymentAmount()):null); //首付款金额（付款方式3）//保留两位小数
        HTXX.setDKSQRQ(htDetail.getLoanApplyDate()); //贷款申请日期（付款方式3）
        HTXX.setQTFKNR(htDetail.getPaymentContents()); //其它付款内容（付款方式4）
        HTXX.setQTFKMFZF(htDetail.getBuyerPays()); //其它付款-买方支付（付款方式4）
        if(StringUtils.isNotBlank(jyht.getTaxBurdenParty())){
            if(jyht.getTaxBurdenParty().equals("1")){
                HTXX.setJFCDSF(1); //买方承担税费情况，1 独自承担（此时YFCDSF为空），2 承担各自部分
            }else if(jyht.getTaxBurdenParty().equals("2")){
                HTXX.setYFCDSF(1); //卖方承担税费情况，1 独自承担（此时JFCDSF为空），2 承担各自部分
            }else if(jyht.getTaxBurdenParty().equals("3")){
                HTXX.setJFCDSF(2); //买方承担税费情况，1 独自承担（此时YFCDSF为空），2 承担各自部分
                HTXX.setYFCDSF(2); //卖方承担税费情况，1 独自承担（此时JFCDSF为空），2 承担各自部分
            }else{
                throw new ZtgeoBizException("不支持的税费承担方式");
            }
        }
        HTXX.setJFTS(Integer.parseInt(jyht.getDeliveryDays())); //交付天数
        HTXX.setJFRQ(jyht.getDeliveryDate());  //交付日期

        YCSLXX YCSLXX = new YCSLXX();
        YCSLXX.setSLBH(sjsq.getReceiptNumber());
        YCSLXX.setSQSJ(sjsq.getReceiptTime());

        taxParam.setCFZT(CFZT);
        taxParam.setDYZT(DYZT);
        taxParam.setYYZT(YYZT);
        taxParam.setHTXX(HTXX);
        taxParam.setYCSLXX(YCSLXX);
        taxParam.setQSXX(QSXX);
        taxParam.setJYQLRXX(JYQLRXX);
        taxParam.setJYDLRXX(JYDLRXX);
        return taxParam;
    }
    public static TraParamBody dealParamForTra(SJ_Sjsq sjsq){
        TaxParamBody taxParam = dealParamForTax(sjsq);
        TraParamBody traParam = new TraParamBody();
        traParam.setCFZT(taxParam.getCFZT());
        traParam.setDYZT(taxParam.getDYZT());
        traParam.setHTXX(taxParam.getHTXX());
        traParam.setJYQLRXX(taxParam.getJYQLRXX());
        traParam.setJYDLRXX(taxParam.getJYDLRXX());
        traParam.setQSXX(taxParam.getQSXX());
        traParam.setYCSLXX(taxParam.getYCSLXX());
        traParam.setYYZT(taxParam.getYYZT());
        return traParam;
    }

    public static JYQLRXX getJyqlr(SJ_Qlr_Gl qlrgl){
        JYQLRXX jyqlrxx = new JYQLRXX();
        SJ_Qlr_Info qlrInfo = qlrgl.getRelatedPerson();
        jyqlrxx.setQLRMC(qlrInfo.getObligeeName());
        jyqlrxx.setZJLX(qlrInfo.getObligeeDocumentType());
        jyqlrxx.setZJHM(qlrInfo.getObligeeDocumentNumber());
        jyqlrxx.setDH(qlrInfo.getDh());
        jyqlrxx.setGYFS(qlrgl.getSharedMode());
        jyqlrxx.setGYFE(qlrgl.getSharedValue()!=null?Integer.toString(qlrgl.getSharedValue()):null);
        return jyqlrxx;
    }
}
