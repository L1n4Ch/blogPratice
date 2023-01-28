package com.lsc.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.lsc.blog.dao.pojo.SysUser;
import com.lsc.blog.service.RegisterService;
import com.lsc.blog.service.SysUserService;
import com.lsc.blog.utils.JWTUtils;
import com.lsc.blog.vo.ErrorCode;
import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.params.RegisterParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String salt = "lsc!@#";

    @Override
    public Result register(RegisterParams registerParams) {

        // 1.判断参数是否合法
        // 2.判断账户是否存在，存在-返回账户已被注册
        // 3.如果账户不存在，不存在-注册用户，生成token
        // 4.存入redis 并返回
        // 5.加上"事务 @Transactional"，一旦中间出现任何问题，注册的用户 需要回滚，不能写进数据库（理论上出现一个接口多个实现类的情况，事务注解应该放在接口处）

        // 步骤1
        String account = registerParams.getAccount();
        String password = registerParams.getPassword();
        String nickname = registerParams.getNickname();
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        // 步骤2
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        // 步骤3
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password + salt));   // 与登录时的密码保持一致
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48co.png");
        sysUser.setAdmin(1);    // true
        sysUser.setDeleted(0);  // false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);
        // 步骤4
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
