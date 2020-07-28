package com.springboot.entity.newPlat.query.bizData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/7/24/024
 * description：统计信息详情
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorInfo {

    private String date;    //时间节点
    private Integer value;      //统计量

}
