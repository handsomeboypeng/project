package com.mszlu.blog.handler;

import com.mszlu.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 对加了@Controller注解的类进行拦截 AOP实现
@ControllerAdvice
public class AllExceptionHandler {

    // 进行异常处理，处理Exception.class
    @ExceptionHandler(Exception.class)
    //返回json数据
    @ResponseBody
    public Result doException(Exception e){
        e.printStackTrace();
        return Result.fail(-999, "系统异常");
    }
}
