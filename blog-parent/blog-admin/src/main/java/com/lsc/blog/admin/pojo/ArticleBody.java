package com.lsc.blog.admin.pojo;

import lombok.Data;

@Data
public class ArticleBody {

    private Long id;

    private String content;

    private String contentHtml;

    private Long articleId;

}
