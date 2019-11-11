package com.springboot.service.chenbin.other;

import com.springboot.entity.chenbin.personnel.other.other_depart.BookReturnEntity;
import com.springboot.entity.chenbin.personnel.tra.TraRespBody;
import com.springboot.popj.pub_data.SJ_Info_Bdcqlxgxx;
import com.springboot.popj.pub_data.Sj_Info_Jyhtxx;
import com.springboot.popj.warrant.ParametricData;

import java.text.ParseException;
import java.util.List;

public interface ExchangeInterfaceService {
    String sdqg2Inner(String commonInterfaceAttributer) throws ParseException;
    String firstReg2Inner(String commonInterfaceAttributer, String yhbs);
    String receiptSuccess(String slbh);
    String JFSuccess(String slbh);
    String JSSuccess(String slbh);
    String JFAndJSSuccess(String slbh);
    String bookSuccess(BookReturnEntity bookRequest);
    List<Sj_Info_Jyhtxx> getHtInfo(String htbah);
    List<Sj_Info_Jyhtxx> getSecondHtInfo(String htbah);
    List<SJ_Info_Bdcqlxgxx> getBDCQLInfo(ParametricData parametricData) throws ParseException;
    Object examineSuccess1(String slbh);
    Object examineSuccess3(String slbh, String water);
}
