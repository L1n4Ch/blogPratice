package com.lsc.blog.vo;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
    private Long id;

    private String title;

    private String summary;

    private int commentCounts;

    private int viewCounts;

    private int weight;

    /**
     * 数据库，Article对象是Long型，vo对象呈现在前端的是String型
     */
    private String createDates;

    private String author;

    //    private ArticleBodyVo body;

    private List<TagVo> tags;

    //    private CategoryVo category;
}
