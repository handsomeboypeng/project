package com.mszlu.blog.controller;

import com.mszlu.blog.service.LoginService;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {
    //什么服务就用什么service，低耦合
    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParams loginParams){
        //登陆 验证用户 访问用户表
        return loginService.login(loginParams);
    }
}
