package com.mszlu.blog.service;

import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.TagVo;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
