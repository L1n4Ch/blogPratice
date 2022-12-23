package com.lsc.blog.service;

import com.lsc.blog.vo.Result;
import com.lsc.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    //这和TagMapper里的方法同名但是用处不同 ?????????????????????????///
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);
}
