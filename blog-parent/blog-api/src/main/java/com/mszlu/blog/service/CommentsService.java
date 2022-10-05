package com.mszlu.blog.service;

import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.CommentParam;

public interface CommentsService {
    /**
     * 根据文章id 查询所有的评论列表
     * @param articleId
     * @return
     */
    Result commentsByArticleId(Long articleId);

    Result comment(CommentParam commentParam);
}

