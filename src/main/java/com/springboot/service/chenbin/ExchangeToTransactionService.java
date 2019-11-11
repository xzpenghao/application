package com.springboot.service.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.entity.chenbin.personnel.tra.TraRespBody;

import java.text.ParseException;

public interface ExchangeToTransactionService {
    String deal2Tra(String commonInterfaceAttributer) throws ParseException;
    ObjectRestResponse<String> examineSuccess4Tra(TraRespBody traRespBody);
}
