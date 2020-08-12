package com.springboot.entity.newPlat.result;

import lombok.Data;

/**
 * @author chenb
 * @version 2020/8/12
 * description：附件下载结果对象
 */
@Data
public class FtpFileDownResult {
    private boolean isSuccess;
    private String error;
    private String fjdx;
    private byte[] fjnr;

    public FtpFileDownResult success(String fjdx,byte[] fjnr){
        this.isSuccess = true;
        this.fjdx = fjdx;
        this.fjnr = fjnr;
        return this;
    }
    public FtpFileDownResult fail(String error){
        this.isSuccess = false;
        this.error = error;
        return this;
    }
}
