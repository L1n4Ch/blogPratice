package com.lsc.blog.service;

import com.lsc.blog.dao.pojo.SysUser;
import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.params.LoginParams;

public interface LoginService {

    /**
     * 登录功能
     * @param loginParams
     * @return
     */
    Result login(LoginParams loginParams);

    /**
     * 校验token合法性
     * @param token
     * @return
     */
    SysUser checkToken(String token);

    /**
     * 退出登录功能
     * @param token
     * @return
     */
    Result logout(String token);
}
