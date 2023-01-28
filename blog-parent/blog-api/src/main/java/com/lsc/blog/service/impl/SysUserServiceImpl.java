package com.lsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lsc.blog.dao.mapper.SysUserMapper;
import com.lsc.blog.dao.pojo.SysUser;
import com.lsc.blog.service.LoginService;
import com.lsc.blog.service.SysUserService;
import com.lsc.blog.vo.ErrorCode;
import com.lsc.blog.vo.LoginUserVo;
import com.lsc.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private LoginService loginService;

    @Override
    public SysUser findUserById(Long id) {
        // selectById：根据主键id进行查询记录的。返回一条记录。（这里的id即SysUser表中的id，等价于authorId，在这里作者=用户，所以直接使用selectById查询）
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null){
            // 如果不new，直接sysUser.setNickname()，可能会导致空指针异常。
            SysUser user = new SysUser();
            user.setNickname("游客");
        }
        // 建议不直接 return sysUserMapper.selectById(id); 为了避免id为空的情况，前面加一层可能为空的判断
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        // 选择查询出来的用户的所需信息
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");
        // selectOne：mybatis中接受不了数据结果的方法，接受单条数据。接受多条数据结果的方法为selectList
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {

        // 1.token合法性校验。（token是否为空，token解析是否成功，redis是否存在）
        // 2.如果校验失败 返回错误
        // 3.如果成功，返回对应的结果 （创建一个LoginUserVo对象）

        // 步骤1（在checkToken方法里）
        SysUser sysUser = loginService.checkToken(token);   // token解析成功，返回user信息
        // 步骤2
        if(sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        // 步骤3
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickName(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setAccount(sysUser.getAccount());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        // 注意：保存用户时，id会自动生成，默认生成的id是分布式id（雪花算法）
        // mybatisPlus 默认使用分布式id（如果要改成自增id，在SysUser类的id上加注解：@TableId(type = IdType.AUTO)）
        this.sysUserMapper.insert(sysUser);
    }
}
