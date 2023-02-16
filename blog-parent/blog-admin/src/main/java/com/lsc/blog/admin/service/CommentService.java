package com.lsc.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsc.blog.admin.mapper.CommentMapper;
import com.lsc.blog.admin.model.params.PageParam;
import com.lsc.blog.admin.pojo.Comment;
import com.lsc.blog.admin.vo.PageResult;
import com.lsc.blog.admin.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 查询评论列表
     * @param pageParam
     * @return
     */
    public Result listComment(PageParam pageParam){
        Page<Comment> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(pageParam.getQueryString())){
//            queryWrapper.eq(Comment::getContent, pageParam.getQueryString());
            queryWrapper.like(Comment::getContent,pageParam.getQueryString());
        }
        Page<Comment> commentPage = commentMapper.selectPage(page,queryWrapper);
        PageResult<Comment> pageResult = new PageResult<>();
        pageResult.setList(commentPage.getRecords());
        pageResult.setTotal(commentPage.getTotal());
        return Result.success(pageResult);

    }

    /**
     * 更新评论
     * @param comment
     * @return
     */
    public Result update(Comment comment) {
        this.commentMapper.updateById(comment);
        return Result.success(null);
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    public Result deleteComment(Long id) {
        this.commentMapper.deleteById(id);
        return Result.success(null);
    }
}
