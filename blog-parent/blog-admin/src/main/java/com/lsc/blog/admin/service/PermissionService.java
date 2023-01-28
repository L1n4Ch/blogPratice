package com.lsc.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsc.blog.admin.mapper.PermissionMapper;
import com.lsc.blog.admin.model.params.PageParam;
import com.lsc.blog.admin.pojo.Permission;
import com.lsc.blog.admin.vo.PageResult;
import com.lsc.blog.admin.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 查询权限列表
     * @param pageParam
     * @return
     */
    public Result listPermission(PageParam pageParam) {

        // 返回的数据为后台管理系统中permission表的所有字段 即返回permission类对象
        // 进行分页查询 PageResult返回对象

        Page<Permission> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());

        // 查询
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(pageParam.getQueryString())){
            queryWrapper.eq(Permission::getName, pageParam.getQueryString());
        }

        // 分页
        Page<Permission> permissionPage = permissionMapper.selectPage(page,queryWrapper);
        PageResult<Permission> pageResult = new PageResult<>();
        pageResult.setList(permissionPage.getRecords());
        pageResult.setTotal(permissionPage.getTotal());
        return Result.success(pageResult);

    }

    /**
     * 增加权限
     * @param permission
     * @return
     */
    public Result add(Permission permission) {
        this.permissionMapper.insert(permission);
        return Result.success(null);
    }

    /**
     * 更新权限
     * @param permission
     * @return
     */
    public Result update(Permission permission) {
        this.permissionMapper.updateById(permission);
        return Result.success(null);
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    public Result delete(Long id) {
        this.permissionMapper.deleteById(id);
        return Result.success(null);
    }
}
