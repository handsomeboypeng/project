package com.mszlu.blog.aop;

import java.lang.annotation.*;

//aop开发统一缓存
//切点
//把缓存注解加在哪个地方哪个地方就是缓存的切点
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    //过期时间
    long expire() default 1 * 60 * 1000;
    //缓存标识 key
    String name() default "";

}
