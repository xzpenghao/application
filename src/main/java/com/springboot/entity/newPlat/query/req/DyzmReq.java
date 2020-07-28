package com.springboot.entity.newPlat.query.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/7/27/027
 * description：抵押证明信息查询的条件参数实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DyzmReq {
    private String dyzmh;
    private String dyr;
    private String bdcdyh;
    private String xgzh;
}
