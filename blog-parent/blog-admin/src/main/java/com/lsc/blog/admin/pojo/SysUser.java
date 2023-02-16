package com.lsc.blog.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class SysUser {

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
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

    private String email;

    private Long lastLogin;

    private String mobilePhoneNumber;

    private String nickname;

//    private String password;

    /**
     * 加密盐 （可以在这里写死加密盐？然后再从类中获取该属性？）
     */
//    private String salt;

    /**
     * 状态
     */
//    private String status;

}
