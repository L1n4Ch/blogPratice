package com.lsc.blog.service;

import com.lsc.blog.vo.CategoryVo;

public interface CategoryService {
    /**
     * 根据分类Id获得分类
     * @param categoryId
     * @return
     */
    CategoryVo findCategoryById(Long categoryId);
}
