package com.lsc.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    //跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //前端为8080端口，后端为8888端口，跨域配置即解决8080可以访问8888端口的所有接口服务，（/**：即所有接口）
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }
}