package com.springboot.service.shike.impl;

import cn.hutool.http.HttpUtil;
import com.github.wxiaoqi.security.common.exception.base.BusinessException;
import com.springboot.service.shike.PublicInspectService;
import com.springboot.vo.JudicialQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service("publicInspectService")
public class PublicInspectServiceImpl implements PublicInspectService {

    @Value("${PublicInspect.BDCZHURL}")
    private String BDCZHURL;

    @Value("${PublicInspect.DYZMHURL}")
    private String DYZMHURL;

    @Value("${PublicInspect.YGZMHURL}")
    private String YGZMHURL;

    /**
     * 证号/证书号查询
     * @param bdczh
     * @param type
     * @return
     */
    @Override
    public JudicialQueryVo JudicialQuery(String bdczh, String type) {
        if (StringUtils.isBlank(bdczh)||StringUtils.isBlank(type)){
            throw new BusinessException("参数格式错误");
        }
        log.info("查询的不动产单元号:{},查询类型:{}",bdczh,type);
        JudicialQueryVo judicialQueryVo = new JudicialQueryVo();

        switch (type){
            case "1":
                //不动产证号查询
                String bdczhResult= HttpUtil.get(String.format(BDCZHURL,bdczh));
                log.info("不动产证号查询结果:{}",bdczhResult);
                if (StringUtils.isBlank(bdczhResult)||"[]".equals(bdczhResult)){
                    throw new BusinessException("未查询到信息");
                }else {
                    judicialQueryVo = judicialQueryVo.genJudicialQuery(bdczhResult,judicialQueryVo,type);
                }
                break;
            case "2":
                //抵押证明号查询
                String dyzmhResult= HttpUtil.get(String.format(DYZMHURL,bdczh));
                log.info("抵押证明号查询结果:{}",dyzmhResult);
                if (StringUtils.isBlank(dyzmhResult)||"[]".equals(dyzmhResult)){
                    throw new BusinessException("未查询到信息");
                }else {
                    judicialQueryVo = judicialQueryVo.genJudicialQuery(dyzmhResult,judicialQueryVo,type);
                }
                break;
            case "3":
                //预告证明号查询
                String ygzmhResult= HttpUtil.get(String.format(YGZMHURL,bdczh));
                log.info("预告证明号查询结果:{}",ygzmhResult);
                if (StringUtils.isBlank(ygzmhResult)||"[]".equals(ygzmhResult)){
                    throw new BusinessException("未查询到信息");
                }else {
                    judicialQueryVo = judicialQueryVo.genJudicialQuery(ygzmhResult,judicialQueryVo,type);
                }
                break;
        }
        return judicialQueryVo;
    }
}
