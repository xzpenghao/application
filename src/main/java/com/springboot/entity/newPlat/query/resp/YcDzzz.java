package com.springboot.entity.newPlat.query.resp;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/8/21
 * description：一窗的电子证照实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YcDzzz extends Dzzzxx {
    private String sqbh;
    private String bdcywlx;          //不动产业务类型分类（10种）
    private byte[] zznr;             //证照内容，字节数组
    private String fjlj;             //生成附件路径
    private String saveType;         //保存方式

    public YcDzzz initByParent(Dzzzxx dzzzxx){
        return JSONObject.parseObject(JSONObject.toJSONString(dzzzxx),YcDzzz.class);
    }
}
