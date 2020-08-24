package com.springboot.entity.newPlat.query.resp;

import com.springboot.config.ZtgeoBizException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenb
 * @version 2020/7/24/024
 * description：电子证照信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dzzzxx {
    private String ywbh;            //业务编号
    private String dzzzfjxx;        //电子证照附件信息
    private String zh;              //证（明）号
    private String zhlx;            //证书类型
    private String dzzzwj;          //电子证照文件
    private String djlx;            //登记类型

    /**
     * 描述：返回数据的基础自检方法
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[]
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void checkSelfStandard(){
        if(StringUtils.isBlank(this.ywbh))
            throw new ZtgeoBizException("电子证照接口返回业务号为空");
        if(StringUtils.isBlank(this.zh))
            throw new ZtgeoBizException("电子证照接口返回证号为空");
        if(StringUtils.isBlank(this.zhlx))
            throw new ZtgeoBizException("电子证照接口返回证号类型为空");
    }
}
