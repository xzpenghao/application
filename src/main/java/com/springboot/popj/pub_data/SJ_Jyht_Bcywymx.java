package com.springboot.popj.pub_data;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author chenb
 * @version 2020/8/27
 * description：交易合同的补充与违约明细类
 */
@Data
@Table(name = "sj_jyht_bcywymx")
public class SJ_Jyht_Bcywymx {
    @Id
    private String id;          //主键
    private String infoId;      //外键
    private String xh;          //序号
    private String mx;          //明细
    private String mxlx;        //明细类型（BC-补充/WY-违约）
    private String ext1;        //扩展字段1
    private String ext2;        //扩展字段2
    private String ext3;        //扩展字段3

    public SJ_Jyht_Bcywymx(){
        super();
    }

    public SJ_Jyht_Bcywymx(String id,String infoId){
        super();
        this.id=id;
        this.infoId=infoId;
    }
}
