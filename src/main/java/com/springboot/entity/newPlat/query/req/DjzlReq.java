package com.springboot.entity.newPlat.query.req;

import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *   登记资料 查询接口  对接南京新平台 5.1  请求参数  产权查询 适用
 */


@NoArgsConstructor
@Data
public class DjzlReq {

    private String qlrmc;// 权利人名称
    private String qlrzjh; // 权利人证件号
    private String zl; // 坐落
}
