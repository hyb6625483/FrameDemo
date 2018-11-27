package com.mybatis.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * SpringBoot + MyBatis + Generator反向工程 + JSP
 */
@SpringBootApplication
@MapperScan("com.mybatis.demo.mapper")
public class MyBatisApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(MyBatisApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringApplicationBuilder.class);
    }
}
