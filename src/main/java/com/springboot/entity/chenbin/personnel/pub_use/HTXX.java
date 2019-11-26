package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class HTXX implements Serializable {
    @JsonProperty("HTJE")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double HTJE = -999d;
    @JsonProperty("FSSS")
    private String FSSS;
    @JsonProperty("SFCZ")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer SFCZ = -999;
    @JsonProperty("CZSM")
    private String CZSM;
    @JsonProperty("SFBHFS")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer SFBHFS = -999;		//是否包含附属设施 1 包含，0 不包含
    @JsonProperty("SFTG")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer SFTG = -999;			//是否资金托管 1 是，0 否
    @JsonProperty("TGKHH")
    private String TGKHH;			//资金托管开户行
    @JsonProperty("TGZH")
    private String TGZH;			//托管账户
    @JsonProperty("TGMFZFNR")
    private String TGMFZFNR;		//买方支付内容
    @JsonProperty("ZFFS")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer ZFFS = -999;			//支付方式 1 一次性付款，2 分期付款，3 贷款付款，4 其它付款方式
    @JsonProperty("FKRQ")
    private String FKRQ;			//付款日期（付款方式2）
    @JsonProperty("FQFKRQ1")
    private String FQFKRQ1;		//分期付款日期1（付款方式2）
    @JsonProperty("FQFKJE1")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double FQFKJE1 = -999d;		//分期付款金额1（付款方式2）
    @JsonProperty("FQFKRQ2")
    private String FQFKRQ2;		//分期付款日期2（付款方式2）
    @JsonProperty("FQFKJE2")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double FQFKJE2 = -999d;		//分期付款金额2（付款方式2）
    @JsonProperty("FQFKRQ3")
    private String FQFKRQ3;		//分期付款日期3（付款方式2）
    @JsonProperty("FQFKJE3")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double FQFKJE3 = -999d;		//分期付款金额3（付款方式2）

    @JsonProperty("DKFS")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer DKFS = -999;			 //贷款方式，1 银行按揭，2 公积金贷款（付款方式3）
    @JsonProperty("SFKRQ")
    private String SFKRQ;			//首付款日期（付款方式3）

    @JsonProperty("SFKJE")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double SFKJE = -999d;			 //首付款金额（付款方式3）
    @JsonProperty("DKSQRQ")
    private String DKSQRQ;			 //贷款申请日期（付款方式3）
    @JsonProperty("QTFKNR")
    private String QTFKNR;			 //其它付款内容（付款方式4）
    @JsonProperty("QTFKMFZF")
    private String QTFKMFZF;			 //其它付款-买方支付（付款方式4）
    @JsonProperty("JFCDSF")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer JFCDSF = -999;			 //买方承担税费情况，1 独自承担（此时YFCDSF为空），2 承担各自部分
    @JsonProperty("YFCDSF")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer YFCDSF = -999;			 //卖方承担税费情况，1 独自承担（此时JFCDSF为空），2 承担各自部分
    @JsonProperty("JFTS")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer JFTS = -999;			 //交付天数
    @JsonProperty("JFRQ")
    private String JFRQ;			  //交付日期

    public String getFSSS() {
        return FSSS;
    }
    @JsonIgnore
    public void setFSSS(String FSSS) {
        this.FSSS = FSSS;
    }

    public Integer getSFCZ() {
        return SFCZ;
    }
    @JsonIgnore
    public void setSFCZ(Integer SFCZ) {
        if(SFCZ==null){
            SFCZ = -999;
        }
        this.SFCZ = SFCZ;
    }

    public String getCZSM() {
        return CZSM;
    }
    @JsonIgnore
    public void setCZSM(String CZSM) {
        this.CZSM = CZSM;
    }

    public Integer getSFBHFS() {
        return SFBHFS;
    }
    @JsonIgnore
    public void setSFBHFS(Integer SFBHFS) {
        if(SFBHFS==null){
            SFBHFS = -999;
        }
        this.SFBHFS = SFBHFS;
    }

    public Integer getSFTG() {
        return SFTG;
    }
    @JsonIgnore
    public void setSFTG(Integer SFTG) {
        if(SFTG==null){
            SFTG = -999;
        }
        this.SFTG = SFTG;
    }

    public String getTGKHH() {
        return TGKHH;
    }
    @JsonIgnore
    public void setTGKHH(String TGKHH) {
        this.TGKHH = TGKHH;
    }

    public String getTGZH() {
        return TGZH;
    }
    @JsonIgnore
    public void setTGZH(String TGZH) {
        this.TGZH = TGZH;
    }

    public String getTGMFZFNR() {
        return TGMFZFNR;
    }
    @JsonIgnore
    public void setTGMFZFNR(String TGMFZFNR) {
        this.TGMFZFNR = TGMFZFNR;
    }

    public Integer getZFFS() {
        return ZFFS;
    }
    @JsonIgnore
    public void setZFFS(Integer ZFFS) {
        if(ZFFS==null){
            ZFFS = -999;
        }
        this.ZFFS = ZFFS;
    }

    public String getFKRQ() {
        return FKRQ;
    }
    @JsonIgnore
    public void setFKRQ(String FKRQ) {
        this.FKRQ = FKRQ;
    }

    public String getFQFKRQ1() {
        return FQFKRQ1;
    }
    @JsonIgnore
    public void setFQFKRQ1(String FQFKRQ1) {
        this.FQFKRQ1 = FQFKRQ1;
    }

    public String getFQFKRQ2() {
        return FQFKRQ2;
    }
    @JsonIgnore
    public void setFQFKRQ2(String FQFKRQ2) {
        this.FQFKRQ2 = FQFKRQ2;
    }

    public String getFQFKRQ3() {
        return FQFKRQ3;
    }
    @JsonIgnore
    public void setFQFKRQ3(String FQFKRQ3) {
        this.FQFKRQ3 = FQFKRQ3;
    }

    public Integer getDKFS() {
        return DKFS;
    }
    @JsonIgnore
    public void setDKFS(Integer DKFS) {
        if(DKFS==null){
            DKFS = -999;
        }
        this.DKFS = DKFS;
    }

    public String getSFKRQ() {
        return SFKRQ;
    }
    @JsonIgnore
    public void setSFKRQ(String SFKRQ) {
        this.SFKRQ = SFKRQ;
    }

    public Double getHTJE() {
        return HTJE;
    }
    @JsonIgnore
    public void setHTJE(Double HTJE) {
        if(HTJE==null){
            HTJE = -999d;
        }
        this.HTJE = HTJE;
    }

    public Double getFQFKJE1() {
        return FQFKJE1;
    }
    @JsonIgnore
    public void setFQFKJE1(Double FQFKJE1) {
        if(FQFKJE1==null){
            FQFKJE1 = -999d;
        }
        this.FQFKJE1 = FQFKJE1;
    }

    public Double getFQFKJE2() {
        return FQFKJE2;
    }
    @JsonIgnore
    public void setFQFKJE2(Double FQFKJE2) {
        if(FQFKJE2==null){
            FQFKJE2 = -999d;
        }
        this.FQFKJE2 = FQFKJE2;
    }

    public Double getFQFKJE3() {
        return FQFKJE3;
    }
    @JsonIgnore
    public void setFQFKJE3(Double FQFKJE3) {
        if(FQFKJE3==null){
            FQFKJE3 = -999d;
        }
        this.FQFKJE3 = FQFKJE3;
    }

    public Double getSFKJE() {
        return SFKJE;
    }
    @JsonIgnore
    public void setSFKJE(Double SFKJE) {
        if(SFKJE==null){
            SFKJE = -999d;
        }
        this.SFKJE = SFKJE;
    }

    public String getDKSQRQ() {
        return DKSQRQ;
    }
    @JsonIgnore
    public void setDKSQRQ(String DKSQRQ) {
        this.DKSQRQ = DKSQRQ;
    }

    public String getQTFKNR() {
        return QTFKNR;
    }
    @JsonIgnore
    public void setQTFKNR(String QTFKNR) {
        this.QTFKNR = QTFKNR;
    }

    public String getQTFKMFZF() {
        return QTFKMFZF;
    }
    @JsonIgnore
    public void setQTFKMFZF(String QTFKMFZF) {
        this.QTFKMFZF = QTFKMFZF;
    }

    public Integer getJFCDSF() {
        return JFCDSF;
    }
    @JsonIgnore
    public void setJFCDSF(Integer JFCDSF) {
        if(JFCDSF==null){
            JFCDSF = -999;
        }
        this.JFCDSF = JFCDSF;
    }

    public Integer getYFCDSF() {
        return YFCDSF;
    }
    @JsonIgnore
    public void setYFCDSF(Integer YFCDSF) {
        if(YFCDSF==null){
            YFCDSF = -999;
        }
        this.YFCDSF = YFCDSF;
    }

    public Integer getJFTS() {
        return JFTS;
    }
    @JsonIgnore
    public void setJFTS(Integer JFTS) {
        if(JFTS==null){
            JFTS = -999;
        }
        this.JFTS = JFTS;
    }

    public String getJFRQ() {
        return JFRQ;
    }
    @JsonIgnore
    public void setJFRQ(String JFRQ) {
        this.JFRQ = JFRQ;
    }
}
