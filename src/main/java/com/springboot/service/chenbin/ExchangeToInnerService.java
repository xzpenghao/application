package com.springboot.service.chenbin;

import com.springboot.entity.chenbin.personnel.other.paph.PaphEntity;
import com.springboot.entity.chenbin.personnel.pub_use.SJ_Sjsq_User_Ext;
import com.springboot.entity.chenbin.personnel.req.PaphReqEntity;
import com.springboot.popj.pub_data.SJ_Info_Bdcqlxgxx;
import com.springboot.popj.warrant.ParametricData;

import java.text.ParseException;
import java.util.List;

public interface ExchangeToInnerService {
    String dealYGYD2Inner(String commonInterfaceAttributer) throws ParseException;
    String secTra2InnerWithoutDY(String commonInterfaceAttributer) throws ParseException;
    String secTra2InnerWithDY(String commonInterfaceAttributer) throws ParseException;
    String processAutoSubmit(String commonInterfaceAttributer) throws ParseException;

    List<PaphEntity> getPaphMortBefore(PaphReqEntity paph);
    List<PaphEntity> getPaphMortAfter(PaphReqEntity paph);
    List<SJ_Info_Bdcqlxgxx> getBdcQlInfoWithItsRights(ParametricData parametricData);

    List<SJ_Sjsq_User_Ext> getBdcUsers(String pid);
}
