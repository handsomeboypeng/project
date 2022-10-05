package com.mszlu.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mszlu.blog.dao.dos.Archives;
import com.mszlu.blog.dao.pojo.Article;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

//因为前面MyBatisPlus里配置了扫描路径，所以不需要@Mapper，但是
//但idea并不能识别到我们的mapper已经被注入。
//要想不报红，可以在mapper层加上@Repository注解
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据流量数量查询排名前limit的文章
     * @param limit 排名
     * @return 符合条件的文章集合
     */
    List<Article> findHotArticleByViewCounts(int limit);

    /**
     * 根据创建时间查询前limit的文章
     * @param limit 文章个数
     * @return 符合条件的文章信息集合
     */
    List<Article> findNewArticlesByCreateDate(int limit);

    /**
     * 文章归档
     * @return 归档信息
     */
    List<Archives> listArticleArchives();

    /**
     * 根据类别id，标签id，年，月查询文章信息
     * @param page
     * @param categoryId
     * @param tagId
     * @param year
     * @param month
     * @return
     */
    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);

}
