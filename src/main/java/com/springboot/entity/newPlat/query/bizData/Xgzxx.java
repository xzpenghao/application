package com.springboot.entity.newPlat.query.bizData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author chenb
 * @version 2020/7/27/027
 * description：相关证信息（查询补充内容）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Xgzxx {
    private String xgzh;            //相关证号
    private String zszmlx;          //证书证明类型
    private String ywh;             //业务号
    private String bdcdyh;          //不动产单元号
    private List<?> qlrlb;          //权利人列表
}
