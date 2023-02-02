package com.lsc.blog.service;

import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.params.CommentParams;

public interface CommentsService {
    /**
     * 根据文章id查询显示所有的评论列表
     * @param id
     * @return
     */
    Result commentsByArticleId(Long id);

    /**
     * 评论功能
     * @param commentParams
     * @return
     */
    Result comment(CommentParams commentParams);
}
