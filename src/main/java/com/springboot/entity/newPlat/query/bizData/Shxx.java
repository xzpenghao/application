package com.springboot.entity.newPlat.query.bizData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/7/24/024
 * description：审核信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shxx {
    private String djxl;    //登记小类
    private String spyj;    //审批意见
    private String jdsm;    //节点说明
}
