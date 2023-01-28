package com.lsc.blog.controller;

import com.lsc.blog.service.RegisterService;
import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.params.RegisterParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping
    public Result register(@RequestBody RegisterParams registerParams){
        return registerService.register(registerParams);
    }

}
