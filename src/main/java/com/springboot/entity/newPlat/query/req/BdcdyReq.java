package com.springboot.entity.newPlat.query.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/8/17
 * description：不动产单元查询接口
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BdcdyReq {
    private String cxbh;                //查询编号
    private String bdcdyh;              //不动产单元号
    private String qlrmc;               //权利人名称
    private String qlrzjlx;             //权利人证件类型
    private String qlrzjhm;             //权利人证件号码
    private String zh;                  //幢号
    private String fjh;                 //房间号
    private String dyh;                 //单元号

    public BdcdyReq initDyReq(String cxbh,String bdcdyh){
        this.cxbh = cxbh;
        this.bdcdyh = bdcdyh;
        return this;
    }
}
