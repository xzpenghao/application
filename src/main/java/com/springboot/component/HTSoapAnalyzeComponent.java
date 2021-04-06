package com.springboot.component;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.google.common.collect.Maps;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.feign.ExchangeWithOuterFeign;
import com.springboot.popj.FwInfo;
import com.springboot.popj.GlImmovable;
import com.springboot.popj.RelatedPerson;
import com.springboot.popj.netSign.BusinessContract;
import com.springboot.popj.netSign.GlHouseBuyer;
import com.springboot.popj.netSign.GlHouseSeller;
import com.springboot.popj.pub_data.SJ_Bdc_Gl;
import com.springboot.popj.pub_data.Sj_Info_Jyhtxx;
import com.springboot.util.ParseXML;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component("htSoapAnalyzeComp")
public class HTSoapAnalyzeComponent {

    @Value("${immovable.type}")
    private String immovableType;
    @Value("${glHouseSeller.obligeeType}")
    private String obligeeType;
    @Value("${glHouseBuyer.obligeeType}")
    private String obligeeBuyType;

    @Autowired
    private RealEstateMortgageComponent realEstateMortgageComponent;
    @Autowired
    private ExchangeWithOuterFeign withOuterFeign;

    /**
     * 功能描述: <br>
     * 〈〉 通过合同契约号或合同备案号获取交易合同信息
     * @Param: [htqyh, htbah]
     * @Return: com.springboot.popj.pub_data.Sj_Info_Jyhtxx
     * @Author: Peng Hao
     * @Date: 2021/4/2 11:14
     */
    public Sj_Info_Jyhtxx getTransactionContract(String registrationNumber,String obligeeName,String obligeeDocumentNumber) {
        Map<String, String> paramMap = Maps.newHashMap();
        if (StringUtils.isEmpty(registrationNumber)) {
            throw new ZtgeoBizException("缺少合同契约号");
        }
        // 1.1 记录传入参数
        log.info("合同契约号:{},权利人:{},权利人证件号码:{}", registrationNumber,obligeeName,obligeeDocumentNumber);
        paramMap.put("registrationNumber", registrationNumber);
        paramMap.put("obligeeName", obligeeName);
        paramMap.put("obligeeDocumentNumber", obligeeDocumentNumber);
        // 1.2 查询交易合同信息接口
        OtherResponseEntity<Sj_Info_Jyhtxx> entity = withOuterFeign.jyhtxxcx(paramMap);
        // 1.3 记录返回数据
        log.info("接口返回值:{}", JSON.toJSONString(entity));
        if (Objects.isNull(entity) || "0".equals(entity.getCode())) {
            throw new ZtgeoBizException("接口请求失败");
        }
        if (Objects.isNull(entity.getData())) {
            throw new ZtgeoBizException("接口返回的数据为空");
        }
        Sj_Info_Jyhtxx jyhtxx = entity.getData();
        List<SJ_Bdc_Gl> bdc_gls =  jyhtxx.getGlImmovableVoList();
        if (Objects.isNull(bdc_gls.stream().findFirst().get().getFwInfo())){
            throw new ZtgeoBizException("接口返回数据不符合要求,没有房屋信息");
        }
        bdc_gls.stream().forEach(bdcGl -> bdcGl.setImmovableType(Msgagger.FANGDI));
        jyhtxx.setGlImmovableVoList(bdc_gls);
        // 存入前端需求值
        return jyhtxx;
    }



