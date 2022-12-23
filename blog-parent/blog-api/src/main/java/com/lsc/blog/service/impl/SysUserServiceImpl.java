package com.lsc.blog.service.impl;

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
        //这里并不需要像Tag一样进行多表查询，通过mybatisPlus自带的查询id功能查询
        //这里建议不直接返回 sysUserMapper.selectById(id); 为了避免id为空的情况，加一层判度胺
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null){
            //如果不new，直接sysUser.setNickname()，可能会导致空指针异常
                //SysUser user = new SysUser();
                //user.setNickname("游客");
            new SysUser().setNickname("游客");
        }
        return sysUser;
    }
}
