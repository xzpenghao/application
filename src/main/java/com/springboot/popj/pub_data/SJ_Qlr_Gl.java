package com.springboot.popj.pub_data;

import com.springboot.config.ZtgeoBizException;
import com.springboot.emm.DIC_RY_GYFS_Enums;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Cyr;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Qlr;
import com.springboot.util.newPlatBizUtil.DicConvertUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class SJ_Qlr_Gl implements Serializable {
    private String relationId;                      //关联id
    private String infoId;                          //收件信息id
    private String obligeeId;                       //权利人id
    private String obligeeName;                     //权利人名称
    private String obligeeType;                     //权利人类型（权利人、抵押人、债务人...）
    private Integer obligeeOrder;                    //权利人顺序
    private String sharedMode;                      //共有方式
    private String sharedValue;                     //共有份额
    private String infoTableIdentification;         //关联info表标识
    private String status;                          //状态

    private String sfcz;                            //是否持证()
    private String sczh;                            //所持证号

    private SJ_Qlr_Info relatedPerson;                    //相关人信息
    private SJ_Qlr_Info relatedAgent;                     //代理人信息

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getObligeeId() {
        return obligeeId;
    }

    public void setObligeeId(String obligeeId) {
        this.obligeeId = obligeeId;
    }

    public String getObligeeName() {
        return obligeeName;
    }

    public void setObligeeName(String obligeeName) {
        this.obligeeName = obligeeName;
    }

    public String getObligeeType() {
        return obligeeType;
    }

    public void setObligeeType(String obligeeType) {
        this.obligeeType = obligeeType;
    }

    public Integer getObligeeOrder() {
        return obligeeOrder;
    }

    public void setObligeeOrder(Integer obligeeOrder) {
        this.obligeeOrder = obligeeOrder;
    }

    public String getSharedMode() {
        return sharedMode;
    }

    public void setSharedMode(String sharedMode) {
        this.sharedMode = sharedMode;
    }

    public String getSharedValue() {
        return sharedValue;
    }

    public void setSharedValue(String sharedValue) {
        this.sharedValue = sharedValue;
    }

    public String getInfoTableIdentification() {
        return infoTableIdentification;
    }

    public void setInfoTableIdentification(String infoTableIdentification) {
        this.infoTableIdentification = infoTableIdentification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SJ_Qlr_Info getRelatedPerson() {
        return relatedPerson;
    }

    public void setRelatedPerson(SJ_Qlr_Info relatedPerson) {
        this.relatedPerson = relatedPerson;
    }

    public String getSfcz() {
        return sfcz;
    }

    public void setSfcz(String sfcz) {
        this.sfcz = sfcz;
    }

    public String getSczh() {
        return sczh;
    }

    public void setSczh(String sczh) {
        this.sczh = sczh;
    }

    public SJ_Qlr_Info getRelatedAgent() {
        return relatedAgent;
    }

    public void setRelatedAgent(SJ_Qlr_Info relatedAgent) {
        this.relatedAgent = relatedAgent;
    }

    /**
     * 描述：权利人一窗规范产生
     *      共有份额的初步处理和数字判断
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[qlrzl, sort, qlr]
     * 返回：SJ_Qlr_Gl
     * 更新记录：更新人：{}，更新日期：{}
     */
    public SJ_Qlr_Gl initByDjqlr(String qlrzl,Integer sort,Qlr qlr){
        initByDjcyr(qlrzl,sort,qlr);
        this.setSharedMode(DicConvertUtil.getDicNameByVal(qlr.getGyfs(), DIC_RY_GYFS_Enums.values()));
        this.setSharedValue(StringUtils.isNotBlank(qlr.getGyfe())?qlr.getGyfe().replaceAll("%",""):null);
        if(StringUtils.isNotBlank(this.sharedValue)) {
            try {
                Float.parseFloat(this.sharedValue);
            }catch (NumberFormatException e){
                throw new ZtgeoBizException("权利人共有份额未通过数字检查");
            }
        }
        return this;
    }

    public SJ_Qlr_Gl initByDjcyr(String cyrzl, Integer sort, Cyr cyr){
        this.setObligeeName(cyr.getQlrmc());
        this.setObligeeType(cyrzl);
        this.setObligeeOrder(sort);
        return this;
    }
}
