package com.lsc.blog.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permission {

    // 后台管理不需要分布式id
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String path;

    private String description;

}
