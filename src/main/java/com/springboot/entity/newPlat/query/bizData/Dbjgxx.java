package com.springboot.entity.newPlat.query.bizData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/7/24/024
 * description：登簿结果信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dbjgxx {
    private String bdczh;       //不动产证号
    private String zslx;        //证书类型
    private String djxl;        //登记小类
    private String djrq;        //登记日期
}
