package com.lsc.blog.admin.model.params;

import lombok.Data;

@Data
public class PageParam {

    private Integer currentPage;

    private Integer pageSize;

    /**
     * 查询条件
     */
    private String queryString;

}
