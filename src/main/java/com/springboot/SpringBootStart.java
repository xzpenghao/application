package com.springboot;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.springboot.util.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableFeignClients({"com.springboot.feign"})
@Component("com.springboot")
@MapperScan({"com.springboot.mapper"})
public class SpringBootStart {


    public static  void main(String [] args){
        ApplicationContext app =  SpringApplication.run(SpringBootStart.class, args);
        //启动时将ApplicationContext对象作为资源注入
        SpringUtil.setApplicationContext(app);
    }
}
