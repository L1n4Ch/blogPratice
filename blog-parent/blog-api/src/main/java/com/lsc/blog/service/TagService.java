package com.lsc.blog.service;

import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    /**
     * 这个方法是为ArticleServiceImpl服务的，并不是为TagServiceImpl服务
     * ArticleServiceImpl中的copy方法使用：根据文章id查找标签vo对象
     * 返回vo对象的原因是：ArticleVo对象里的tag类型为List<TagVo>
     * ------------------------------
     * 和TagMapper里的方法同名但是用处不同
     * 该方法返回的是tagVo对象，TagMapper返回的是tag对象
     * 得到tag对象后通过copyList，转义成tagVo对象，所以TagMapper里的方法是该方法的"子项"
     *
     * @param articleId
     * @return
     */
    List<TagVo> findTagsByArticleId(Long articleId);

    /**
     * 最热标签
     * @param limit
     * @return
     */
    Result hots(int limit);

    /**
     * 写文章-查询所有文章标签
     * @return
     */
    Result findAll();

    /**
     * 导航-文章标签
     * @return
     */
    Result findAllDetail();

    /**
     *
     * @param id
     * @return
     */
    Result findDetailById(Long id);
}
