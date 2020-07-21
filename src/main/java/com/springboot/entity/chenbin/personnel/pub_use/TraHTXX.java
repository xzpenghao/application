package com.springboot.entity.chenbin.personnel.pub_use;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chenb
 * @version 2020/7/21/021
 * description：泗洪特有住建合同信息
 */
public class TraHTXX extends HTXX {

    @JsonProperty("DKYH")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String DKYH;

    @JsonProperty("DKJE")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String DKJE;

    public static TraHTXX init(HTXX htxx){
        return JSONObject.parseObject(JSONObject.toJSONString(htxx),TraHTXX.class);
    }

    public String getDKYH() {
        return DKYH;
    }
    @JsonIgnore
    public void setDKYH(String DKYH) {
        this.DKYH = DKYH;
    }

    public String getDKJE() {
        return DKJE;
    }
    @JsonIgnore
    public void setDKJE(String DKJE) {
        this.DKJE = DKJE;
    }
}
