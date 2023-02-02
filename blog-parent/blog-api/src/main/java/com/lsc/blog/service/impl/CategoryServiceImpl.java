package com.lsc.blog.service.impl;

import com.lsc.blog.dao.mapper.CategoryMapeer;
import com.lsc.blog.dao.pojo.Category;
import com.lsc.blog.service.CategoryService;
import com.lsc.blog.vo.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapeer categoryMapeer;

    // 返回categoryVo
    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        // 根据分类id获得分类对象
        Category category = categoryMapeer.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

}
