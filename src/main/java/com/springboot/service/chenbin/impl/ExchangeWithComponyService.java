package com.springboot.service.chenbin.impl;

import com.springboot.entity.chenbin.personnel.req.DLReqEntity;
import com.springboot.entity.chenbin.personnel.resp.DLReturnUnitEntity;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.feign.OuterBackFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenb
 * @version 2020/4/29/029
 * description：
 */
@Slf4j
@Service("exc2Comp")
public class ExchangeWithComponyService {
    @Autowired
    private ExchangeWithOtherFeign otherFeign;
    @Autowired
    private OuterBackFeign backFeign;

    public OtherResponseEntity<DLReturnUnitEntity> exchangeWithPowerCompany(DLReqEntity dlcs){
        return otherFeign.sendPowerCompany(dlcs);
    }
}
