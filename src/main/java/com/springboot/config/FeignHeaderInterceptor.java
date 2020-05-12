package com.springboot.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import feign.RequestTemplate;

@Slf4j
@Configuration
public class FeignHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("log-feign 请求的地址为："+requestTemplate.url());
        log.info("log-feign 请求的参数为："+requestTemplate.requestBody().asString());
//        System.out.println("out 请求的参数为："+requestTemplate.requestBody().asString());
    }
}