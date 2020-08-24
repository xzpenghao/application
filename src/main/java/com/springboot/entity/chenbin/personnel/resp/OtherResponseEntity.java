package com.springboot.entity.chenbin.personnel.resp;

import com.springboot.config.ZtgeoBizException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.BDC_INTF_HANDLE_RETURN_CODE_SUCCESS;
import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.BDC_INTF_HANDLE_RETURN_CODE_UNSUCCESS;

/**
 * @author chenb
 * @version 2020/4/29/029
 * description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherResponseEntity<T>{
    private String code;
    private String msg;
    private T data;

    public OtherResponseEntity checkSelfIfBdc(String intfDescribe){
        if(BDC_INTF_HANDLE_RETURN_CODE_UNSUCCESS.equals(this.getCode()))
            throw new ZtgeoBizException(intfDescribe+"运行失败，返回异常信息："+this.getMsg());
        if(!BDC_INTF_HANDLE_RETURN_CODE_SUCCESS.equals(this.getCode()))
            throw new ZtgeoBizException(intfDescribe+"响应CODE不符合规范，取值范围是[0,1],当前响应：【"+this.getCode()+"】");
        return this;
    }
}
