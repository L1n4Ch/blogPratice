package com.lsc.blog.admin.model.params;

import lombok.Data;

@Data
public class PageParam {

    /**
     * 当前页数
     */
    private Integer currentPage;

    /**
     * 每页显示的数量
     */
    private Integer pageSize;

    /**
     * 查询条件
     */
    private String queryString;

}
