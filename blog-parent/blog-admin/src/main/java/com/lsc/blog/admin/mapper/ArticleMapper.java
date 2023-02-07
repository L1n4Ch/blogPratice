package com.lsc.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lsc.blog.admin.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
