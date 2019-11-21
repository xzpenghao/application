package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class HTXX implements Serializable {
    @JsonProperty("hTJE")
    private String HTJE;
    @JsonProperty("fSSS")
    private String FSSS;
    @JsonProperty("sFCZ")
    private Integer SFCZ;
    @JsonProperty("cZSM")
    private String CZSM;
    @JsonProperty("sFBHFS")
    private Integer SFBHFS;		//是否包含附属设施 1 包含，0 不包含
    @JsonProperty("sFTG")
    private Integer SFTG;			//是否资金托管 1 是，0 否
    @JsonProperty("tGKHH")
    private String TGKHH;			//资金托管开户行
    @JsonProperty("tGZH")
    private String TGZH;			//托管账户
    @JsonProperty("tGMFZFNR")
    private String TGMFZFNR;		//买方支付内容
    @JsonProperty("zFFS")
    private Integer ZFFS;			//支付方式 1 一次性付款，2 分期付款，3 贷款付款，4 其它付款方式
    @JsonProperty("fKRQ")
    private String FKRQ;			//付款日期（付款方式2）
    @JsonProperty("fQFKRQ1")
    private String FQFKRQ1;		//分期付款日期1（付款方式2）
    @JsonProperty("fQFKJE1")
    private String FQFKJE1;		//分期付款金额1（付款方式2）
    @JsonProperty("fQFKRQ2")
    private String FQFKRQ2;		//分期付款日期2（付款方式2）
    @JsonProperty("fQFKJE2")
    private String FQFKJE2;		//分期付款金额2（付款方式2）
    @JsonProperty("fQFKRQ3")
    private String FQFKRQ3;		//分期付款日期3（付款方式2）
    @JsonProperty("fQFKJE3")
    private String FQFKJE3;		//分期付款金额3（付款方式2）
    @JsonProperty("dKFS")
    private Integer DKFS;			 //贷款方式，1 银行按揭，2 公积金贷款（付款方式3）
    @JsonProperty("sFKRQ")
    private String SFKRQ;			//首付款日期（付款方式3）
    @JsonProperty("sFKJE")
    private String SFKJE;			 //首付款金额（付款方式3）
    @JsonProperty("dKSQRQ")
    private String DKSQRQ;			 //贷款申请日期（付款方式3）
    @JsonProperty("qTFKNR")
    private String QTFKNR;			 //其它付款内容（付款方式4）
    @JsonProperty("qTFKMFZF")
    private String QTFKMFZF;			 //其它付款-买方支付（付款方式4）
    @JsonProperty("jFCDSF")
    private Integer JFCDSF;			 //买方承担税费情况，1 独自承担（此时YFCDSF为空），2 承担各自部分
    @JsonProperty("yFCDSF")
    private Integer YFCDSF;			 //卖方承担税费情况，1 独自承担（此时JFCDSF为空），2 承担各自部分
    @JsonProperty("jFTS")
    private Integer JFTS;			 //交付天数
    @JsonProperty("jFRQ")
    private String JFRQ;			  //交付日期

    public String getHTJE() {
        return HTJE;
    }

    public void setHTJE(String HTJE) {
        this.HTJE = HTJE;
    }

    public String getFSSS() {
        return FSSS;
    }

    public void setFSSS(String FSSS) {
        this.FSSS = FSSS;
    }

    public Integer getSFCZ() {
        return SFCZ;
    }

    public void setSFCZ(Integer SFCZ) {
        this.SFCZ = SFCZ;
    }

    public String getCZSM() {
        return CZSM;
    }

    public void setCZSM(String CZSM) {
        this.CZSM = CZSM;
    }

    public Integer getSFBHFS() {
        return SFBHFS;
    }

    public void setSFBHFS(Integer SFBHFS) {
        this.SFBHFS = SFBHFS;
    }

    public Integer getSFTG() {
        return SFTG;
    }

    public void setSFTG(Integer SFTG) {
        this.SFTG = SFTG;
    }

    public String getTGKHH() {
        return TGKHH;
    }

    public void setTGKHH(String TGKHH) {
        this.TGKHH = TGKHH;
    }

    public String getTGZH() {
        return TGZH;
    }

    public void setTGZH(String TGZH) {
        this.TGZH = TGZH;
    }

    public String getTGMFZFNR() {
        return TGMFZFNR;
    }

    public void setTGMFZFNR(String TGMFZFNR) {
        this.TGMFZFNR = TGMFZFNR;
    }

    public Integer getZFFS() {
        return ZFFS;
    }

    public void setZFFS(Integer ZFFS) {
        this.ZFFS = ZFFS;
    }

    public String getFKRQ() {
        return FKRQ;
    }

    public void setFKRQ(String FKRQ) {
        this.FKRQ = FKRQ;
    }

    public String getFQFKRQ1() {
        return FQFKRQ1;
    }

    public void setFQFKRQ1(String FQFKRQ1) {
        this.FQFKRQ1 = FQFKRQ1;
    }

    public String getFQFKJE1() {
        return FQFKJE1;
    }

    public void setFQFKJE1(String FQFKJE1) {
        this.FQFKJE1 = FQFKJE1;
    }

    public String getFQFKRQ2() {
        return FQFKRQ2;
    }

    public void setFQFKRQ2(String FQFKRQ2) {
        this.FQFKRQ2 = FQFKRQ2;
    }

    public String getFQFKJE2() {
        return FQFKJE2;
    }

    public void setFQFKJE2(String FQFKJE2) {
        this.FQFKJE2 = FQFKJE2;
    }

    public String getFQFKRQ3() {
        return FQFKRQ3;
    }

    public void setFQFKRQ3(String FQFKRQ3) {
        this.FQFKRQ3 = FQFKRQ3;
    }

    public String getFQFKJE3() {
        return FQFKJE3;
    }

    public void setFQFKJE3(String FQFKJE3) {
        this.FQFKJE3 = FQFKJE3;
    }

    public Integer getDKFS() {
        return DKFS;
    }

    public void setDKFS(Integer DKFS) {
        this.DKFS = DKFS;
    }

    public String getSFKRQ() {
        return SFKRQ;
    }

    public void setSFKRQ(String SFKRQ) {
        this.SFKRQ = SFKRQ;
    }

    public String getSFKJE() {
        return SFKJE;
    }

    public void setSFKJE(String SFKJE) {
        this.SFKJE = SFKJE;
    }

    public String getDKSQRQ() {
        return DKSQRQ;
    }

    public void setDKSQRQ(String DKSQRQ) {
        this.DKSQRQ = DKSQRQ;
    }

    public String getQTFKNR() {
        return QTFKNR;
    }

    public void setQTFKNR(String QTFKNR) {
        this.QTFKNR = QTFKNR;
    }

    public String getQTFKMFZF() {
        return QTFKMFZF;
    }

    public void setQTFKMFZF(String QTFKMFZF) {
        this.QTFKMFZF = QTFKMFZF;
    }

    public Integer getJFCDSF() {
        return JFCDSF;
    }

    public void setJFCDSF(Integer JFCDSF) {
        this.JFCDSF = JFCDSF;
    }

    public Integer getYFCDSF() {
        return YFCDSF;
    }

    public void setYFCDSF(Integer YFCDSF) {
        this.YFCDSF = YFCDSF;
    }

    public Integer getJFTS() {
        return JFTS;
    }

    public void setJFTS(Integer JFTS) {
        this.JFTS = JFTS;
    }

    public String getJFRQ() {
        return JFRQ;
    }

    public void setJFRQ(String JFRQ) {
        this.JFRQ = JFRQ;
    }
}
