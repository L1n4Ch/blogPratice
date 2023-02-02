package com.lsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsc.blog.dao.dos.Archives;
import com.lsc.blog.dao.mapper.ArticleBodyMapper;
import com.lsc.blog.dao.mapper.ArticleMapper;
import com.lsc.blog.dao.mapper.CategoryMapeer;
import com.lsc.blog.dao.pojo.Article;
import com.lsc.blog.dao.pojo.ArticleBody;
import com.lsc.blog.service.*;
import com.lsc.blog.vo.ArticleBodyVo;
import com.lsc.blog.vo.ArticleVo;
import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    private CategoryService categoryService;

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 根据"创建时间"和"是否置顶"降序排序
        queryWrapper.orderByDesc(Article::getCreateDate,Article::getWeight);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        // 得到page后再获取整个page 的 list （获取的是pojo的list）
        List<Article> records = articlePage.getRecords();
        // 需要将其转义成vo的list （由于上面获取的是pojo的list）
        List<ArticleVo> articleVoList = copyList(records, true, true);
        return Result.success(articleVoList);
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 最热文章根据"文章访问量"降序排序
        queryWrapper.orderByDesc(Article::getViewCounts);
        // 取文章的两条数据：id和title
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit);
        // sql语句为：select id,title from article order by view_counts desc limit 5;
        List<Article> articles = articleMapper.selectList(queryWrapper);
        // 需要将其转义成vo的list （由于上面获取的是pojo的list）
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        // "Archives":文章归档类，数据库并不存在，所以创建dos文件夹，里面存放do对象 （不是vo对象，所以无需copyList）
        // （指不是数据库的对象，只做展示使用，即不会持久化的对象）
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }


    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        // copy -> copyList ： 本质即使用for循环遍历record，得到recordList（records），再add至articleVoList
            // for循环快捷键 ： records.for
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    /**
     * 这里对上面的copyList进行了重载，方法名相同，参数不同
     * 因为上面的copyList还有别的用途，下面的copyList用于文章详情
     * @param records
     * @param isTag
     * @param isAuthor
     * @param isBody
     * @param isCategory
     * @return
     */
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        // copy -> copyList ： 本质即使用for循环遍历record，得到recordList（records），再add至articleVoList
        // for循环快捷键 ： records.for
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, true, true));
        }
        return articleVoList;
    }


    // ArticleVo里有 tag和author属性。
    // 而Article里则没有这两个属性，而且并不是所有接口都需要这两个属性
    // 所以copy需要加一层判断：isTag isAuthor
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        // 注意Article和ArticleVo的createDate，类型不同，一个是Long一个是String，无法直接copy，这里进行set并使用toString转换
        articleVo.setCreateDates(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd:mm"));
        if(isTag){
            // 注意：因为是文章的tag，所以用文章id（articleId）查找 tag，
            // 而且article对象并没有tagId这个属性，这里通过article类的get方法只能拿到文章id，所以使用articleId来查找tag
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            // 同上，这里通过article类的get方法只能拿到authorId，所以使用authorId来作为id
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if(isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if(isCategory){
            Long categoryId = article.getCategoryId();
            // 这里category单独属于一个类别 创建categoryService
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private CategoryMapeer categoryMapeer;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    @Autowired
    private ThreadService threadService;

    @Override
    public Result findArticleById(Long articleId) {

        // 1.根据id查询文章信息
        // 2.文章详情还包括了 标签/文章分类 要进行表的关联查询（根据bodyId和categoryId做关联查询）
        // ps.返回ArticleVo对象

        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);

        // 看完文章了 理应在此处新增阅读数？思考问题
        // 问题1：查看完文章详情，本应直接返回数据 这时候如果新增阅读数（即做了一个更新操作），即更新时加了“写锁”（注意：不是所有更新都会加写锁），会阻塞其他的“读”操作，性能就会比较低
        // 问题2：更新操作还会增加此次接口的耗时
        // 思考解决办法
        // 问题1没法解决，必定会降低性能，所以只能优化问题2 -- 即如果更新步骤出问题，不会因为耗时影响文章的查看
        // 使用线程池技术解决 -- 把更新操作(更新文章阅读数)扔到线程池去执行 不会影响查看文章详情的主线程

        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);

    }

}
