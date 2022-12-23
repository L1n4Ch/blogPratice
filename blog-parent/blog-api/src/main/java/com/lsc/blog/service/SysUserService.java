package com.lsc.blog.service;

import com.lsc.blog.dao.pojo.SysUser;

public interface SysUserService {
    //这里的SysUser暂时没做VO对象，跟TagService不同
    SysUser findUserById(Long id);
}
