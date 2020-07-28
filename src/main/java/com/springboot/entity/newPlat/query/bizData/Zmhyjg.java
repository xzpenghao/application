package com.springboot.entity.newPlat.query.bizData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/7/24/024
 * description：证明核验结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zmhyjg {
    private String zl;              //坐落
    private String bdczmh;          //不动产证明号
    private String bdcdyh;          //不动产单元号
    private String djlx;            //登记类型
    private String qlr;             //权利人
    private String ywr;             //义务人
    private String qt;              //其他
    private String djrq;            //登记日期
    private String zsxlh;           //证书序列号
    private String lifecycle;       //证书状态
    private String fj;              //附记
}
