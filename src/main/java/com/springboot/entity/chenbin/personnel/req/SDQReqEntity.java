package com.springboot.entity.chenbin.personnel.req;

import com.springboot.config.ZtgeoBizException;
import com.springboot.popj.pub_data.*;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.springboot.constant.penghao.BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_FD;

/**
 * 电力的分发请求实体
 * @author chenb
 * @version 2020/4/29/029
 * description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SDQReqEntity {
    private String originalUserName;        //原用户名
    private String originalUserCard;        //原用户身份证
    private String newUserName;             //新用户名
    private String newOriginalUserCard;     //新用户身份证
    private String newMobile;               //新用户联系方式
    private String qxno;                    //新不动产权证号
    private String bdcno;                   //不动产单元号
    private String address;                  //地址（坐落）
    private String orgNo;                   //32413
    private String contractId;              //一窗收件编号
    private List<SDQFile> data = new ArrayList<>();  //附件信息

    public SDQReqEntity getBaseFromBdcql(SJ_Info_Bdcqlxgxx bdcql){
        this.qxno = bdcql.getImmovableCertificateNo();
        this.address = bdcql.getImmovableSite();
        List<SJ_Bdc_Gl> bdcgls = bdcql.getGlImmovableVoList();
        String bdcdyhs = "";
        if(bdcgls!=null) {
            for (SJ_Bdc_Gl bdcgl : bdcgls) {
                if(IMMOVABLE_TYPE_OF_FD.equals(bdcgl.getImmovableType())) {
                    bdcdyhs = bdcdyhs + "," + bdcgl.getFwInfo().getImmovableUnitNumber();
                }
            }
        }
        if(bdcdyhs.contains(","))
            bdcdyhs = bdcdyhs.substring(1);
        this.bdcno = bdcdyhs;
        return this;
    }

    public SDQReqEntity replenishFromJyxx(String sqbh, String noticeName, String noticeMobile, Sj_Info_Jyhtxx jyxx){
        this.contractId = sqbh;
        this.newUserName = noticeName;
        this.newMobile = noticeMobile;
        List<SJ_Qlr_Gl> buys = jyxx.getGlHouseBuyerVoList();
        SJ_Qlr_Info noticePerson = BusinessDealBaseUtil.getNeedPerson(noticeName,buys);
        if(noticePerson == null){
            List<SJ_Qlr_Gl> buyAgents = jyxx.getGlAgentVoList();//代理人里取
            noticePerson = BusinessDealBaseUtil.getNeedPerson(noticeName,buyAgents);
            if(noticePerson == null){
                throw new ZtgeoBizException("买房通知人与权利人及代理人设置不符");
            }
        }
        this.newOriginalUserCard = noticePerson.getObligeeDocumentNumber();
        List<SJ_Qlr_Gl> sellers = jyxx.getGlHouseSellerVoList();
        if(sellers==null || sellers.size()<1){
            throw new ZtgeoBizException("房屋卖方不明");
        }
        this.originalUserName = sellers.get(0).getRelatedPerson().getObligeeName();
        this.originalUserCard = sellers.get(0).getRelatedPerson().getObligeeDocumentNumber();
        return this;
    }

    public void assignOrg(SJ_Info_Sdqgxx sdqgxx){
        this.setOrgNo("");
    }
}
