package com.springboot.entity.newPlat.query.resp;

import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.newPlat.query.bizData.fromSY.bdcdy.Bdcdyxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.bdcdy.QlrSimple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author chenb
 * @version 2020/8/17
 * description：不动产单元信息查询返回实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BdcdyResponse {
    private String cxbh;            //查询编号
    private String bdcqzh;          //不动产权证号
    private String bdczmh;          //不动产证明号
    private String tdzzh;           //土地证证号
    private String sfczyc;          //是否存在异常
    private List<Bdcdyxx> bdcdyxxlb;       //不动产单元信息列表
    private List<QlrSimple> bdccqqlrlb;      //不动产产权权利人列表

    /**
     * 描述：返回数据的基础自检方法
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[]
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void checkSelfStandard(){
        if(StringUtils.isBlank(this.cxbh))
            throw new ZtgeoBizException("查询编号为空");
    }
}
