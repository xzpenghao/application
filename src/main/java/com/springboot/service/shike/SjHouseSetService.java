package com.springboot.service.shike;

import com.springboot.entity.SJHouseSet;

import java.util.List;

/**
 * @author sk
 * @version 2020/5/21
 * description：房屋套次 业务层接口
 */
public interface SjHouseSetService {

    /**
     * 描述：根据权利人集合查询房屋套次
     * 作者：sk
     * 日期：2020/5/22
     * 参数：权利人集合 格式为[{"obligeeName":"xxx","obligeeIdType":"身份证","obligeeId":"xxx"}]
     * 返回：房屋套次集合
     * 更新记录：更新人：{}，更新日期：{}
     */
    List<SJHouseSet> queryBdcHouseSetByObligeeList(String obligeeList);
}
