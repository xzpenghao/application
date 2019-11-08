package com.springboot.service.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.entity.chenbin.personnel.other.web.RequestParamBody;

public interface ExchangeToWebService {
    Object initWebSecReg(RequestParamBody paramBody);
}
