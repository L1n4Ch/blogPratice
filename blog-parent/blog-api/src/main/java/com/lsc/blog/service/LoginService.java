package com.lsc.blog.service;

import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.params.LoginParams;

public interface LoginService {

    /**
     * 登陆功能
     * @param loginParams
     * @return
     */
    Result login(LoginParams loginParams);

}
