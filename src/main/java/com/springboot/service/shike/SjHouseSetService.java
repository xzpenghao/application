package com.springboot.service.shike;

import com.github.wxiaoqi.security.common.msg.BaseResponse;
import com.springboot.entity.SJHouseSet;
import com.springboot.vo.Obligee;

import java.util.List;
import java.util.Map;

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
    List<SJHouseSet> queryBdcHouseSetByObligeeList(List<Obligee> obligeeList);

    /**
     * 描述：税务推送
     * 作者：sk
     * 日期：2020/5/27
     * 参数：受理编号 与 套次集合
     * 返回：推送结果
     * 更新记录：更新人：{}，更新日期：{}
     */
    BaseResponse taxPush(Map<String,String> taxPushMap);
}
