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
    public List<TagVo> findTagsByArticleId(Long articleId) {
        // 在数据库表中除了有ms_tag表，还有一个ms_article tag关联表，进行多表查询
        // 多表查询：在ms_tag表中查询获得tag的集合list；又需要在 ms_article_tag表 中 拿articleId（参数），所以是多表查询
        // 而mybatisPlus无法进行多表查询，所以需要在mapper里写sql语句
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public Result hots(int limit) {
        // 业务逻辑：最热标签的查询 根据当前 标签ID 下的文章数量决定，文章数量最多就是最热标签（而不是访问的人最多，那是最热文章）
        // 即分组查询（groupBy）根据 tags_id的count（技术文章量）来查询，取前limit个，并从大到小排序（desc）
        List<Long> tagIds =  tagMapper.findHotsTagIds(limit);
        // tagIds为空的判断：如果为空，给tagIds赋予一个空值
        if(CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        // 前面是获得最热标签的tagId，这里是通过tagId，遍历获得最热标签对象（包含tagId和tagName）
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
