package com.springboot.entity.newPlat.transInner.req.fromZY.domain;

import com.springboot.config.ZtgeoBizException;
import com.springboot.util.newPlatBizUtil.DicConvertUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewBdcFlowRespData {
    private String sid;
    private String ywlx;
    private String ywh;

    /**
     * 描述：返回数据的基础自检方法
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[]
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void checkSelfStandard(){
        if(StringUtils.isBlank(this.sid))
            throw new ZtgeoBizException("转内网接口创建返回sid为空");
        if(StringUtils.isBlank(this.ywh))
            throw new ZtgeoBizException("转内网接口创建返回ywh为空");
        if(StringUtils.isBlank(this.ywlx))
            throw new ZtgeoBizException("转内网接口创建返回ywlx为空");
        if(StringUtils.isBlank(DicConvertUtil.getKeyCodeByWord(this.ywlx)))
            throw new ZtgeoBizException("转内网接口创建返回ywlx不在规范范围内");
    }
}
