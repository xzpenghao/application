package com.springboot.mapper;

import com.springboot.entity.chenbin.personnel.pub_use.Biz_Request_Intercept;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface RequestInterceptMapper {
    Biz_Request_Intercept selectByPrimaryKey(@Param("id") String id);
    Biz_Request_Intercept selectByInputIndex(@Param("businessId")String businessId,@Param("interfaceCode") String interfaceCode);
    Integer insertEntity(Biz_Request_Intercept requestIntercept);
    Integer updateEntity(Biz_Request_Intercept requestIntercept);
    Integer deleteByPrimaryKey(@Param("id") String id);
}
