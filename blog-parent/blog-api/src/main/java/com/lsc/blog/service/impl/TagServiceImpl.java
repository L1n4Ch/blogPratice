package com.lsc.blog.service.impl;

import com.lsc.blog.dao.mapper.TagMapper;
import com.lsc.blog.dao.pojo.Tag;
import com.lsc.blog.service.TagService;
import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    //因为要返回的是<TagVo>，所以return是以copyList转义的结果
    public List<TagVo> findTagsByArticleId(Long articleId) {
        //mybatisPlus 无法进行多表查询-tagMapper在数据库表中除了有ms_tag表，还有一个ms_article tag关联表
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    /**
     * 最热标签(前6个)
     * @param limit
     * @return
     */
    @Override
    public Result hots(int limit) {
        /*
         * 业务逻辑：最热标签的查询根据当前标签ID下的文章数量决定
         * 即分组查询（groupBy）根据 tags_id，count（技术文章量）来查询，取前limit个，并从大到小排序（desc）
         */
        List<Long> tagIds =  tagMapper.findHotsTagIds(limit);
        //写一个tagIds为空的判断：如果为空，给tagIds赋予一个空值
        if(CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        //这里只得到了tagIds（最热标签的id），实际上需要的是Tag对象（最热），即还需要得到tagName
        //findTagsByTagIds里的Tags即tagName
        //tagList，即Tag对象（最热）的集合
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }

    private List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }
}
