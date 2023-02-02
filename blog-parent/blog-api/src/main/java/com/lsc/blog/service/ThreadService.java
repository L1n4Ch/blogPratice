package com.lsc.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lsc.blog.dao.mapper.ArticleMapper;
import com.lsc.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    /**
     * 更新文章阅读数
     * @param articleMapper
     * @param article
     */

    // 目的：在线程池中执行 不影响原有主线
    @Async("taskExecutor")  // Async+线程池名称 到指定的线程池执行进行
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {

        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts +1);
        LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        // 设置一个 为了在多线程的环境下 保证线程安全的操作
        updateWrapper.eq(Article::getViewCounts,viewCounts);
        // update方法的参数包含 属性和更新条件
        articleMapper.update(articleUpdate,updateWrapper);

//        // 以下是test
//        try {
//            Thread.sleep(5000);
//            System.out.println("更新完成了");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 如果不加 @Async("taskExecutor")
        // 即查看文章和增加阅读数在同一线程池运行，可以尝试去掉此注解确认下查看文章详情的速度
        // 即：等5s的更新操作结束才刷新文章

    }
}
