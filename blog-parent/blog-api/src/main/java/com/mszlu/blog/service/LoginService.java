package com.mszlu.blog.service;

import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.LoginParams;

public interface LoginService {
    /**
     * 登陆功能
     * @param loginParams
     * @return
     */
    Result login(LoginParams loginParams);

    /**
     * 校验token
     * @param token token
     * @return 校验成功返回用户信息
     */
    SysUser checkToken(String token);

    /**
     * 退出功能
     * @param loginParams
     * @return
     */
    Result logout(String token);

    /**
     * 注册
     * @param loginParams
     * @return
     */
    Result register(LoginParams loginParams);

}
