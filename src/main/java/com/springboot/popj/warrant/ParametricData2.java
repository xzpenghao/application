package com.springboot.popj.warrant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/7/29/029
 * description：抵押信息查询条件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametricData2 {
    private String bdcdyh;//不动产单元号
    private String dyzmh;//抵押证明
    private String dyrmc;   //抵押人名称
    private String dyrzjh;  //抵押人证件号
    private String dyqrmc;//抵押权人姓名
    private String dyqrzjh;//抵押权人证件号
}
