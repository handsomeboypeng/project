package com.mszlu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mszlu.blog.dao.dos.Archives;
import com.mszlu.blog.dao.mapper.ArticleBodyMapper;
import com.mszlu.blog.dao.mapper.ArticleMapper;
import com.mszlu.blog.dao.mapper.ArticleTagMapper;
import com.mszlu.blog.dao.pojo.Article;
import com.mszlu.blog.dao.pojo.ArticleBody;
import com.mszlu.blog.dao.pojo.ArticleTag;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.*;
import com.mszlu.blog.utils.UserThreadLocal;
import com.mszlu.blog.vo.*;
import com.mszlu.blog.vo.params.ArticleParam;
import com.mszlu.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;

    /*
     * 首页 文章展示
     */
    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page, pageParams.getCategoryId(), pageParams.getTagId(), pageParams.getYear(), pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(), true, true));
    }
/*
    @Override
    public Result listArticle(PageParams pageParams) {
        //1.分页查询article数据库表

        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        LambdaQueryWrapper<Article>queryWrapper = new LambdaQueryWrapper<>();
        //文章分类 查询文章的参数 加上分类id，判断不为空 加上分类条件
        if (pageParams.getCategoryId() != null) {
            //and category_id =#{categoryId}
            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
        }
        List<Long> articleIdList = new ArrayList<>();
        if (pageParams.getTagId() != null){
            //加入标签 条件查询
            //article表中 并没有tag字段 一篇文章 有多个标签
            //article_tag article_id 1:n tag_id
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if (articleIdList.size() > 0){
                queryWrapper.in(Article::getId,articleIdList);
            }
        }
        //是否置顶进行排序
        queryWrapper.orderByDesc(Article::getWeight);
        //order by create_date desc
        queryWrapper.orderByDesc(Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page,queryWrapper);
        List<Article> records = articlePage.getRecords();
        //能直接返回吗？不行，要返回vo,vo是为了以后需求方便
        List<ArticleVo> articleVoList = copyList(records,true,true);
        return Result.success(articleVoList);
    }
    */

    /*
     * 查询浏览数量排名前limit的文章
     */
    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article>queryWrapper = new LambdaQueryWrapper<>();
        //对浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCounts);
        //只取id和title
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result findNewArticlesByCreateDate(int limit) {
        LambdaQueryWrapper<Article>queryWrapper = new LambdaQueryWrapper<>();
        //对创建时间进行排序
        queryWrapper.orderByDesc(Article::getCreateDate);
        //只取id和title
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArticleArchives();
        return Result.success(archivesList);
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }

    //重载
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        //String.valueof()可以防止空指针异常
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);
        //时间类型不一样需要单独转换一下
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口都需要标签，作者信息
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = article.getId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody) {
            ArticleBodyVo articleBody = findArticleBody(article.getId());
            articleVo.setBody(articleBody);
        }
        if (isCategory) {
            CategoryVo categoryVo = categoryService.findCategoryById(article.getCategoryId());
            articleVo.setCategory(categoryVo);
        }
        return articleVo;
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBody(Long articleId) {
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId,articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    @Autowired
    private ThreadService threadService;

    /*
     * 文章详情
     */
    @Override
    public Result findArticleById(Long id) {
        /**
         * 1.根据id查询 文章信息
         * 2.根据bodyId和categoryid 去做关联查询
         */
        Article article = articleMapper.selectById(id);
        ArticleVo articleVo = copy(article, true, true,true,true);
        // 查看完文章增加阅读数
        // 数据库在做更新操作时会写锁，阻塞其他的操作，性能会降低，增加该接口此次的耗时，一旦更新失败不能影响查看文章的操作
        // 线程池，可以把更新操作，扔到线程池中去执行，与主线程取消联系
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    @Override
    @Transactional
    /**
     * 1. 发布文章 目的 构建Article对象
     * 2. 作者id  当前的登录用户
     * 3. 标签  要将标签加入到 关联列表当中
     * 4. body 内容存储 article bodyId
     */
    //此接口需要加入到登陆拦截当中
    public Result publish(ArticleParam articleParam) {
        //要写拦截器不然UserThreadLocal拿不到值，导致爆空指针异常
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        //权重
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        //插入之后 会生成一个文章id
        this.articleMapper.insert(article);

        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tag.getId()));
                this.articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        return Result.success(articleVo);
    }
}
