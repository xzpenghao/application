package com.springboot.config;

import com.github.ag.core.exception.BaseException;
import com.springboot.constant.chenbin.BusinessConstant;
import com.springboot.constant.penghao.BizOrBizExceptionConstant;

public class ZtgeoActivitiException extends BaseException {
    public ZtgeoActivitiException(String message) {
        super(message, BusinessConstant.ZTGEO_ACT_ERROR_CODE);
    }
}
