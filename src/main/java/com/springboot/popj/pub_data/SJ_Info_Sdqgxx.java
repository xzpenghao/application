package com.springboot.popj.pub_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * @author chenb
 * @version 2020/5/9/009
 * description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SJ_Info_Sdqgxx extends SJ_Information {
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
}
