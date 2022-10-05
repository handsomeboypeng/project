package com.mszlu.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mszlu.blog.dao.mapper.ArticleMapper;
import com.mszlu.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {
    //期望此操作在线程池执行，不会影响原有的主线程
    @Async("taskExecutor")  //taskExecutor线程池名称，这个注解表示要在线程池中执行一个任务
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(article.getViewCounts() + 1);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,article.getId());
        //设置一个 为了在多线程的环境下 线程安全
        queryWrapper.eq(Article::getViewCounts,article.getViewCounts());
        //update article set view_count = 100 where view_count = 99 and id = 11
        //mybatis-plus只要articleUpdate对象的属性不为null就会更新相应的属性 所有属性要设置为包装类型
        articleMapper.update(articleUpdate,queryWrapper);
        try {
            //睡眠5秒 证明不会影响主线程的使用
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
