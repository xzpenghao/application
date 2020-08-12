package com.springboot.entity.chenbin.personnel.other.paph;

import com.springboot.emm.DIC_BDC_DYFS_Enums;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Dyxx;
import com.springboot.util.newPlatBizUtil.DicConvertUtil;
import com.springboot.util.newPlatBizUtil.ResultConvertUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaphDyxx implements Serializable {
    private String dyqr;
    private String zqje;
    private String zwlxqx;//债务履行期限
    private String dylx;

    public String getDyqr() {
        return dyqr;
    }

    public void setDyqr(String dyqr) {
        this.dyqr = dyqr;
    }

    public String getZqje() {
        return zqje;
    }

    public void setZqje(String zqje) {
        this.zqje = zqje;
    }

    public String getZwlxqx() {
        return zwlxqx;
    }

    public void setZwlxqx(String zwlxqx) {
        this.zwlxqx = zwlxqx;
    }

    public String getDylx() {
        return dylx;
    }

    public void setDylx(String dylx) {
        this.dylx = dylx;
    }

    public PaphDyxx initByDjzlDy(Dyxx dyxx){
        this.zqje = StringUtils.isBlank(dyxx.getZqje())?dyxx.getDyje():dyxx.getZqje();
        if(StringUtils.isNotBlank(this.zqje))
            this.zqje = ResultConvertUtil.getStringFromBigDecimalNotThrowNull(
                    new BigDecimal(Double.parseDouble(this.zqje)*10000),
                    "#.00"
            );
        this.dylx = StringUtils.isBlank(dyxx.getDylx())?dyxx.getDyfs():dyxx.getDylx();
        if(StringUtils.isNotBlank(this.dylx))
            this.dylx = DicConvertUtil.getDicNameByVal(this.dylx , DIC_BDC_DYFS_Enums.values());
        this.zwlxqx = ResultConvertUtil.creatZwlxqx(dyxx);
        return this;
    }
}
