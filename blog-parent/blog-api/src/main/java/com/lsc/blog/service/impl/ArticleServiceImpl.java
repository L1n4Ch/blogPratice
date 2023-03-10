package com.lsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsc.blog.dao.dos.Archives;
import com.lsc.blog.dao.mapper.ArticleBodyMapper;
import com.lsc.blog.dao.mapper.ArticleMapper;
import com.lsc.blog.dao.mapper.ArticleTagMapper;
import com.lsc.blog.dao.mapper.CategoryMapeer;
import com.lsc.blog.dao.pojo.*;
import com.lsc.blog.service.*;
import com.lsc.blog.utils.UserThreadLocal;
import com.lsc.blog.vo.*;
import com.lsc.blog.vo.params.ArticleParams;
import com.lsc.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if(pageParams.getCategoryId() != null){
            // 显示对应标签/分类下的文章
            // 给pageParams加上参数tagId和categoryId，进行if判断
            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
        }

        // new一个articleId的list
        List<Long> articleIdList = new ArrayList<>();

        // 考验sql的理解
        if(pageParams.getTagId() != null){
            // 注意，article表中是没有tag字段 因为一篇文章有多个标签 使用article_tag关联表
            // 实现过程：根据pageParams.getTagId()得到的tagId把所有的articleId查出来，然后使用"in"查询，插入下查询条件下
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            // ArticleTag的tagId = pageParams的tagId
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
            // 查到tags的List
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            // 根据上面新建的空的list 对articleTags进行for循环
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if(articleIdList.size() > 0){
                // and id in(1,2,3)
                queryWrapper.in(Article::getId,articleIdList);
            }
        }
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
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }


    // ArticleVo里有 tag和author属性。
    // 而Article里则没有这两个属性，而且并不是所有接口都需要这两个属性
    // 所以copy需要加一层判断：isTag isAuthor
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        // 新增了setId这一行代码
        articleVo.setId(String.valueOf(article.getId()));
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

//            // 同上，这里通过article类的get方法只能拿到authorId，所以使用authorId来作为id
//            Long authorId = article.getAuthorId();
//            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());

            // 视频最后修改了代码 ArticleVo里的author返回值类为userVo
            Long authorId = article.getAuthorId();
            SysUser sysUser = sysUserService.findUserById(authorId);
            // 跟原来的代码不同是这里new了个UserVo
            UserVo userVo = new UserVo();
            userVo.setAvatar(sysUser.getAvatar());
            userVo.setId(sysUser.getId().toString());
            userVo.setNickname(sysUser.getNickname());
            articleVo.setAuthor(userVo);
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

    /**
     * 文章内容？（非ArticleService方法）
     * @param bodyId
     * @return
     */
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

    @Override
    public Result publish(ArticleParams articleParams) {

        // 1.发布文章 需要获取作者id 即拿到当前的登录用户
        // 2.发布文章 构建Article对象
        // 3.发布文章 需要获取文章的标签 要将标签加入到关联列表中 (tag存在关联表)
        // 4.发布文章 body 内容存储

        // 步骤1
        SysUser sysUser = UserThreadLocal.get();
        // 步骤2
        Article article = new Article();
        boolean isEdit = false;
        // 如果有文章id 即已有文章 - 更新
        if(articleParams.getId() != null){
            article = new Article();
            article.setId(articleParams.getId());
            article.setTitle(articleParams.getTitle());
            article.setSummary(articleParams.getSummary());
            article.setCategoryId(Long.parseLong(articleParams.getCategory().getId()));
            // 对象作为参数 根据id update （拿到对象即拿到id）
            articleMapper.updateById(article);
            isEdit = true; // 如果存在文章，既可以使用编辑（更新）功能
        }else {
            // 如果没传文章id - 新增 新建文章 赋属性默认值
            article = new Article();
            article.setAuthorId(sysUser.getId());
            article.setWeight(Article.Article_Common);
            article.setViewCounts(0);
            article.setTitle(articleParams.getTitle());
            article.setSummary(articleParams.getSummary());
            article.setCommentCounts(0);
            article.setCreateDate(System.currentTimeMillis());
            article.setCategoryId(Long.parseLong(articleParams.getCategory().getId()));
            // 对象作为参数 根据id insert（insert文章后就会生成articleId）
            this.articleMapper.insert(article);
        }

        // 步骤3
        // tag
        List<TagVo> tags = articleParams.getTags();
        // 获取articleId （因insert文章生成了articleId）
        Long articleId = article.getId();

        // 先删除
        // 标签只能选一个，多选显示的是选中的最后一个，即只能减少不能增加。
        // 如果删除掉那段代码，编辑可以增加但是不能减少。
        // 解决办法：把这段代码放在标签判定循环之前！！！！！
        if(isEdit){ // isEdit == true
            LambdaQueryWrapper<ArticleTag> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(ArticleTag::getArticleId,articleId);
            articleTagMapper.delete(queryWrapper);
        }
        if(tags != null){
            // tags.for
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        // body
        // 步骤4
        if(isEdit){
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParams.getBody().getContent());
            articleBody.setContentHtml(articleParams.getBody().getContentHtml());
            LambdaUpdateWrapper<ArticleBody> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(ArticleBody::getArticleId,article.getId());
            articleBodyMapper.update(articleBody, updateWrapper);
        }else {
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParams.getBody().getContent());
            articleBody.setContentHtml(articleParams.getBody().getContentHtml());
            // insert生成文章即id
            articleBodyMapper.insert(articleBody);
            article.setBodyId(articleBody.getId());
            // 前面已insert文章，最后update
            articleMapper.updateById(article);
        }

//        ArticleVo articleVo = new ArticleVo();
//        articleVo.setId(article.getId());
//        return Result.success(articleVo);

        Map<String, String> map = new HashMap<>();
        // 避免精度损失问题，使用toString
        map.put("id",article.getId().toString());
//        if(isEdit){
//            //发送一条消息给rocketmq 当前文章更新了，更新一下缓存吧
//            ArticleMessage articleMessage = new ArticleMessage();
//            articleMessage.setArticleId(article.getId());
//        }
        return Result.success(map);

    }
}
