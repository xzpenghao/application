package com.springboot.service.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.entity.chenbin.personnel.tax.TaxRespBody;

import java.text.ParseException;

public interface ExchangeToTaxService {
    String deal2Tax(String commonInterfaceAttributer) throws ParseException;
    ObjectRestResponse<String> examineSuccess4Tax(TaxRespBody taxRespBody);
}
