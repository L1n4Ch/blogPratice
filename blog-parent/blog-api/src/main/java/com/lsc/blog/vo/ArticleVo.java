package com.lsc.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {

    // 解决因雪花算法导致精度缺失 导致有些文章正文无法显示的问题
//    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    private String title;

    private String summary;

    // 这三个int类型的属性都要改成integer -
    // 因为int数据类型会有默认值0。而mybatisPlus中，进行查询的时候，如果对象Article类不为null，则会给它的其他int属性赋予默认值，即使我们使用的是setViewCounts方法。（具体教程回顾P20）
    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;

    // 数据库，Article对象是Long型，vo对象呈现在前端的是String型
    private String createDates;

//    private String author;
    private UserVo author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;
}
