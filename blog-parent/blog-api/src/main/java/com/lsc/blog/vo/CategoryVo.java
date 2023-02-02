package com.lsc.blog.vo;

import lombok.Data;

@Data
public class CategoryVo {

    private Long id;

    /**
     * 图标路径
     */
    private String avatar;

    /**
     * 类别名称
     */
    private String categoryName;

}