package com.springboot.entity.chenbin.personnel.other.web;

import com.springboot.config.ZtgeoBizException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

public class RequestParamBody implements Serializable {
    private String returnSlbh;
    private RequestParamSjsq recEntity;
    private List<ImmovableFile> fileVoList;

    public String getReturnSlbh() {
        return returnSlbh;
    }

    public void setReturnSlbh(String returnSlbh) {
        this.returnSlbh = returnSlbh;
    }

    public RequestParamSjsq getRecEntity() {
        return recEntity;
    }

    public void setRecEntity(RequestParamSjsq recEntity) {
        this.recEntity = recEntity;
    }

    public List<ImmovableFile> getFileVoList() {
        return fileVoList;
    }

    public void setFileVoList(List<ImmovableFile> fileVoList) {
        this.fileVoList = fileVoList;
    }

    public void checkSelfStandard(){
        if(StringUtils.isBlank(this.returnSlbh)){
            this.returnSlbh = "0";
        }
        if(recEntity==null)
            throw new ZtgeoBizException("请求主体数据【recEntity】未传入！");
    }
}
