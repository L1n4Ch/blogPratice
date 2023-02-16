package com.lsc.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsc.blog.admin.mapper.ArticleMapper;
import com.lsc.blog.admin.model.params.PageParam;
import com.lsc.blog.admin.pojo.Article;
import com.lsc.blog.admin.vo.PageResult;
import com.lsc.blog.admin.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 查询文章列表
     * @param pageParam
     * @return
     */
    public Result listArticle(PageParam pageParam) {
        Page<Article> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(pageParam.getQueryString())){
//            queryWrapper.eq(Article::getTitle, pageParam.getQueryString());
            queryWrapper.like(Article::getTitle, pageParam.getQueryString());
        }
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        PageResult<Article> pageResult = new PageResult<>();
        pageResult.setList(articlePage.getRecords());
        pageResult.setTotal(articlePage.getTotal());
        return Result.success(pageResult);
    }

    /**
     * 更新文章
     * @param article
     * @return
     */
    public Result update(Article article) {
        this.articleMapper.updateById(article);
        return Result.success(null);
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    public Result deleteArticle(Long id) {
        this.articleMapper.deleteById(id);
        return Result.success(null);
    }
}
