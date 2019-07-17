package com.springboot.component;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.popj.FwInfo;
import com.springboot.popj.GlImmovable;
import com.springboot.popj.RelatedPerson;
import com.springboot.popj.netSign.BusinessContract;
import com.springboot.popj.netSign.GlHouseBuyer;
import com.springboot.popj.netSign.GlHouseSeller;
import com.springboot.util.ParseXML;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            businessContract.setContractNumber((String) htfwmap.get("HTBH"));//合同编码
            businessContract.setContractSignTime((String) htfwmap.get("HTQDRQ"));//合同签订时间
            businessContract.setDataJson(htxml);//原始查询数据
            businessContract.setDataComeFromMode("xml形式获取");//数据获取方式
            businessContract.setContractAmount((String) htfwmap.get("HTJE"));//合同金额
            glImmovable.setFwInfo(fwInfo);
            fwInfo.setHouseholdId((String) htfwmap.get("HID"));//房屋户Id
            fwInfo.setSeatId((String) htfwmap.get("ZID"));//幢id
            fwInfo.setHouseLocation((String) htfwmap.get("HTZL"));//坐落
            fwInfo.setArchitecturalArea(htfwmap.get("JZMJ").toString());//建筑面积
            fwInfo.setHouseArchitecturalArea(htfwmap.get("TNJZMJ").toString());//套内建筑
            fwInfo.setApportionmentArchitecturalArea(htfwmap.get("FTJZMJ").toString());//分摊建筑面积
            fwInfo.setBuildingNumber((String) htfwmap.get("LPBH"));//楼盘编号
            fwInfo.setHouseNumber((String) htfwmap.get("FWBH"));//房屋编号
            businessContract.getGlImmovableList().add(glImmovable);
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
            String zjlb =realEstateMortgageComponent.getZjlb(zjlx);
            if (Integer.parseInt((String) qlrmap.get("XGRLX")) == 0) {
                GlHouseSeller glHouseSeller = new GlHouseSeller();
                glHouseSeller.setObligeeName((String) qlrmap.get("XGRMC"));//售房名称
                glHouseSeller.setObligeeType(obligeeType);
                glHouseSeller.setObligeeOrder(Integer.parseInt((String) qlrmap.get("XGRSX")));
                RelatedPerson relatedPerson = new RelatedPerson();
                relatedPerson.setObligeeDocumentType(zjlb);
                relatedPerson.setObligeeDocumentNumber((String)qlrmap.get("ZJHM"));
                businessContract.getGlHouseSellerList().add(glHouseSeller);
                glHouseSeller.setRelatedPerson(relatedPerson);
            }else {
                GlHouseBuyer glHouseBuyer = new GlHouseBuyer();
                glHouseBuyer.setObligeeName((String) qlrmap.get("XGRMC"));//买房
                glHouseBuyer.setObligeeType(obligeeBuyType);
                glHouseBuyer.setObligeeOrder(Integer.parseInt((String) qlrmap.get("XGRSX")));
                RelatedPerson relatedPerson = new RelatedPerson();
                relatedPerson.setObligeeDocumentType(zjlb);
                relatedPerson.setObligeeDocumentNumber((String)qlrmap.get("ZJHM"));
                businessContract.getGlHouseBuyerList().add(glHouseBuyer);
                glHouseBuyer.setRelatedPerson(relatedPerson);
            }
        }
        rv.setMessage("success");
        rv.setData(businessContract);
        return rv;
    }
}
