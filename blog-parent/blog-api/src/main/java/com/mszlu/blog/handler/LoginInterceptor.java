package com.mszlu.blog.handler;

import com.alibaba.fastjson.JSON;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.LoginService;
import com.mszlu.blog.utils.UserThreadLocal;
import com.mszlu.blog.vo.ErrorCode;
import com.mszlu.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//springmvc 拦截器  使用拦截器，进行登录拦截，如果遇到需要登录才能访问的接口，如果未登录，拦截器直接返回，并跳转登录页面。
@Component
@Slf4j//利用lombok做日志后面只要写log
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    /**
     1.需要判断 请求的接口路径是否为HandlerMethod(controller方法)
     2.判断token是否为空，如果为空则是未登录
     3.如果token不为空 登录验证 loginService.checkToken
     4.验证成功之后放行
     */
        if (!(handler instanceof HandlerMethod)){
            // handler 可能是 RequestResourceHandler springboot程序 访问静态资源默认去classpath下的static目录去查询 要把这种情况排除
            return true;
        }
        String token = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if (token == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            //让浏览器识别到我们返回的是json
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //是登录状态，放行
        //我们希望在controller中直接获取用户的信息 怎么获取？ ThreadLocal保存用户信息
        //ThreadLocal做线程变量隔离，往个线程中存取数据，这个数据只能这个线程获取，其他线程无法获取这个数据
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 删除ThreadLocal中用完的信息，避免内存泄漏问题
        UserThreadLocal.remove();
    }
}
