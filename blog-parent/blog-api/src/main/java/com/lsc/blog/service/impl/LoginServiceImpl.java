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

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 加密盐
    private static final String salt = "lsc!@#";

    @Override
    public Result login(LoginParams loginParams) {
        // 1.检查参数是否合法
        // 2.根据用户名和密码去user表中查询是否存在，如果不存在，则登陆失败，如果存在，使用JWT生成token，返回给前端（return）
        // 3.将token放入redis中，redis存放token/user表信息，并给redis设置过期时间（登陆认证时，先认证token字符串是否合法，再去redis中验证是否存在）
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        // 步骤1
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            // 使用枚举类统一包装错误玛
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        // 处理密码加密
        password = DigestUtils.md5Hex(password + salt);
        // 步骤2
        SysUser sysUser = sysUserService.findUser(account, password);
        if(sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        // 步骤2-1 登陆成功，使用JWT生成token
        String token = JWTUtils.createToken(sysUser.getId());
        // 步骤3 token放入redis中，redis存放token/user表信息（将对象转成JSON形式存放在redis中）
            // JSON.toJSONString 是将对象转化为Json字符串
            // JSON.parseObject 是将Json字符串转化为相应的对象；
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        // 对应SysUserServiceImpl里的步骤1-1：token解析是否为空
        if(StringUtils.isBlank(token)){
            return null;
        }
        // 对应SysUserServiceImpl里的步骤1-2：token解析是否成功
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap == null){
            return null;
        }
        // 对应SysUserServiceImpl里的步骤1-3：redis是否存在
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        // 对应SysUserServiceImpl里的步骤1-3：redis是否存在
        if(StringUtils.isBlank(userJson)){
            return null;
        }
        // token合法性校验成果：将user信息（json字符串）转换为对象（sysUser.class）返回结果
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    @Override
    public Result logout(String token) {
        // 删除redis中的token即退出登录
        redisTemplate.delete("TOKEN_" + token);
        // 文档中的数据声明：退出登录后返回的data数据为null
        return Result.success(null);
    }

}
