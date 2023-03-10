package com.lsc.blog.config;

import com.lsc.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    // 跨域配置
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // 前端为8080端口，后端为8888端口，跨域配置即解决8080可以访问8888端口的所有接口服务，（/**：即所有接口）
////        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
////        registry.addMapping("/**").allowedOrigins("http://116.205.139.13:8887");
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8887","http://116.205.139.13:8887","blog.cmjlkj.com:8887");
//    }

    // 登录拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish");
    }
}