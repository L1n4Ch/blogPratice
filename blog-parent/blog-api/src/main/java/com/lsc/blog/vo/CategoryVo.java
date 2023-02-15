package com.lsc.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CategoryVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 图标路径（头像）
     */
    private String avatar;

    /**
     * 类别名称
     */
    private String categoryName;

    private String description;

}
