package com.springboot.service.chenbin.impl;

import com.alibaba.fastjson.JSONObject;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.tax.TaxParamBody;
import com.springboot.popj.pub_data.SJ_Info_Bdcqlxgxx;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.popj.pub_data.Sj_Info_Dyhtxx;
import com.springboot.popj.pub_data.Sj_Info_Jyhtxx;
import com.springboot.service.chenbin.ExchangeToTaxService;
import com.springboot.util.SysPubDataDealUtil;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Slf4j
@Service("exc2Tax")
public class ExchangeToTaxServiceImpl implements ExchangeToTaxService {
    @Override
    public String deal2Tax(String commonInterfaceAttributer) throws ParseException {
        String result = "处理成功";
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        if (sjsq.getTransactionContractInfo() == null) {
            throw new ZtgeoBizException("业务税务系统分发时异常，交易合同信息为空");
        }
        if (sjsq.getImmovableRightInfoVoList() == null) {
            throw new ZtgeoBizException("业务税务系统分发时异常，不动产权利信息为空");
        }
        //准备参数
        TaxParamBody taxParamBody = BusinessDealBaseUtil.dealParamForTax(sjsq);
        System.out.println("进入税务处理，参数转换为:"+JSONObject.toJSONString(taxParamBody));
        //调用Feign
        return result;
    }
}
