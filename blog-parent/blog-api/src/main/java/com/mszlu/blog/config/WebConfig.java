package com.mszlu.blog.config;

import com.mszlu.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置，不可设置为*，不安全, 前后端分离项目，可能域名不一致
        //本地测试 端口不一致 也算跨域
        //  /**允许所有接口访问到http://localhost:8080

        //registry.addMapping("/**").allowedOrigins("http://localhost:8083");//dev
        registry.addMapping("/**").allowedOrigins("http://43.143.40.48/","null").allowedMethods("POST","GET","PUT","OPTIONS","DELETE").maxAge(3600).allowCredentials(true);//prod

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截接口，后续实际遇到需要拦截的接口时，再配置需要拦截的路径
        //评论的时候需要用户已经登陆所以这里需要判断用户是否登陆
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish");;
    }
}
