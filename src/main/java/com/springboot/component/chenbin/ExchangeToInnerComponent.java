package com.springboot.component.chenbin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.config.Msgagger;
import com.springboot.feign.ForImmovableFeign;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.util.DateUtils;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.ErrorDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ExchangeToInnerComponent {

    @Autowired
    private ForImmovableFeign immovableFeign;

    public List<SJ_Info_Bdcqlxgxx> getBdcQlInfoWithItsRights(ParametricData parametricData){
        List<SJ_Info_Bdcqlxgxx> serviceDataInfos = new ArrayList<SJ_Info_Bdcqlxgxx>();
        log.info("执行BDC查询前："+ new Date().getTime());
        List<JSONObject> jsonImmoVoList = immovableFeign.getBdcInfoByZH(parametricData.getBdczh(),parametricData.getQlrmc(),parametricData.getObligeeId());
        log.info("执行BDC查询后："+ new Date().getTime());
        log.info("不动产查询数据为："+JSONArray.toJSONString(jsonImmoVoList));
        if(jsonImmoVoList!=null && jsonImmoVoList.size()>0) {
            for (JSONObject jsonImmov:jsonImmoVoList) {
                SJ_Info_Bdcqlxgxx bdcqlxgxx = new SJ_Info_Bdcqlxgxx();
                String zslx = jsonImmov.getString("certificateType");
                if(StringUtils.isNotBlank(zslx) && !zslx.equals("null")) {
                    switch (zslx){
                        case "房屋不动产证":
                            bdcqlxgxx.setCertificateType("不动产权证");
                            break;
                        case "土地不动产证":
                            bdcqlxgxx.setCertificateType("不动产权证");
                            break;
                        case "房产证":
                            bdcqlxgxx.setCertificateType("房产证");
                            break;
                        case "土地证":
                            bdcqlxgxx.setCertificateType("土地证");
                            break;
                        default:
                            bdcqlxgxx.setCertificateType("不动产权证");
                            break;
                    }
                }else{
                    bdcqlxgxx.setCertificateType("不动产权证");
                }
                bdcqlxgxx.setRemarks(getNotNullData(jsonImmov.getString("remark")));                      //备注
                bdcqlxgxx.setAcceptanceNumber(getNotNullData(jsonImmov.getString("slbh")));//受理编号
                bdcqlxgxx.setOther(getNotNullData(jsonImmov.getString("other"))); //其他权利状况
                try {
                    bdcqlxgxx.setRegistrationDate(TimeUtil.getTimeFromString(getNotNullData(jsonImmov.getString("registerDate"))));//登记时间
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(StringUtils.isNotBlank(getNotNullData(jsonImmov.getString("realEstateId")))) {
                    bdcqlxgxx.setImmovableCertificateNo(getNotNullData(jsonImmov.getString("realEstateId")));//权证号
                }else if(StringUtils.isNotBlank(getNotNullData(jsonImmov.getString("vormerkungId")))){
                    bdcqlxgxx.setImmovableCertificateNo(getNotNullData(jsonImmov.getString("vormerkungId")));//预告号
                }
                JSONArray landRightVoList = jsonImmov.getJSONArray("landRightRelatedVoList");//土地权利信息
                if(landRightVoList!=null && landRightVoList.size()>0){
                    JSONObject landRight = landRightVoList.getJSONObject(0);

                    String tdzzrq = getNotNullData(landRight.getString("landRightEndDate"));
                    if(StringUtils.isNotBlank(tdzzrq) ){            //tdzzrq不为空时赋值
                        try {
                            bdcqlxgxx.setLandUseRightEndingDate(TimeUtil.getDateFromString(tdzzrq));
                        } catch (ParseException e) {
                            log.error(ErrorDealUtil.getErrorInfo(e));
                            e.printStackTrace();
                        }
                        bdcqlxgxx.setLandUseTimeLimit(tdzzrq);
                    }

                    bdcqlxgxx.setLandUseRightOwner(landRight.getString("landUser"));
                    bdcqlxgxx.setLandRightType(landRight.getString("landRightType"));
                    bdcqlxgxx.setLandRightNature(landRight.getString("landRightNature"));
                    bdcqlxgxx.setLandPurpose(landRight.getString("landUsage"));      //土地用途
                    String gymj = getNotNullData(landRight.getString("commonLandArea"));
                    String dymj = getNotNullData(landRight.getString("singleLandArea"));
                    String ftmj = getNotNullData(landRight.getString("sharedLandArea"));
                    String jzzjmj=getNotNullData(landRight.getString("architectureLandArea"));
                    bdcqlxgxx.setCommonLandArea(StringUtils.isNotBlank(gymj)?new BigDecimal(gymj):null);
                    bdcqlxgxx.setSingleLandArea(StringUtils.isNotBlank(dymj)?new BigDecimal(dymj):null);
                    bdcqlxgxx.setShareLandArea(StringUtils.isNotBlank(ftmj)?new BigDecimal(ftmj):null);
                    bdcqlxgxx.setBuildingParcelArea(StringUtils.isNotBlank(jzzjmj)?new BigDecimal(jzzjmj):null);
                }
                JSONArray houseRightVoList = jsonImmov.getJSONArray("houseRightRelatedVoList");//权利信息
                if(houseRightVoList!=null && houseRightVoList.size()>0){                            //权利信息赋值
                    JSONObject houseRight = houseRightVoList.getJSONObject(0);
                    bdcqlxgxx.setHouseRightType(getNotNullData(houseRight.getString("houseRightType")));
                    bdcqlxgxx.setHouseRightNature(getNotNullData(houseRight.getString("houseRightNature")));
                    String jzmj = getNotNullData(houseRight.getString("architectureArea"));
                    String tnmj = getNotNullData(houseRight.getString("innerArchitectureArea"));
                    String ftmj = getNotNullData(houseRight.getString("sharedArchitectureArea"));
                    bdcqlxgxx.setArchitecturalArea(StringUtils.isNotBlank(jzmj)?new BigDecimal(jzmj):null);
                    bdcqlxgxx.setHouseArchitecturalArea(StringUtils.isNotBlank(tnmj)?new BigDecimal(tnmj):null);
                    bdcqlxgxx.setApportionmentArchitecturalArea(StringUtils.isNotBlank(ftmj)?new BigDecimal(ftmj):null);
                    bdcqlxgxx.setHouseObtainingWays(getNotNullData(houseRight.getString("acquireWay")));
                    String qdjg = getNotNullData(houseRight.getString("acquirePrice"));
                    bdcqlxgxx.setHouseObtainingPrice(StringUtils.isNotBlank(qdjg)?new BigDecimal(qdjg):null);
                    bdcqlxgxx.setHousePlanningPurpose(getNotNullData(houseRight.getString("plannedUsage")));
                    String pgjg = getNotNullData(houseRight.getString("evaluationValue"));
                    bdcqlxgxx.setHouseValuationAmount(StringUtils.isNotBlank(pgjg)?new BigDecimal(pgjg):null);
                    bdcqlxgxx.setHouseType(getNotNullData(houseRight.getString("houseType")));
                    String ckmj = getNotNullData(houseRight.getString("garageArea"));
                    String ccsmj = getNotNullData(houseRight.getString("storageArea"));
                    String glmj = getNotNullData(houseRight.getString("atticArea"));
                    bdcqlxgxx.setGarageArea(StringUtils.isNotBlank(ckmj)?new BigDecimal(ckmj):null);//车库
                    bdcqlxgxx.setStoreroomArea(StringUtils.isNotBlank(ccsmj)?new BigDecimal(ccsmj):null);//储藏室
                    bdcqlxgxx.setAtticArea(StringUtils.isNotBlank(glmj)?new BigDecimal(glmj):null);//阁楼
                }

                JSONArray mortgageInfoVoList = jsonImmov.getJSONArray("mortgageInfoVoList");//存在的抵押限制信息
                JSONArray attachmentInfoVoList = jsonImmov.getJSONArray("attachmentInfoVoList");//存在的查封限制信息
                JSONArray dissentInfoVoList = jsonImmov.getJSONArray("dissentInfoVoList");//存在的异议限制信息

                List<SJ_Its_Right> itsRights = new ArrayList<SJ_Its_Right>();                   //他项权
                log.info("查询到的不动产现势抵押："+JSONArray.toJSONString(mortgageInfoVoList));
                log.info("查询到的不动产现势查封："+JSONArray.toJSONString(attachmentInfoVoList));
                log.info("查询到的不动产现势异议："+JSONArray.toJSONString(dissentInfoVoList));
                if(mortgageInfoVoList!=null && mortgageInfoVoList.size()>0){//抵押
                    for(int i=0;i<mortgageInfoVoList.size();i++){
                        JSONObject dyObj = mortgageInfoVoList.getJSONObject(i);
                        SJ_Its_Right itsRight = new SJ_Its_Right();
                        itsRight.setRegisterNumber(getNotNullData(dyObj.getString("dySLBH")));
                        itsRight.setItsRightStart(getNotNullData(dyObj.getString("mortgageStartDate")));
                        itsRight.setItsRightEnd(getNotNullData(dyObj.getString("mortgageEndDate")));
                        itsRight.setItsRightLimit(getNotNullData(dyObj.getString("mortgageEndDate")));
                        itsRight.setItsRightType("抵押");
                        JSONArray realEstateUnitIdList = dyObj.getJSONArray("realEstateUnitIdList");
                        String realEstateUnitIdTotal = "";
                        if(realEstateUnitIdList!=null && realEstateUnitIdList.size()>0){
                            if(realEstateUnitIdList.size()>1){
                                realEstateUnitIdTotal = realEstateUnitIdList.getString(0)+"等";
                            }else{
                                realEstateUnitIdTotal = realEstateUnitIdList.getString(0);
                            }
                        }
                        itsRight.setImmovableUnitNumber(realEstateUnitIdTotal);
                        itsRights.add(itsRight);
                    }
                }
                if(attachmentInfoVoList!=null && attachmentInfoVoList.size()>0){//查封
                    for(int i=0;i<attachmentInfoVoList.size();i++){
                        JSONObject cfObj = attachmentInfoVoList.getJSONObject(i);
                        SJ_Its_Right itsRight = new SJ_Its_Right();
                        itsRight.setRegisterNumber(getNotNullData(cfObj.getString("slbh")));
                        itsRight.setItsRightStart(getNotNullData(cfObj.getString("attachStartDate")));
                        itsRight.setItsRightEnd(getNotNullData(cfObj.getString("attachEndDate")));
                        itsRight.setItsRightLimit(getNotNullData(cfObj.getString("attachEndDate")));
                        itsRight.setItsRightType("查封");
                        itsRight.setImmovableUnitNumber(getNotNullData(cfObj.getString("realEstateUnitId")));
                        itsRights.add(itsRight);
                    }
                }
                if(dissentInfoVoList!=null && dissentInfoVoList.size()>0){//异议
                    for(int i=0;i<dissentInfoVoList.size();i++){
                        JSONObject dyObj = dissentInfoVoList.getJSONObject(i);
                        SJ_Its_Right itsRight = new SJ_Its_Right();
                        itsRight.setRegisterNumber(getNotNullData(dyObj.getString("slbh")));
                        itsRight.setItsRightType("异议");
                        itsRight.setImmovableUnitNumber(getNotNullData(dyObj.getString("realEstateUnitId")));
                        itsRights.add(itsRight);
                    }
                }
                log.info("该不动产限制信息为："+JSONArray.toJSONString(itsRights));
                bdcqlxgxx.setItsRightVoList(itsRights);

                JSONArray obligeeInfoVoList = jsonImmov.getJSONArray("obligeeInfoVoList");//权利人信息
                List<SJ_Qlr_Gl> qlrgls = new ArrayList<SJ_Qlr_Gl>();
                if(obligeeInfoVoList!=null && obligeeInfoVoList.size()>0){
                    for(int i=0;i<obligeeInfoVoList.size();i++){
                        JSONObject qlrObj = obligeeInfoVoList.getJSONObject(i);
                        SJ_Qlr_Gl qlrgl = new SJ_Qlr_Gl();
                        SJ_Qlr_Info qlrInfo = new SJ_Qlr_Info();

                        qlrInfo.setObligeeName(getNotNullData(qlrObj.getString("obligeeName")));
                        qlrInfo.setObligeeDocumentType(getZjlxString(getNotNullData(qlrObj.getString("obligeeIdType"))));
                        qlrInfo.setObligeeDocumentNumber(getNotNullData(qlrObj.getString("obligeeId")));

                        qlrgl.setRelatedPerson(qlrInfo);
                        qlrgl.setObligeeName(getNotNullData(qlrObj.getString("obligeeName")));
                        qlrgl.setObligeeType("权利人");
                        qlrgl.setObligeeOrder(i+1);
                        String gyfs = getNotNullData(qlrObj.getString("commonWay"));
                        if(gyfs!=null) {
                            qlrgl.setSharedMode(chooseGyfs(gyfs));
                        }
                        String gyfe = getNotNullData(qlrObj.getString("sharedShare"));
                        qlrgl.setSharedValue(StringUtils.isNotBlank(gyfe)?gyfe.replaceAll("%",""):null);
                        qlrgls.add(qlrgl);
                    }
                }
                bdcqlxgxx.setGlObligeeVoList(qlrgls);

                List<SJ_Bdc_Gl> bdcgls = new ArrayList<SJ_Bdc_Gl>();
                JSONArray realEstateUnitInfoVoList = jsonImmov.getJSONArray("realEstateUnitInfoVoList");//房地
                boolean isFd = false;
                if(realEstateUnitInfoVoList!=null && realEstateUnitInfoVoList.size()>0){//房屋
                    String zl = getNotNullData(realEstateUnitInfoVoList.getJSONObject(0).getString("sit"));
                    if(realEstateUnitInfoVoList.size()>1){
                        bdcqlxgxx.setImmovableSite(zl+"等");
                    }else{
                        bdcqlxgxx.setImmovableSite(zl);
                    }
                    for(int i=0;i<realEstateUnitInfoVoList.size();i++){
                        JSONObject estateObj = realEstateUnitInfoVoList.getJSONObject(i);
                        SJ_Bdc_Gl bdcGl = new SJ_Bdc_Gl();
                        SJ_Bdc_Fw_Info fw = new SJ_Bdc_Fw_Info();
                        bdcGl.setImmovableType("房地");
                        fw.setImmovableUnitNumber(getNotNullData(estateObj.getString("realEstateUnitId")));
                        fw.setImmovableUnicode(getNotNullData(estateObj.getString("householdId")));   //统一编码
                        fw.setHouseLocation(getNotNullData(estateObj.getString("sit")));      //坐落
                        fw.setSeatNumber(getNotNullData(estateObj.getString("buildingId")));         //幢编号
                        fw.setHouseholdMark(getNotNullData(estateObj.getString("accountId")));      //户号
                        fw.setRoomMark(getNotNullData(estateObj.getString("roomId")));           //房间号
                        fw.setUnitMark(getNotNullData(estateObj.getString("unitId")));           //单元号
                        fw.setLocationStorey(getNotNullData(estateObj.getString("floor")));      //所在层
                        fw.setTotalStorey(getNotNullData(estateObj.getString("totalFloor")));         //总层数
                        fw.setProjectName(getNotNullData(estateObj.getString("projectName")));        //项目名称
                        fw.setBuildingName(getNotNullData(estateObj.getString("architectureName")));       //建筑名称
                        String hjzmj = getNotNullData(estateObj.getString("architectureArea"));
                        String htnmj = getNotNullData(estateObj.getString("innerArchitectureArea"));
                        String hftmj = getNotNullData(estateObj.getString("sharedArchitectureArea"));
                        fw.setArchitecturalArea(StringUtils.isNotBlank(hjzmj)?new BigDecimal(hjzmj):null);  //建筑面积
                        fw.setHouseArchitecturalArea(StringUtils.isNotBlank(htnmj)?new BigDecimal(htnmj):null); //套内建筑面积
                        fw.setApportionmentArchitecturalArea(StringUtils.isNotBlank(hftmj)?new BigDecimal(hftmj):null);//分摊建筑面积
                        fw.setHouseType(getNotNullData(estateObj.getString("houseType")));              //房屋类型
                        fw.setHouseNature(getNotNullData(estateObj.getString("houseNature")));            //房屋性质
                        fw.setHouseStructure(getNotNullData(estateObj.getString("structure")));         //房屋结构
                        fw.setImmovablePlanningUse(getNotNullData(estateObj.getString("plannedUsage")));       //规划用途
                        bdcGl.setFwInfo(fw);
                        bdcgls.add(bdcGl);
                    }
                    isFd = true;
                }

                JSONArray landUnitInfoVoList = jsonImmov.getJSONArray("landUnitInfoVoList");//净地
                if(landUnitInfoVoList!=null && landUnitInfoVoList.size()>0){
                    String tdzzrq = getNotNullData(landUnitInfoVoList.getJSONObject(0).getString("landRightEndDate"));
                    if(
                            StringUtils.isNotBlank(tdzzrq) &&                                   //土地权属信息里存在日期
                            StringUtils.isNotBlank(bdcqlxgxx.getLandUseRightEndingDate())       //且不动产权属里终止日期没有被赋值
                    ){
                        try {
                            bdcqlxgxx.setLandUseRightEndingDate(TimeUtil.getDateFromString(tdzzrq));
                        } catch (ParseException e) {
                            log.error(ErrorDealUtil.getErrorInfo(e));
                            e.printStackTrace();
                        }
                        bdcqlxgxx.setLandUseTimeLimit(tdzzrq);
                    }
                    //房地业务不给土地信息
                    if(!isFd) {
                        String zl = getNotNullData(landUnitInfoVoList.getJSONObject(0).getString("landSit"));
                        if(landUnitInfoVoList.size()>1){
                            bdcqlxgxx.setImmovableSite(zl+"等");
                        }else{
                            bdcqlxgxx.setImmovableSite(zl);
                        }
                        for (int i = 0; i < landUnitInfoVoList.size(); i++) {
                            JSONObject estateObj = landUnitInfoVoList.getJSONObject(i);
                            SJ_Bdc_Gl bdcGl = new SJ_Bdc_Gl();
                            SJ_Bdc_Zd_Info zd = new SJ_Bdc_Zd_Info();
                            bdcGl.setImmovableType("宗地");
                            zd.setImmovableUnitNumber(getNotNullData(estateObj.getString("realEstateUnitId")));
                            zd.setParcelUnicode(getNotNullData(estateObj.getString("landUnicode")));//统一编码
                            zd.setParcelType(getNotNullData(estateObj.getString("landType")));     //宗地类型
                            zd.setCadastralNumber(getNotNullData(estateObj.getString("cadastralNumber")));//地籍号
                            zd.setParcelLocation(getNotNullData(estateObj.getString("landSit")));//坐落
                            zd.setLandUse(getNotNullData(estateObj.getString("landUsage")));        //用途
                            String dytdmj = getNotNullData(estateObj.getString("singleLandArea"));
                            String gxtdmj = getNotNullData(estateObj.getString("sharedLandArea"));
                            zd.setPrivateLandArea(StringUtils.isNotBlank(dytdmj) ? new BigDecimal(dytdmj) : null);//独用土地面积
                            zd.setApportionmentLandArea(StringUtils.isNotBlank(gxtdmj) ? new BigDecimal(gxtdmj) : null);//分摊土地面积
                            bdcGl.setZdInfo(zd);
                            bdcgls.add(bdcGl);
                        }
                    }
                }
                String sit = getNotNullData(jsonImmov.getString("sit"));
                if(StringUtils.isNotBlank(sit)){
                    bdcqlxgxx.setImmovableSite(sit);
                }
                bdcqlxgxx.setGlImmovableVoList(bdcgls);
                serviceDataInfos.add(bdcqlxgxx);
            }
        }
        log.info("服务数据生成结束，为："+JSONArray.toJSONString(serviceDataInfos));
        return serviceDataInfos;
    }

    private String getNotNullData(String param){
        return (StringUtils.isBlank(param) || param.equals("null"))?null:param;
    }

    private String getZjlxString(String zjlxCode){
        String zjlxString = "其它";
        if(StringUtils.isNotBlank(zjlxCode)) {
            switch (zjlxCode) {
                case "1":
                    zjlxString = "身份证";
                    break;
                case "7":
                    zjlxString = "组织机构代码";
                    break;
                case "8":
                    zjlxString = "统一社会信用代码";
                    break;
                default:
                    zjlxString = zjlxCode;
                    break;
            }
        }
        return zjlxString;
    }

    private String chooseGyfs(String gyfsCode){
        String gyfsStr = "其它共有";
        switch (gyfsCode){
            case "0":
                gyfsStr = "单独所有";
                break;
            case "1":
                gyfsStr = "共同共有";
                break;
            case "2":
                gyfsStr = "按份共有";
                break;
            default:
                gyfsStr = gyfsCode;
                break;
        }
        return gyfsStr;
    }
}
