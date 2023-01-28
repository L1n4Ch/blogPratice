package com.lsc.blog.service;

import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.params.RegisterParams;

public interface RegisterService {

    /**
     * 注册功能
     * @param registerParams
     * @return
     */
    Result register(RegisterParams registerParams);
}
