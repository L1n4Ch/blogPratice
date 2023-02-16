package com.lsc.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsc.blog.admin.mapper.TagMapper;
import com.lsc.blog.admin.model.params.PageParam;
import com.lsc.blog.admin.pojo.Category;
import com.lsc.blog.admin.pojo.Tag;
import com.lsc.blog.admin.vo.PageResult;
import com.lsc.blog.admin.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;

    /**
     * 查询标签列表
     * @param pageParam
     * @return
     */
    public Result listTag(PageParam pageParam) {
        Page<Tag> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(pageParam.getQueryString())){
//            queryWrapper.eq(Tag::getTagName, pageParam.getQueryString());
            queryWrapper.like(Tag::getTagName,pageParam.getQueryString());
        }
        Page<Tag> tagPage = tagMapper.selectPage(page,queryWrapper);
        PageResult<Tag> pageResult = new PageResult<>();
        pageResult.setList(tagPage.getRecords());
        pageResult.setTotal(tagPage.getTotal());
        return Result.success(pageResult);

    }

    /**
     * 更新标签
     * @param tag
     * @return
     */
    public Result update(Tag tag) {
        this.tagMapper.updateById(tag);
        return Result.success(null);
    }

    /**
     * 删除标签
     * @param id
     * @return
     */
    public Result deleteTag(Long id) {
        this.tagMapper.deleteById(id);
        return Result.success(null);
    }

    /**
     * 新增标签
     * @param tag
     * @return
     */
    public Result add(Tag tag) {
        this.tagMapper.insert(tag);
        return Result.success(null);
    }
}
