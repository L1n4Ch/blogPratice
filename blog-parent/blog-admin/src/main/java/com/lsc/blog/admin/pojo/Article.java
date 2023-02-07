package com.lsc.blog.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Article {

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;

    /**
     * 简介
     */
    private String summary;

    /**
     * 评论数量
     */
    private Integer commentCounts;

    /**
     * 浏览数量
     */
    private Integer viewCounts;

    /**
     * 作者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long authorId;

    /**
     * 内容id
     */
//    private Long bodyId;

    /**
     * 类别id
     */
//    private Long categoryId;

    private Integer weight;

//    private Long createDate;

}
