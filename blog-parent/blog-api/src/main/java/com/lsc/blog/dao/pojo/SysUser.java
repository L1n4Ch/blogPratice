package com.lsc.blog.dao.pojo;

import lombok.Data;

@Data
public class SysUser {

//    @TableId(type = IdType.ASSIGN_ID)   // 分布式id
//    @TableId(type = IdType.AUTO)    // 自增id
    // 建议使用分布式id（mybatisPlus默认使用分布式id），以后用户多了，要进行分表操作
    private Long id;

    private String account;

    /**
     * 是否为管理员
     */
    private Integer admin;

    /**
     * 头像
     */
    private String avatar;

    private Long createDate;

    /**
     * 是否删除
     */
    private Integer deleted;

    private String email;

    private Long lastLogin;

    private String mobilePhoneNumber;

    /**
     * 昵称
     */
    private String nickname;

    private String password;

    /**
     * 加密盐 （可以在这里写死加密盐？然后再从类中获取该属性？）
     */
    private String salt;

    /**
     * 状态
     */
    private String status;

}
