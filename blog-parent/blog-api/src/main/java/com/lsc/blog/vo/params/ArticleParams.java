package com.lsc.blog.vo.params;

import com.lsc.blog.vo.CategoryVo;
import com.lsc.blog.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParams {

    private Long id;

    private ArticleBodyParams body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;

}
