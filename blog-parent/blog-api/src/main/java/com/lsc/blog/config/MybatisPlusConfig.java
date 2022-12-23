package com.lsc.blog.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.lsc.blog.dao.mapper")
public class MybatisPlusConfig {
    //mybatisPlus配置-分页插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){ //mybatisPlus的拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());  //interceptor拦截器添加分页插件PaginationInnerInterceptor
        return interceptor;
    }
}
