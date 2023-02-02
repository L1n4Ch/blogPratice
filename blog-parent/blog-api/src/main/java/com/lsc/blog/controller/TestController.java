package com.lsc.blog.controller;

import com.lsc.blog.dao.pojo.SysUser;
import com.lsc.blog.utils.UserThreadLocal;
import com.lsc.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }

}
