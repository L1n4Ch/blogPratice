package com.lsc.blog.handler;

import com.alibaba.fastjson.JSON;
import com.lsc.blog.dao.pojo.SysUser;
import com.lsc.blog.service.LoginService;
import com.lsc.blog.utils.UserThreadLocal;
import com.lsc.blog.vo.ErrorCode;
import com.lsc.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 登录拦截器
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 原理：在执行controller方法（Handler）之前进行拦截
        // 1.需要判断 请求的接口路径是否为HandlerMethod（即controller方法）
        // 2.判断token是否为空 如果为空 未登录
        // 3.如果token不为空 登录验证 loginService checkToken
        // 4.如果认证成功 放行

        // 步骤1
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        // 步骤2
        String token = request.getHeader("Authorization");
        // 这里插入控制台日志记录
        log.info("================request start==============");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}",token);
        log.info("================request end================");
        if(StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        // 步骤3
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        // 举例：在controller中拉取用户信息 （在testController中模拟使用实现功能，在CommentsServiceImpl实现类的评论列表方法中也用到）
        UserThreadLocal.put(sysUser);
        // 步骤4
        return true;
    }

    // 拉取完用户信息后，如果不删除 ThreadLocal中用完的用户信息 会有内存泄露的风险
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }

}
