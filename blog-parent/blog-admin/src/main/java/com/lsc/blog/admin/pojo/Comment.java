package com.lsc.blog.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Comment {

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long authorId;

//    private Long parentId;
//
//    private Long toUid;

    private Integer level;
}
