package com.springboot.popj.json_data;

import com.springboot.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

/**
 * @author chenb
 * @version 2020/5/9/009
 * description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JSONSdqgxx implements Serializable {
    private String infoId;                          //主键
    private String receiptNumber;                   //收件编号-sj_sjsq表主键
    private String serviceCode;                     //服务code
    private String dataJson;                        //原始查询数据
    private String dataComeFromMode;                //数据获取方式
    private String preservationMan;                 //数据提交人
    private String provideUnit;                     //数据提供单位
    private Date insertTime;                      //入库时间

    private String elecOldNo;           //旧电号
    private String elecNewNo;           //新电号
    private String elecCompony;         //电力公司
    private String waterOldNo;          //旧水号
    private String waterNewNo;          //新水号
    private String waterCompony;        //自来水公司
    private String gasOldNo;            //旧气号
    private String gasNewNo;            //新气号
    private String gasCompony;          //天然气煤气公司
    private String tvOldNo;             //广播电视旧户号
    private String tvNewNo;             //广播电视新户号
    private String tvCompony;           //广播电视公司
    private String ext1;                //扩展字段1
    private String ext2;                //扩展字段2
    private String ext3;                //扩展字段3

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) throws ParseException {
        this.insertTime = TimeUtil.getTimeFromString(insertTime);
    }
}
