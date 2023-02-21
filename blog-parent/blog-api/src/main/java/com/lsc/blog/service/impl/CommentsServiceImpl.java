package com.lsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lsc.blog.dao.mapper.CommentMapper;
import com.lsc.blog.dao.pojo.Comment;
import com.lsc.blog.dao.pojo.SysUser;
import com.lsc.blog.service.CommentsService;
import com.lsc.blog.service.SysUserService;
import com.lsc.blog.utils.UserThreadLocal;
import com.lsc.blog.vo.CommentVo;
import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.UserVo;
import com.lsc.blog.vo.params.CommentParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result commentsByArticleId(Long id) {

        // 1.根据文章id查询评论列表 从comment表中查询
        // 2.根据作者id查询评论人信息
        // 3.判断 如果level = 1 要去查询有无子评论
        // 4.如果有 根据评论id进行查询（parentId）

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);
        // 倒序排序楼层评论
        queryWrapper.orderByDesc(Comment::getCreateDate);
        // 步骤1
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        // for循环 comments
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {

        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        // 作者信息 -- 属于步骤2
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);

        // 子评论 -- 属于步骤3
        Integer level = comment.getLevel();
        if(1 == level){
            // 如果level为1，则证明是1楼评论，即有parentId 即id
            Long id = comment.getId();
            // 步骤4
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            // 生成子评论
            commentVo.setChildrens(commentVoList);
        }
        // toUser 给谁评论
        if(level > 1){
            Long toUid = comment.getToUid();
            // 这里跟上面的 步骤2作者信息是一样的逻辑 复制过来修改对应参数即可
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    /**
     * 即根据父评论id查询子评论
     * 写法与commentsByArticleId类似
     * @param id
     * @return
     */
    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Comment::getParentId,id);
        // 注意这里的level一定要等于2 此方法是实现查询子评论
        queryWrapper.eq(Comment::getLevel,2);
        // 理解：本质使用了递归查询 level为2和查询条件为空是终止条件 level为1的时候会一直查询下去
        return copyList(commentMapper.selectList(queryWrapper));
    }

    @Override
    public Result comment(CommentParams commentParams) {
        // 使用threadLocal之前的保存用户信息 拿到当前登录的用户信息 （过程：登录拦截器LoginInterceptor中保存到threadLocal）
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParams.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParams.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParams.getParent();
        if(parent == null || parent == 0){
            comment.setLevel(1);
        }else {
            comment.setLevel(2);
        }
        // 如果parent（parentId） 等于null时，设为0，如果不是 则给parent设一个值
        comment.setParentId(parent == null ? 0 :parent);
        Long toUserId = commentParams.getToUserId();
        // 同上
        comment.setToUid(toUserId == null ? 0 :toUserId);
        // 不返回commentVo对象的原因：vo属于输出，是给用户看的，而写评论，是对数据库的，不返回vo结果
        this.commentMapper.insert(comment);
        return Result.success(null);
    }
}