    public ObjectRestResponse ersxxSoap(String clhtbah, String htbah) throws DocumentException {
        ObjectRestResponse rv = new ObjectRestResponse();
        Document docHT = DocumentHelper.parseText(clhtbah);//报文转成doc对象
        Element root = docHT.getRootElement();//获取根元素，准备递归解析这个XML树
        List<Element> elems = root.element("Body")
                .element("CLF_FC_CLMMHTResponse")
                .element("CLF_FC_CLMMHTResult")
                .element("data")
                .elements("row");
        BusinessContract businessContract = new BusinessContract();
        try {
            for (Element elem : elems) {
                FwInfo fwInfo = new FwInfo();
                GlImmovable glImmovable = new GlImmovable();
                glImmovable.setImmovableType(immovableType);
                Map<String, Object> htfwmap = ParseXML.parseByElement(elem);
                businessContract.setContractId((String) htfwmap.get("HTID"));//合同id
                businessContract.setContractRecordTime((String) htfwmap.get("HTBASJ"));//合同备案时间
                businessContract.setDataJson(clhtbah);//原始查询数据
                businessContract.setDataComeFromMode(Msgagger.XML_FROMDATA);//数据获取方式
                fwInfo.setHouseLocation((String) htfwmap.get("FWZL"));//坐落
                fwInfo.setHouseholdId((String) htfwmap.get("HID"));//房屋户Id
                fwInfo.setSeatId((String) htfwmap.get("ZID"));//幢id
                if (StringUtils.isNotEmpty(htfwmap.get("JZMJ").toString())) {
                    fwInfo.setArchitecturalArea(new BigDecimal(htfwmap.get("JZMJ").toString()));//建筑面积
                }
                if (StringUtils.isNotEmpty(htfwmap.get("FTJZMJ").toString())) {
                    fwInfo.setApportionmentArchitecturalArea(new BigDecimal(htfwmap.get("FTJZMJ").toString()));//分摊建筑面积
                }
                if (StringUtils.isNotEmpty(htfwmap.get("TNJZMJ").toString())) {
                    fwInfo.setHouseArchitecturalArea(new BigDecimal(htfwmap.get("TNJZMJ").toString()));//套内建筑
                }
                glImmovable.setFwInfo(fwInfo);
                businessContract.getGlImmovableVoList().add(glImmovable);
            }
            Document docQLR = DocumentHelper.parseText(htbah);//报文转成doc对象
            Element rootQLR = docQLR.getRootElement();//获取根元素，准备递归解析这个XML树
            List<Element> elemqs = rootQLR.element("Body")
                    .element("FC_GFQLRXXResponse")
                    .element("FC_GFQLRXXResult")
                    .element("data")
                    .elements("row");
            for (Element elemq : elemqs) {
                Map<String, Object> qlrmap = ParseXML.parseByElement(elemq);
                String zjlx = (String) qlrmap.get("ZJLX");
                String zjlb = realEstateMortgageComponent.getZjlb(zjlx);
                if (Integer.parseInt((String) qlrmap.get("XGRLX")) == 0) {
                    //售房者
                    businessContract.getGlHouseSellerVoList().add(getGlHouseSeller(zjlb, obligeeType, qlrmap));
                } else {
                    businessContract.getGlHouseBuyerVoList().add(getGlHouseBuyer(zjlb, obligeeType, qlrmap));
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
            log.error(e.getMessage().toString());
        }
        rv.setMessage("success");
        List<BusinessContract> businessContracts = new ArrayList<BusinessContract>();
        businessContracts.add(businessContract);
        rv.setData(businessContracts);
        return rv;
    }


    private GlHouseSeller getGlHouseSeller(String zjlb, String obligeeType, Map<String, Object> qlrmap) {
        GlHouseSeller glHouseSeller = new GlHouseSeller();
        glHouseSeller.setObligeeName((String) qlrmap.get("XGRMC"));//售房名称
        glHouseSeller.setObligeeType(obligeeType);
        if (StringUtils.isNotEmpty((String) qlrmap.get("XGRSX"))) {
            glHouseSeller.setObligeeOrder(Integer.parseInt((String) qlrmap.get("XGRSX")));
        }
        RelatedPerson relatedPerson = new RelatedPerson();
        relatedPerson.setObligeeName((String) qlrmap.get("XGRMC"));
        relatedPerson.setObligeeDocumentType(zjlb);
        relatedPerson.setObligeeDocumentNumber((String) qlrmap.get("ZJHM"));
        glHouseSeller.setRelatedPerson(relatedPerson);
        return glHouseSeller;
    }


    private GlHouseBuyer getGlHouseBuyer(String zjlb, String obligeeType, Map<String, Object> qlrmap) {
        GlHouseBuyer glHouseBuyer = new GlHouseBuyer();
        glHouseBuyer.setObligeeName((String) qlrmap.get("XGRMC"));//买房
        glHouseBuyer.setObligeeType(obligeeType);
        if (StringUtils.isNotEmpty((String) qlrmap.get("XGRSX"))) {
            glHouseBuyer.setObligeeOrder(Integer.parseInt((String) qlrmap.get("XGRSX")));
        }
        RelatedPerson relatedPerson = new RelatedPerson();
        relatedPerson.setObligeeDocumentType(zjlb);
        relatedPerson.setObligeeDocumentNumber((String) qlrmap.get("ZJHM"));
        relatedPerson.setObligeeName((String) qlrmap.get("XGRMC"));
        glHouseBuyer.setRelatedPerson(relatedPerson);
        return glHouseBuyer;
    }


    /**
     * 商品房信息获取
     *
     * @param htxml
     * @param qlrxml
     * @return
     * @throws DocumentException
     */
    public ObjectRestResponse analyzeSoap(String htxml, String qlrxml) throws DocumentException {
        log.info("进入合同信息解析页面，其中htxml:" + htxml + ";\n    qlrxml:" + qlrxml);
        ObjectRestResponse rv = new ObjectRestResponse();
        BusinessContract businessContract = new BusinessContract();
        Document docHT = DocumentHelper.parseText(htxml);//报文转成doc对象
        Element root = docHT.getRootElement();//获取根元素，准备递归解析这个XML树
        List<Element> elems = root.element("Body")
                .element("FC_SPFYGHTResponse")
                .element("FC_SPFYGHTResult")
                .element("data")
                .elements("row");
        List<BusinessContract> htfws = new ArrayList<BusinessContract>();
        for (Element elem : elems) {
            FwInfo fwInfo = new FwInfo();
            GlImmovable glImmovable = new GlImmovable();
            glImmovable.setImmovableType(immovableType);
            Map<String, Object> htfwmap = ParseXML.parseByElement(elem);
            businessContract.setContractId((String) htfwmap.get("HTID"));//合同id
            businessContract.setContractRecordNumber((String) htfwmap.get("SPFHTBAH"));//合同备案号
            businessContract.setContractRecordTime((String) htfwmap.get("HTBASJ"));//合同备案时间
            businessContract.setContractNumber((String) htfwmap.get("HTBH"));//合同编码
            businessContract.setContractSignTime((String) htfwmap.get("HTQDRQ"));//合同签订时间
            businessContract.setDataJson(htxml);//原始查询数据
            businessContract.setDataComeFromMode(Msgagger.XML_FROMDATA);//数据获取方式
            businessContract.setContractAmount((String) htfwmap.get("HTJE"));//合同金额
            fwInfo.setHouseholdId((String) htfwmap.get("HID"));//房屋户Id
            fwInfo.setSeatId((String) htfwmap.get("ZID"));//幢id
            fwInfo.setHouseLocation((String) htfwmap.get("HTZL"));//坐落
            if (StringUtils.isNotEmpty(htfwmap.get("JZMJ").toString())) {
                fwInfo.setArchitecturalArea(new BigDecimal(htfwmap.get("JZMJ").toString()));//建筑面积
            }
            if (StringUtils.isNotEmpty(htfwmap.get("TNJZMJ").toString())) {
                fwInfo.setHouseArchitecturalArea(new BigDecimal(htfwmap.get("TNJZMJ").toString()));//套内建筑
            }
            if (StringUtils.isNotEmpty(htfwmap.get("JZMJ").toString())) {
                fwInfo.setApportionmentArchitecturalArea(new BigDecimal(htfwmap.get("FTJZMJ").toString()));//分摊建筑面积
            }
            fwInfo.setBuildingNumber((String) htfwmap.get("LPBH"));//楼盘编号
            fwInfo.setHouseNumber((String) htfwmap.get("FWBH"));//房屋编号
            glImmovable.setFwInfo(fwInfo);
            businessContract.getGlImmovableVoList().add(glImmovable);
        }

        Document docQLR = DocumentHelper.parseText(qlrxml);//报文转成doc对象
        Element rootQLR = docQLR.getRootElement();//获取根元素，准备递归解析这个XML树
        List<Element> elemqs = rootQLR.element("Body")
                .element("SPF_FC_CLMMHTResponse")
                .element("SPF_FC_CLMMHTResult")
                .element("data")
                .elements("row");

        for (Element elemq : elemqs) {
            Map<String, Object> qlrmap = ParseXML.parseByElement(elemq);
            String zjlx = (String) qlrmap.get("ZJLX");
            String zjlb = realEstateMortgageComponent.getZjlb(zjlx);
            if (Integer.parseInt((String) qlrmap.get("XGRLX")) == 0) {
                businessContract.getGlHouseSellerVoList().add(getGlHouseSeller(zjlb, obligeeType, qlrmap));
            } else {
                businessContract.getGlHouseBuyerVoList().add(getGlHouseBuyer(zjlb, obligeeType, qlrmap));
            }
        }
        rv.setMessage("success");
        List<BusinessContract> businessContracts = new ArrayList<BusinessContract>();
        businessContracts.add(businessContract);
        rv.setData(businessContracts);
        return rv;
    }
}



