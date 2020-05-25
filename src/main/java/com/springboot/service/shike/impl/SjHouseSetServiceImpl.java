package com.springboot.service.shike.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJHouseSet;
import com.springboot.service.shike.SjHouseSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sk
 * @version 2020/5/21
 * description：房屋套次 业务层实现
 */
@Slf4j
@Service
public class SjHouseSetServiceImpl implements SjHouseSetService {

    @Value("${HouseSet.Bdc}")
    private String houseSetBdc;

    /**
     * 描述：根据权利人集合查询房屋套次
     * 作者：sk
     * 日期：2020/5/22
     * 参数：权利人集合 格式为[{"obligeeName":"xxx","obligeeIdType":"身份证","obligeeId":"xxx"}]
     * 返回：房屋套次集合
     * 更新记录：更新人：{}，更新日期：{}
    */
    @Override
    public List<SJHouseSet> queryBdcHouseSetByObligeeList(String obligeeList){
        //1.    参数合法性验证
        if (StrUtil.isBlank(obligeeList)){
            throw new ZtgeoBizException("参数为空");
        }
        //2.    组织参数，发送请求
        Map<String,Object> params = new HashMap<>();
        //2.1   String转集合
        params.put("familyMemberList",JSONArray.parseArray(obligeeList));
        //2.2   参数格式化
        String formatParams = JSON.toJSONString(JSONObject.toJSON(params));
        log.info("格式化后的参数",formatParams);
        String result = "";
        //2.3   发送请求 捕获异常
        try {
            result = HttpUtil.post(houseSetBdc,formatParams);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("不动产套次查询失败，权利人集合：{}",obligeeList);
            throw new ZtgeoBizException("不动产套次查询失败");
        }
        //3.    组织并返回结果
        List<SJHouseSet> sjHouseSets = new ArrayList<>();
        if (StrUtil.isBlank(result)){
            return sjHouseSets;
        }
        //3.1   获取指定参数
        JSONArray jsonArray = JSON.parseObject(result).getJSONArray("data");
        //3.2   不为空时遍历参数并将权利人集合格式化为字符串
        if (CollUtil.isNotEmpty(jsonArray)){
            for (Object data : jsonArray) {
                SJHouseSet sjHouseSet = JSON.parseObject(JSON.toJSONString(data),SJHouseSet.class);
                sjHouseSet.setObligeeInfoVoList(JSON.toJSONString(sjHouseSet.getObligeeInfoVoList()));
                sjHouseSets.add(sjHouseSet);
            }
        }
        //3.3   为空直接返回
        return sjHouseSets;
    }
}
