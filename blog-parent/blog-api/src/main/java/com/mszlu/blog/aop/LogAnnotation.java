package com.mszlu.blog.aop;

import java.lang.annotation.*;

/**
 * 日志注解
 */
//TYPE 代表可以放在类上面 METHOD 代表可以放在方法上
//下面这三个注解只要是注解都有  开发完注解开发切面
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operation() default "";
}
