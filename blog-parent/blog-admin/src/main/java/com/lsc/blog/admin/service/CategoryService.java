package com.lsc.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsc.blog.admin.mapper.CategoryMapper;
import com.lsc.blog.admin.model.params.PageParam;
import com.lsc.blog.admin.pojo.Category;
import com.lsc.blog.admin.pojo.Permission;
import com.lsc.blog.admin.vo.PageResult;
import com.lsc.blog.admin.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询分类列表
     * @param pageParam
     * @return
     */
    public Result listCategory(PageParam pageParam) {

        Page<Category> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(pageParam.getQueryString())){
            queryWrapper.eq(Category::getCategoryName, pageParam.getQueryString());
        }
        Page<Category> categoryPage = categoryMapper.selectPage(page,queryWrapper);
        PageResult<Category> pageResult = new PageResult<>();
        pageResult.setList(categoryPage.getRecords());
        pageResult.setTotal(categoryPage.getTotal());
        return Result.success(pageResult);

    }

    /**
     * 更新分类
     * @param category
     * @return
     */
    public Result update(Category category) {
        this.categoryMapper.updateById(category);
        return Result.success(null);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    public Result deleteCategory(Long id) {
        this.categoryMapper.deleteById(id);
        return Result.success(null);
    }

    /**
     * 新增分类
     * @param category
     * @return
     */
    public Result add(Category category) {
        this.categoryMapper.insert(category);
        return Result.success(null);
    }
}
