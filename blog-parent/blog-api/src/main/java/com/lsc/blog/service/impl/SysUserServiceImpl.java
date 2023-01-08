package com.lsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lsc.blog.dao.mapper.SysUserMapper;
import com.lsc.blog.dao.pojo.SysUser;
import com.lsc.blog.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

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
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

}
