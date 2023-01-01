package com.lsc.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.lsc.blog.dao.pojo.SysUser;
import com.lsc.blog.service.LoginService;
import com.lsc.blog.service.SysUserService;
import com.lsc.blog.utils.JWTUtils;
import com.lsc.blog.vo.ErrorCode;
import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.params.LoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //加密盐
    private static final String salt = "lsc!@#";

    @Override
    public Result login(LoginParams loginParams) {
        /**
         * 1.检查参数是否合法
         * 2.根据用户名和密码去user表中查询是否存在，如果不存在，则登陆失败，如果存在，使用JWT生成token，返回给前端
         * 3.将token放入redis中，redis存放token/user表信息，并给redis设置过期时间（登陆认证时，先认证token字符串是否合法，再去redis中验证是否妇女在）
         */
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            //使用枚举类统一包装错误玛
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + salt);
        SysUser sysUser = sysUserService.findUser(account, password);
        if(sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

}
