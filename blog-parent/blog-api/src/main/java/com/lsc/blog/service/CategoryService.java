package com.lsc.blog.service;

import com.lsc.blog.vo.CategoryVo;
import com.lsc.blog.vo.Result;

public interface CategoryService {
    /**
     * 根据分类Id获得分类
     * @param categoryId
     * @return
     */
    CategoryVo findCategoryById(Long categoryId);

    /**
     * 写文章-查询所有文章分类
     * @return
     */
    Result findAll();
}
