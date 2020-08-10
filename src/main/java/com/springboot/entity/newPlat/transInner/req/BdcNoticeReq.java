package com.springboot.entity.newPlat.transInner.req;

import com.springboot.config.ZtgeoBizException;
import com.springboot.util.newPlatBizUtil.DicConvertUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author chenb
 * @version 2020/7/30/030
 * description：不动产节点通知body
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BdcNoticeReq {
    private String jdbs;            //节点标识（受理通知acceptNotice & 审核通知verifyNotice & 登簿通知resultNotice & 废弃通知 ）
    private List<String> ywhlb;     //业务列表
    private String wsywh;           //网申业务号（这里是一窗的收件申请编号）

    /**
     * 描述：检查自身数据是否满足标准
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：void
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
    */
    public void checkSelfStandard() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if(StringUtils.isBlank(this.getJdbs())){
            throw new ZtgeoBizException("通知失败，参数校验失败，传入节点标识为空");
        }
        if(StringUtils.isBlank(DicConvertUtil.getKeyWordByCode(this.getJdbs()))){
            throw new ZtgeoBizException("通知失败，参数校验失败，传入节点标识超出定义");
        }
        if(StringUtils.isBlank(this.wsywh)){
            throw new ZtgeoBizException("通知失败，参数校验失败，传入网申业务号为空");
        }
        if(this.ywhlb==null || this.ywhlb.size()<1){
            throw new ZtgeoBizException("通知失败，参数校验失败，未传入业务号列表或列表长度为空");
        }
    }
}
