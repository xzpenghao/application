package com.springboot.popj.pub_data;

import com.springboot.config.ZtgeoBizException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author chenb
 * @version 2020/8/4
 * description：收件与不动产业务映映射表
 */
@Data
@Table(name = "sj_sjsq_bdc_mapping")
public class Sj_Sjsq_Bdc_Mapping {
    @Id
    private String id;
    private String sid;
    private String sname;
    private String sqbh;
    private String bdcywlx;
    private String bdcywh;
    private String ext1;
    private String ext2;
    private String ext3;

    public Sj_Sjsq_Bdc_Mapping(){
        super();
    }

    public Sj_Sjsq_Bdc_Mapping(String id, String sqbh, String sid, String bdcywh){
        this.id = id;
        this.sqbh = sqbh;
        this.sid = sid;
        this.bdcywh = bdcywh;
    }

    public void checkSelf(){
        if(StringUtils.isBlank(this.getSqbh()))
            throw new ZtgeoBizException("不动产业务挂接数据申请编号不可为空");
        if(StringUtils.isBlank(this.getSid()))
            throw new ZtgeoBizException("不动产业务挂接数据不动产流程SID不可为空");
    }

    public Sj_Sjsq_Bdc_Mapping onlyExample(){
        Sj_Sjsq_Bdc_Mapping example = new Sj_Sjsq_Bdc_Mapping();
        example.setSqbh(this.getSqbh());
        example.setSid(this.getSid());
        return example;
    }
}
