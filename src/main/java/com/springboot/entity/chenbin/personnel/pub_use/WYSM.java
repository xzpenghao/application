package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author chenb
 * @version 2020/8/27
 * description：
 */
public class WYSM implements Serializable {
    @JsonProperty("JFWYZFQX")
    private String JFWYZFQX;        //甲方（卖方）违约支付期限
    @JsonProperty("JFWYTHQX")
    private String JFWYTHQX;        //甲方违约退还期限
    @JsonProperty("JFWYLXZFFE")
    private String JFWYLXZFFE;        //甲方违约利息支付份额
    @JsonProperty("YFWYZNFE")
    private String YFWYZNFE;        //双方违约滞纳金份额

    public String getJFWYZFQX() {
        return JFWYZFQX;
    }
    @JsonIgnore
    public void setJFWYZFQX(String JFWYZFQX) {
        this.JFWYZFQX = JFWYZFQX;
    }

    public String getJFWYTHQX() {
        return JFWYTHQX;
    }
    @JsonIgnore
    public void setJFWYTHQX(String JFWYTHQX) {
        this.JFWYTHQX = JFWYTHQX;
    }

    public String getJFWYLXZFFE() {
        return JFWYLXZFFE;
    }
    @JsonIgnore
    public void setJFWYLXZFFE(String JFWYLXZFFE) {
        this.JFWYLXZFFE = JFWYLXZFFE;
    }

    public String getYFWYZNFE() {
        return YFWYZNFE;
    }
    @JsonIgnore
    public void setYFWYZNFE(String YFWYZNFE) {
        this.YFWYZNFE = YFWYZNFE;
    }
}
