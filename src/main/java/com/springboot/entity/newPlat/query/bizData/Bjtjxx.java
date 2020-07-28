package com.springboot.entity.newPlat.query.bizData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author chenb
 * @version 2020/7/24/024
 * description：办件统计信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bjtjxx {

    private List<MonitorInfo> dayMonitor;       //日统计信息
    private List<MonitorInfo> weekMonitor;      //周统计信息
}
