package com.lsc.blog.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async // 切面 定义了通知和切点的关系
@Slf4j // 记录日志
public class LogAspect {

    // 切点
    @Pointcut("@annotation(com.lsc.blog.common.aop.LogAnnotation)")
    public void pt(){

    }

    // 环绕通知
    @Around("pt()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        // 以下都是记录日志的代码
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = joinPoint.proceed();
        // 执行时长（毫秒）
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
//        recordLog(joinPoint, time);
        return result;
    }



}
