package com.springboot.component.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.constant.chenbin.BusinessConstant;
import com.springboot.entity.chenbin.personnel.pub_use.Biz_Request_Intercept;
import com.springboot.mapper.RequestInterceptMapper;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RequestInterceptComponent {
    @Autowired
    private RequestInterceptMapper requestInterceptMapper;

    public void dealSaveThisIntercept(
            Biz_Request_Intercept requestIntercept,
            ObjectRestResponse<String> rv,
            String receiptNumber,
            String interfaceCode,
            String username,
            String functionDescription,
            String company
    ) {
        if(requestIntercept!=null){
            requestIntercept.setExecutResult(JSONObject.toJSONString(rv));
            requestIntercept.setResultCode(rv.getStatus());
            requestIntercept.setOperationCount((requestIntercept.getOperationCount()+1));
            requestIntercept.setUpdateTime(TimeUtil.getTimeString(new Date()));
            requestInterceptMapper.updateEntity(requestIntercept);
        }else{
            requestIntercept = new Biz_Request_Intercept();
            requestIntercept.setId(IDUtil.getReqId());
            requestIntercept.setBusinessId(receiptNumber);
            requestIntercept.setInterfaceCode(interfaceCode);
            requestIntercept.setRequester(username);
            requestIntercept.setResultCode(rv.getStatus());
            requestIntercept.setExecutResult(JSONObject.toJSONString(rv));
            requestIntercept.setFunctionDescription(functionDescription);
            requestIntercept.setInsertTime(TimeUtil.getTimeString(new Date()));
            requestIntercept.setUpdateTime(TimeUtil.getTimeString(new Date()));
            requestIntercept.setOperationCount(1);
            requestIntercept.setCompany(company);
            requestInterceptMapper.insertEntity(requestIntercept);
        }
    }
}
