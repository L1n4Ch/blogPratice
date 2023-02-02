package com.lsc.blog.controller;

import com.lsc.blog.service.CommentsService;
import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.params.CommentParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentController {

    @Autowired
    private CommentsService commentsService;

    /**
     * 评论列表接口
     * @param id
     * @return
     */
    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id){
        return commentsService.commentsByArticleId(id);
    }

    /**
     * 评论功能接口
     * @return
     */
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParams commentParams){
        return commentsService.comment(commentParams);
    }

}
