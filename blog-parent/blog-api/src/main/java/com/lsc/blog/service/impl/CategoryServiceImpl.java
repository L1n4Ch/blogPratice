package com.lsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lsc.blog.dao.mapper.CategoryMapeer;
import com.lsc.blog.dao.pojo.Category;
import com.lsc.blog.service.CategoryService;
import com.lsc.blog.vo.CategoryVo;
import com.lsc.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public CategoryVo copy(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryMapeer.selectList(queryWrapper);
        // 注意：下面为原来的写法，视频里偷偷增加了查询条件
//        List<Category> categories = categoryMapeer.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(categories));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        List<Category> categories = categoryMapeer.selectList(queryWrapper);
        return Result.success(copyList(categories));
    }
}
