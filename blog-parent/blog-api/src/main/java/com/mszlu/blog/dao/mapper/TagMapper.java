package com.mszlu.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mszlu.blog.dao.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id查询标签信息
     * @param articleId 文章id
     * @return 符合条件的标签信息集合
     */
    List<Tag> findTagsByArticleId(Long articleId);

    /**
     * 查询文章使用最多的limit个标签
     * @param limit 使用最多的标签个数
     * @return 符合条件的标签id集合
     */
    List<Long> findHotTags(int limit);

    /**
     * 根据标签id查询标签信息
     * @param tagIds 标签id
     * @return 符合条件的标签信息集合
     */
    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
