package com.lsc.blog.admin.controller;

import com.lsc.blog.admin.model.params.PageParam;
import com.lsc.blog.admin.pojo.*;
import com.lsc.blog.admin.service.*;
import com.lsc.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 权限列表的增删改查
     * @param pageParam
     * @return
     */
    @PostMapping("permission/permissionList")
    public Result listPermission(@RequestBody PageParam pageParam){
        return permissionService.listPermission(pageParam);
    }

    @PostMapping("permission/add")
    public Result add(@RequestBody Permission permission){
        return permissionService.add(permission);
    }

    @PostMapping("permission/update")
    public Result update(@RequestBody Permission permission){
        return permissionService.update(permission);
    }

    @GetMapping("permission/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return permissionService.delete(id);
    }

    /**
     * 评论列表的增删改查
     * @param pageParam
     * @return
     */
    @PostMapping("comment/commentList")
    public Result listComment(@RequestBody PageParam pageParam){
        return commentService.listComment(pageParam);
    }

    @PostMapping("comment/update")
    public Result update(@RequestBody Comment comment){
        return commentService.update(comment);
    }

    @GetMapping("comment/delete/{id}")
    public Result deleteComment(@PathVariable("id") Long id){
        return commentService.deleteComment(id);
    }

    /**
     * 文章列表的增删改查
     * @param pageParam
     * @return
     */
    @PostMapping("article/articleList")
    public Result listArticle(@RequestBody PageParam pageParam){
        return articleService.listArticle(pageParam);
    }


    @PostMapping("article/update")
    public Result update(@RequestBody Article article){
        return articleService.update(article);
    }

    @GetMapping("article/delete/{id}")
    public Result deleteArticle(@PathVariable("id") Long id){
        return articleService.deleteArticle(id);
    }

    /**
     * 分类列表的增删改查
     * @param pageParam
     * @return
     */
    @PostMapping("category/categoryList")
    public Result listCategory(@RequestBody PageParam pageParam){
        return categoryService.listCategory(pageParam);
    }


    @PostMapping("category/update")
    public Result update(@RequestBody Category category){
        return categoryService.update(category);
    }

    @GetMapping("category/delete/{id}")
    public Result deleteCategory(@PathVariable("id") Long id){
        return categoryService.deleteCategory(id);
    }

    @PostMapping("category/add")
    public Result add(@RequestBody Category category){
        return categoryService.add(category);
    }

    /**
     * 标签列表的增删改查
     */
    @PostMapping("tag/tagList")
    public Result listTag(@RequestBody PageParam pageParam){
        return tagService.listTag(pageParam);
    }

    @PostMapping("tag/update")
    public Result update(@RequestBody Tag tag){
        return tagService.update(tag);
    }

    @GetMapping("tag/delete/{id}")
    public Result deleteTag(@PathVariable("id") Long id){
        return tagService.deleteTag(id);
    }

    @PostMapping("tag/add")
    public Result add(@RequestBody Tag tag){
        return tagService.add(tag);
    }

    /**
     * 用户管理的增删改查
     */
    @PostMapping("sysUser/sysUserList")
    public Result listSysUser(@RequestBody PageParam pageParam){
        return sysUserService.listArticleBody(pageParam);
    }

    @PostMapping("sysUser/update")
    public Result update(@RequestBody SysUser sysUser){
        return sysUserService.update(sysUser);
    }

    @GetMapping("sysUser/delete/{id}")
    public Result deleteSysUser(@PathVariable("id") Long id){
        return sysUserService.deleteSysUser(id);
    }

}
