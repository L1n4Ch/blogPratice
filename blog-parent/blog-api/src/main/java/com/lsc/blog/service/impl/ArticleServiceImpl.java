package com.lsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsc.blog.dao.mapper.ArticleMapper;
import com.lsc.blog.dao.pojo.Article;
import com.lsc.blog.service.ArticleService;
import com.lsc.blog.service.SysUserService;
import com.lsc.blog.service.TagService;
import com.lsc.blog.vo.ArticleVo;
import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.params.PageParams;
import org.aspectj.weaver.ast.Var;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询文章列表
     *
     * @param pageParams
     * @return
     */
    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //2.确认查询条件：文章按时间降序排序 和 是否置顶排序
        queryWrapper.orderByDesc(Article::getCreateDate,Article::getWeight);
        //1.selectPage两个条件，page和查询条件query
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        //得到page后再获取整个page 的 list (获取的是pojo的list，需要将其转义成vo的list)
        List<Article> records = articlePage.getRecords();
        //转义方法：copyList-手动写,本质是copy转义，copyList是遍历转义的结果
        List<ArticleVo> articleVoList = copyList(records, true, true);
        return Result.success(articleVoList);
    }

    /**
     * 最热文章
     * @param limit
     * @return
     */
    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //确认查询条件：最热文章按照文章访问量view_counts进行倒序排序
        queryWrapper.orderByDesc(Article::getViewCounts);
        //取文章的两条数据：id和title
        queryWrapper.select(Article::getId,Article::getTitle);
        //last是最后拼接，这里写成：queryWrapper.last("limit 5");是一个效果？只是在这里声明limit？
        queryWrapper.last("limit " + limit);
        //sql语句为：select id,title from article order by view_counts desc limit 5;
        List<Article> articles = articleMapper.selectList(queryWrapper);
        //返回vo对象，而不是pojo对象，使用copyList转义
        return Result.success(copyList(articles,false,false));

        //以下为上面写法的扩展写法
//        List<ArticleVo> articleVosList = copyList(articles, false, false);
//        return Result.success(articleVosList);

    }

    /**
     * 最新文章
     * @param limit
     * @return
     */
    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit);
        //sql语句为：select id,title from article order by create_date desc limit 5;
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {    //这里是 records.for，才有会这个record
            articleVoList.add(copy(record, isTag, isAuthor));
        }
        return articleVoList;
    }

    //这里的boolean判断-并不是所有文章接口都需要标签和作者
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        //注意Article和ArticleVo的createDate，类型不同，一个是Long一个是String，无法直接copy，这里直接进行set
        articleVo.setCreateDates(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd:mm"));
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        return articleVo;
    }
}
