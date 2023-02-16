package com.lsc.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsc.blog.admin.mapper.SysUserMapper;
import com.lsc.blog.admin.model.params.PageParam;
import com.lsc.blog.admin.pojo.Article;
import com.lsc.blog.admin.pojo.SysUser;
import com.lsc.blog.admin.vo.PageResult;
import com.lsc.blog.admin.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询用户列表
     * @param pageParam
     * @return
     */
    public Result listArticleBody(PageParam pageParam) {
        Page<SysUser> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(pageParam.getQueryString())){
            queryWrapper.eq(SysUser::getNickname, pageParam.getQueryString());
        }
        Page<SysUser> sysUserPage = sysUserMapper.selectPage(page, queryWrapper);
        PageResult<SysUser> pageResult = new PageResult<>();
        pageResult.setList(sysUserPage.getRecords());
        pageResult.setTotal(sysUserPage.getTotal());
        return Result.success(pageResult);
    }

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    public Result update(SysUser sysUser) {
        this.sysUserMapper.updateById(sysUser);
        return Result.success(null);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    public Result deleteSysUser(Long id) {
        this.sysUserMapper.deleteById(id);
        return Result.success(null);
    }
}
