package com.lsc.blog.handler;

import com.lsc.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//对加了Controller注解的方法进行拦截处理-AOP的实现
@ControllerAdvice
public class AllExceptionHandler {
    //进行异常处理，处理Exception.class的异常。
    @ExceptionHandler(Exception.class)
    //加上去返回json数据，控制台preview才会显示"-999 系统异常"
    @ResponseBody
    public Result doException(Exception ex){
        ex.printStackTrace();
        return Result.fail(-999,"系统异常");
    }
}