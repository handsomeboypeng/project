package com.mszlu.blog.controller;

import com.mszlu.blog.aop.Cache;
import com.mszlu.blog.aop.LogAnnotation;
import com.mszlu.blog.service.ArticleService;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.ArticleParam;
import com.mszlu.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController // json数据交互
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    /**
     * 首页 文章列表
     *
     * @param pageParams
     * @return 所有文章
     */
    @PostMapping
    //加上此注解 代表要对此接口记录日志
    @LogAnnotation(module="文章",operation="获取文章列表")
    @Cache(expire = 5 * 60 * 1000,name = "list_article")
    public Result listArticle(@RequestBody PageParams pageParams) {
    //ArticleVo 页面接收的数据
        return articleService.listArticle(pageParams);
    }
    /**
     * 首页 最热文章
     * @return
     */
    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle() {
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 首页 最新文章
     * @return
     */
    @PostMapping("new")
    public Result newArticles() {
        int limit = 5;
        return articleService.findNewArticlesByCreateDate(limit);
    }

    /**
     * 文章归档
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id) {
        return articleService.findArticleById(id);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

}
