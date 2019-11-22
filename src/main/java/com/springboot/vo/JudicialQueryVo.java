package com.springboot.vo;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.exception.base.BusinessException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Data
public class JudicialQueryVo {
    private String DJJG;//登记机构
    private String CXBH;//查询编号
    private String CXRQ;//查询日期
    private String CXJG;//查询机关
    private String CXRY;//查询人员
    private String GZZH;//工作证号
    private String ZXGWZH;//执行公务证号
    private List<BdcDJInfo> bdcDjInfoList;//不动产登记信息集合
    private List<BdcDyInfo> bdcDyInfoList;//不动产抵押信息集合
    private List<BdcCfInfo> bdcCfInfoList;//不动产查封信息集合
    private String remark;//备注
    private String printPerson;//打印人员
    private String printDate;//打印日期

    //不动产登记信息
    @Data
    class BdcDJInfo{
        private String BDCDYH;//不动产单元号
        private String BDCZH;//不动产证号;证明号(抵押证明,预告证明)
        private String ZL;//坐落
        private List<QLR> QLRList; //权利人列表
        private String QLLX;//土地权利类型
        private String QLXZ;//土地权利性质
        private String TDYT;//土地用途
        private String TDMJ;//土地使用面积/分摊土地面积
        private String JZMJ;//建筑面积
        private String GHYT;//规划用途
    }
    //权利人
    @Data
    class QLR{
        private String QLRMC;//权利人名称
        private String GYFS;//共有状况
        private String ZJLB;//权利人证件类型
        private String ZJHM;//权利人证件号码
    }

    //不动产抵押信息
    @Data
    class BdcDyInfo{
        private String DYZMH;//抵押证明号
        private List<QLR> DYRList;//抵押人列表
        private String FWDYMJ;//房屋抵押面积
        private String TDDYMJ;//土地抵押面积
        private String DYFS;//抵押方式
        private String BDBZQSE;//被担保债券数额
        private String DYSW;//抵押顺位
        private String ZWLVQX;//债务履行期限
        private List<QLR> DSFList;//第三方借款人列表
    }

    //不动产查封信息
    @Data
    class BdcCfInfo{
        private String CFJG;//查封机关
        private String CFWH;//查封文号
        private String YGR;//申请执行人
        private String QLR;//被执行人
        private String QX;//查封期限
        private String CFJSSJ;//查封起止日期
        private String CFYY;//查封原因
    }

