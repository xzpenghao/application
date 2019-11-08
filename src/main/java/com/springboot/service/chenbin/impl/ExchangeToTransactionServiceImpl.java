package com.springboot.service.chenbin.impl;

import com.alibaba.fastjson.JSONObject;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.tra.TraParamBody;
import com.springboot.popj.pub_data.SJ_Info_Bdcqlxgxx;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.popj.pub_data.Sj_Info_Jyhtxx;
import com.springboot.service.chenbin.ExchangeToTransactionService;
import com.springboot.util.SysPubDataDealUtil;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Slf4j
@Service("exc2Tran")
public class ExchangeToTransactionServiceImpl implements ExchangeToTransactionService {
    @Override
    public String deal2Tra(String commonInterfaceAttributer) throws ParseException {
        String result = "处理成功";
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer, null, null, null);
        if (sjsq.getTransactionContractInfo() == null) {
            throw new ZtgeoBizException("业务交易系统分发时异常，交易合同信息为空");
        }
        if (sjsq.getImmovableRightInfoVoList() == null) {
            throw new ZtgeoBizException("业务交易系统分发时异常，不动产权利信息为空");
        }
        //准备参数
        TraParamBody traParamBody = BusinessDealBaseUtil.dealParamForTra(sjsq);
        System.out.println("进入交易处理，参数转换为:"+ JSONObject.toJSONString(traParamBody));
        //调用Feign
        return result;
    }
}
