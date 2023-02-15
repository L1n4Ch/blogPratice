package com.lsc.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsc.blog.admin.mapper.ArticleBodyMapper;
import com.lsc.blog.admin.model.params.PageParam;
import com.lsc.blog.admin.pojo.Article;
import com.lsc.blog.admin.pojo.ArticleBody;
import com.lsc.blog.admin.vo.PageResult;
import com.lsc.blog.admin.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleBodyService {

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    public Result listArticleBody(PageParam pageParam) {
        Page<ArticleBody> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(pageParam.getQueryString())){
            queryWrapper.eq(ArticleBody::getArticleId, pageParam.getQueryString());
        }
        Page<ArticleBody> articleBodyPage = articleBodyMapper.selectPage(page, queryWrapper);
        PageResult<ArticleBody> pageResult = new PageResult<>();
        pageResult.setList(articleBodyPage.getRecords());
        pageResult.setTotal(articleBodyPage.getTotal());
        return Result.success(pageResult);
    }

    public Result update(ArticleBody articleBody) {
        this.articleBodyMapper.updateById(articleBody);
        return Result.success(null);
    }

    public Result deleteArticleBody(Long id) {
        this.articleBodyMapper.deleteById(id);
        return Result.success(null);
    }

}
