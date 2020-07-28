package com.springboot.entity.newPlat.query.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/7/24/024
 * description：电子证照信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dzzzxx {
    private String ywbh;            //业务编号
    private String dzzzfjxx;        //电子证照附件信息
    private String zh;              //证（明）号
    private String zslx;            //证书类型
    private String dzzzwj;          //电子证照文件
}