    /**
     * 填充数据
     * @param result
     * @param judicialQueryVo
     * @param flag
     * @return
     */
    public JudicialQueryVo genJudicialQuery(String result,JudicialQueryVo judicialQueryVo,String flag){
        if (StringUtils.isBlank(result)||"[]".equals(result)){
            throw new BusinessException("查询数据有误,请重试");
        }
        //jsonArray为模糊查询的不动产证号集合
        JSONArray jsonArray = JSONArray.parseArray(result);

        //此处仅取第一个数据用于展示
        JSONObject data = (JSONObject)jsonArray.get(0);

        //不动产登记信息列表
        String realEstateUnitInfoVoList = getString(data.get("realEstateUnitInfoVoList"));
        if (StringUtils.isNotBlank(realEstateUnitInfoVoList)){
            JSONArray realEstateUnitInfoJson = JSONArray.parseArray(realEstateUnitInfoVoList);
            List<BdcDJInfo> bdcDJInfoList = new ArrayList<>();
            for (int i = 0; i < realEstateUnitInfoJson.size(); i++) {
                JSONObject bdcDjInfo = (JSONObject) realEstateUnitInfoJson.get(i);
                BdcDJInfo bdcDJInfo = new BdcDJInfo();
                //不动产单元号
                bdcDJInfo.setBDCDYH(getString(bdcDjInfo.get("realEstateUnitId")));
                //不动产证号由最外层取值
                if ("3".equals(flag)){
                    bdcDJInfo.setBDCZH(getString(data.get("vormerkungId")));
                }else {
                    bdcDJInfo.setBDCZH(getString(data.get("realEstateId")));
                }
                //不动产坐落
                bdcDJInfo.setZL(getString(bdcDjInfo.get("sit")));
                //权利人信息列表
                String obligeeInfoVoList = "";
                //根据类型填充权利人消息
                switch (flag){
                    //不动产证号
                    case "1":obligeeInfoVoList = getString(data.get("obligeeInfoVoList"));break;
                    //抵押证明号
                    case "2":
                        String propertyRightInfoVoList = getString(data.get("propertyRightInfoVoList"));
                        if (StringUtils.isNotBlank(propertyRightInfoVoList)){
                            JSONObject propertyRightInfo = (JSONObject)JSONArray.parseArray(propertyRightInfoVoList).get(0);
                            obligeeInfoVoList = getString(propertyRightInfo.get("obligeeInfoVoList"));
                        }
                        break;
                    case "3":obligeeInfoVoList = getString(data.get("obligeeInfoVoList"));break;
                }
                //填充权利人
                if (StringUtils.isNotBlank(obligeeInfoVoList)) {
                    JSONArray obligeeInfoVoListJson = JSONArray.parseArray(obligeeInfoVoList);
                    List<QLR> qlrList = new ArrayList<>();
                    for (int j = 0; j < obligeeInfoVoListJson.size(); j++) {
                        JSONObject QLRJson = (JSONObject) obligeeInfoVoListJson.get(j);
                        QLR qlr = new QLR();
                        qlr.setQLRMC(getString(QLRJson.get("obligeeName")));
                        qlr.setGYFS(getString(QLRJson.get("commonWay")));
                        qlr.setZJLB(getString(QLRJson.get("obligeeIdType")));
                        qlr.setZJHM(getString(QLRJson.get("obligeeId")));
                        qlrList.add(qlr);
                    }
                    bdcDJInfo.setQLRList(qlrList);
                }
                //土地相关列表,此处仅取第一条
                String landRightRelatedVoList = "";
                switch (flag){
                    //不动产证号
                    case "1":landRightRelatedVoList = getString(data.get("landRightRelatedVoList"));break;
                    //抵押证明号
                    case "2":
                        String propertyRightInfoVoList = getString(data.get("propertyRightInfoVoList"));
                        if (StringUtils.isNotBlank(propertyRightInfoVoList)){
                            JSONObject propertyRightInfo = (JSONObject)JSONArray.parseArray(propertyRightInfoVoList).get(0);
                            landRightRelatedVoList = getString(propertyRightInfo.get("landRightRelatedVoList"));
                        }
                        break;
                    case "3":landRightRelatedVoList = getString(data.get("landRightRelatedVoList"));break;
                }
                //根据类型填充土地相关
                if (StringUtils.isNotBlank(landRightRelatedVoList)) {
                    JSONArray landRightRelatedVoListJson = JSONArray.parseArray(landRightRelatedVoList);
                    JSONObject landRight = (JSONObject)landRightRelatedVoListJson.get(0);
                    //土地权利类型
                    bdcDJInfo.setQLLX(getString(landRight.get("landRightType")));
                    //土地权利性质
                    bdcDJInfo.setQLXZ(getString(landRight.get("landRightNature")));
                    //土地用途
                    bdcDJInfo.setTDYT(getString(landRight.get("landUsage")));
                    //土地使用权面积/分摊土地面积
                    StringBuffer landArea = new StringBuffer();
                    String commonLandArea = getString(landRight.get("commonLandArea"));
                    String sharedLandArea = getString(landRight.get("sharedLandArea"));
                    if (StringUtils.isBlank(commonLandArea)){
                        //为空留出默认空格
                        landArea.append("    /");
                    }else {
                        //不为空,格式化
                        commonLandArea = formatNumber(commonLandArea);
                        landArea.append(commonLandArea);
                        landArea.append("/");
                    }
                    if (StringUtils.isNotBlank(sharedLandArea)){
                        sharedLandArea = formatNumber(sharedLandArea);
                        //为空留出默认空格
                        landArea.append(sharedLandArea);
                    }
                    bdcDJInfo.setTDMJ(landArea.toString());
                }

                //权利房屋相关列表
                String houseRightRelatedVoList = "";
                switch (flag){
                    //不动产证号
                    case "1":houseRightRelatedVoList = getString(data.get("houseRightRelatedVoList"));break;
                    //抵押证明号
                    case "2":
                        String propertyRightInfoVoList = getString(data.get("propertyRightInfoVoList"));
                        if (StringUtils.isNotBlank(propertyRightInfoVoList)){
                            JSONObject propertyRightInfo = (JSONObject)JSONArray.parseArray(propertyRightInfoVoList).get(0);
                            houseRightRelatedVoList = getString(propertyRightInfo.get("houseRightRelatedVoList"));
                        }
                        break;
                    case "3":houseRightRelatedVoList = getString(data.get("houseRightRelatedVoList"));break;
                }
                //填充权利房屋相关列表
                if (StringUtils.isNotBlank(houseRightRelatedVoList)) {
                    JSONArray houseRightRelatedVoListJson = JSONArray.parseArray(houseRightRelatedVoList);
                    JSONObject houseRight = (JSONObject)houseRightRelatedVoListJson.get(0);
                    //建筑面积
                    bdcDJInfo.setJZMJ(formatNumber(getString(houseRight.get("architectureArea"))));
                    bdcDJInfo.setGHYT(getString(houseRight.get("plannedUsage")));
                }

                //添加进不动产单元集合
                bdcDJInfoList.add(bdcDJInfo);
            }
            judicialQueryVo.setBdcDjInfoList(bdcDJInfoList);
        }

        //不动产抵押信息列表
        String mortgageInfoVoList = getString(data.get("mortgageInfoVoList"));
        if (StringUtils.isNotBlank(mortgageInfoVoList)){
            JSONArray mortgageInfoVoListJson = JSONArray.parseArray(mortgageInfoVoList);
            List<BdcDyInfo> bdcDYInfoList = new ArrayList<>();
            for (int i = 0; i < mortgageInfoVoListJson.size(); i++) {
                JSONObject bdcDYInfo = (JSONObject) mortgageInfoVoListJson.get(i);
                BdcDyInfo dyInfo = new BdcDyInfo();
                //抵押证明号
                dyInfo.setDYZMH(getString(bdcDYInfo.get("warrantId")));

                //抵押权人信息列表
                String mortgageeInfoVoList = getString(bdcDYInfo.get("mortgageeInfoVoList"));
                //填充抵押权人列表
                if (StringUtils.isNotBlank(mortgageeInfoVoList)) {
                    JSONArray mortgageeInfoVoListJson = JSONArray.parseArray(mortgageeInfoVoList);
                    List<QLR> dyrList = new ArrayList<>();
                    for (int j = 0; j < mortgageeInfoVoListJson.size(); j++) {
                        JSONObject DYRJson = (JSONObject) mortgageeInfoVoListJson.get(j);
                        QLR dyr = new QLR();
                        dyr.setQLRMC(getString(DYRJson.get("mortgageeName")));
                        dyr.setZJHM(getString(DYRJson.get("mortgageeId")));
                        dyrList.add(dyr);
                    }
                    dyInfo.setDYRList(dyrList);
                }

                //房屋抵押面积
                dyInfo.setFWDYMJ(formatNumber(getString(bdcDYInfo.get("mortgageArea"))));
                //土地抵押面积
                dyInfo.setTDDYMJ(formatNumber(getString(bdcDYInfo.get("mortgageLandArea"))));
                //抵押方式
                dyInfo.setDYFS(getString(bdcDYInfo.get("mortgageReason")));
                //被担保债券数额
                dyInfo.setBDBZQSE(formatNumber(getString(bdcDYInfo.get("creditAmount"))));
                //抵押顺位
                dyInfo.setDYSW(getString(bdcDYInfo.get("mortgageOrder")));
                //债务履行期限
                StringBuffer zwlvqx = new StringBuffer();
                String mortgageStartDate = getString(bdcDYInfo.get("mortgageStartDate"));
                String mortgageEndDate = getString(bdcDYInfo.get("mortgageEndDate"));
                if (StringUtils.isBlank(mortgageStartDate)){
                    //为空留出默认空格
                    zwlvqx.append("    ~");
                }else {
                    zwlvqx.append(mortgageStartDate);
                    zwlvqx.append("~");
                }
                if (StringUtils.isNotBlank(mortgageEndDate)){
                    //为空留出默认空格
                    zwlvqx.append(mortgageEndDate);
                }
                dyInfo.setZWLVQX(zwlvqx.toString());

                //第三方借款人信息列表
                String borrowerInfoVoList = getString(bdcDYInfo.get("borrowerInfoVoList"));
                //填充第三方借款人列表
                if (StringUtils.isNotBlank(borrowerInfoVoList)) {
                    JSONArray borrowerInfoVoListJson = JSONArray.parseArray(borrowerInfoVoList);
                    List<QLR> dsfList = new ArrayList<>();
                    for (int j = 0; j < borrowerInfoVoListJson.size(); j++) {
                        JSONObject DSFJson = (JSONObject) borrowerInfoVoListJson.get(j);
                        QLR dsf = new QLR();
                        //第三方借款人
                        dsf.setQLRMC(getString(DSFJson.get("borrowerName")));
                        //证件类型
                        dsf.setZJLB(getString(DSFJson.get("borrowerId")));
                        //证件号码
                        dsf.setZJHM(getString(DSFJson.get("borrowerIdType")));
                        dsfList.add(dsf);
                    }
                    dyInfo.setDSFList(dsfList);
                }
                bdcDYInfoList.add(dyInfo);
            }
            judicialQueryVo.setBdcDyInfoList(bdcDYInfoList);
        }

        //不动产查封信息列表
        String attachmentInfoVoList = getString(data.get("attachmentInfoVoList"));
        if (StringUtils.isNotBlank(attachmentInfoVoList)) {
            JSONArray attachmentInfoVoListJson = JSONArray.parseArray(attachmentInfoVoList);
            List<BdcCfInfo> bdcDYInfoList = new ArrayList<>();
            for (int i = 0; i < attachmentInfoVoListJson.size(); i++) {
                JSONObject bdcCFInfo = (JSONObject) attachmentInfoVoListJson.get(i);
                BdcCfInfo cfInfo = new BdcCfInfo();
                //查封机关
                cfInfo.setCFJG(getString(bdcCFInfo.get("attachOrgan")));
                //查封文号
                cfInfo.setCFWH(getString(bdcCFInfo.get("attachNumber")));
                //申请执行人
                cfInfo.setYGR(getString(bdcCFInfo.get("applicant")));
                //被执行人
                cfInfo.setQLR(getString(bdcCFInfo.get("executedPerson")));
                //查封期限
                cfInfo.setQX(getString(bdcCFInfo.get("attachTerm")));
                //查封起止日期
                StringBuffer cfqzrq = new StringBuffer();
                String attachStartDate = getString(bdcCFInfo.get("attachStartDate"));
                String attachEndDate = getString(bdcCFInfo.get("attachEndDate"));
                if (StringUtils.isBlank(attachStartDate)){
                    //为空留出默认空格
                    cfqzrq.append("    ~");
                }else {
                    cfqzrq.append(attachStartDate);
                    cfqzrq.append("~");
                }
                if (StringUtils.isNotBlank(attachEndDate)){
                    //为空留出默认空格
                    cfqzrq.append(attachEndDate);
                }
                cfInfo.setCFJSSJ(cfqzrq.toString());
                //查封原因
                cfInfo.setCFYY(getString(bdcCFInfo.get("attachReason")));

                bdcDYInfoList.add(cfInfo);
            }
            judicialQueryVo.setBdcCfInfoList(bdcDYInfoList);
        }

        //设置查询日期
        judicialQueryVo.setCXRQ(DateUtil.now());

        return judicialQueryVo;
    }

    /**
     * 字符串非空
     * @param object
     * @return
     */
    private String getString(Object object){
        return object ==null?"":object.toString();
    }

    /**
     * 格式化数字
     * @param number
     * @return
     */
    private String formatNumber(String number){
        if (StringUtils.isBlank(number)){
            return number;
        }
        DecimalFormat format = new DecimalFormat("0.00");
        try {
            number = format.format(new BigDecimal(number));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }

}
