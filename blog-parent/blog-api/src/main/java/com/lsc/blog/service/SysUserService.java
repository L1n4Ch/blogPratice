package com.lsc.blog.service;

import com.lsc.blog.dao.pojo.SysUser;
import com.lsc.blog.vo.Result;

public interface SysUserService {

    /**
     * 这个方法是为ArticleServiceImpl服务的，跟TagService里的List<TagVo> findTagsByArticleId一样
     * ArticleServiceImpl中的copy方法使用： 根据用户id（作者id），查找用户（作者）信息，得到nickName
     * 这里跟 findTagsByArticleId 不同，不返回vo对象的原因是：ArticleVo对象里的author类型为string 且返回的对象是SysUser类（nickName）
     * 注意，不返回vo对象的原因2：其实这里也可以做一个vo对象-SysUserVo，但是视频里讲到暂时不做vo，原因未知
     * 这里的参数id 即 authorId
     * @param id
     * @return
     */
    SysUser findUserById(Long id);

    /**
     * 根据用户名和密码去user表中查询是否存在此用户
     * @param account
     * @param password
     * @return
     */
    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息（登录后获取用户信息）
     * @param token
     * @return
     */
    Result findUserByToken(String token);
}
