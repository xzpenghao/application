package com.springboot.service.newPlat.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.entity.newPlat.query.req.CqzsReq;
import com.springboot.entity.newPlat.query.resp.CqzsResponse;
import com.springboot.feign.newPlat.BdcQueryFeign;
import com.springboot.popj.pub_data.SJ_Info_Bdcqlxgxx;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.util.newPlatBizUtil.ResultConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.*;

/**
 * @author chenb
 * @version 2020/7/27/027
 * description：不动产查询服务
 */
@Slf4j
@Service
public class BdcQueryService {
    @Autowired
    private BdcQueryFeign bdcQueryFeign;

    /**
     * 描述：不动产权属信息查询，带他项权获取
     * 作者：chenb
     * 日期：2020/7/27/027
     * 参数：[parametricData]
     * 返回：[SJ_Info_Bdcqlxgxx]
     * 更新记录：更新人：{}，更新日期：{}
     */
    public List<SJ_Info_Bdcqlxgxx> queryQzxxWithItsRights(ParametricData parametricData) throws ParseException {
        //1. 定义返回结果集
        List<SJ_Info_Bdcqlxgxx> serviceDataInfos = new ArrayList<>();
        //2. 初始化查询条件
        CqzsReq cqzsReq = new CqzsReq().initByYCParams(parametricData);
        //3. 执行查询操作
        OtherResponseEntity<List<CqzsResponse>> cxjg = bdcQueryFeign.cqzscx(cqzsReq);
        //4. 解析查询的结果数据
        if(cqzsReq!=null){
            //4.1 记录查询得原始结果
            log.debug("【产权证书查询】--> 不动产接口执行结果（原始结果）："+ JSONObject.toJSONString(cxjg));
            //4.2 解析结果状态
            if(BDC_INTF_HANDLE_RETURN_CODE_SUCCESS.equals(cxjg.getCode())){
                //4.2.1 查询结果转换为一窗准入数据
                List<CqzsResponse> cqzss = cxjg.getData();
                if(cqzss!=null && cqzss.size()>0){
                    for(CqzsResponse cqzs:cqzss){
                        SJ_Info_Bdcqlxgxx sjInfoBdcqlxgxx = ResultConvertUtil.getImmoRightsByCqzs(cqzs);
                        if(sjInfoBdcqlxgxx!=null)
                            serviceDataInfos.add(sjInfoBdcqlxgxx);
                    }
                }
            } else if(BDC_INTF_HANDLE_RETURN_CODE_UNSUCCESS.equals(cxjg.getCode())){
                throw new ZtgeoBizException(cxjg.getMsg());
            }else{
                throw new ZtgeoBizException("返回的接口响应CODE不符合规范，取值范围是[0,1],当前响应：【"+cxjg.getCode()+"】");
            }
        }else{
            log.warn("【产权证书查询】--> 不动产权证查询，返回了null值，这在设计上是超出规范的，请知悉");
        }
        //5. 返回结果集
        return serviceDataInfos;
    }
}
