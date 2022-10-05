package com.mszlu.blog.service;

import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.UserVo;

public interface SysUserService {

    SysUser findUser(String account,String password);

    /**
     * 根据作者id查询作者信息
     * @param id 作者id
     * @return 符合条件的作者信息
     */
    SysUser findUserById(Long id);

    /**
     * 根据作者id查询作者信息
     * @param id 作者id
     * @return 符合条件的作者信息
     */
    UserVo findUserVoById(Long id);

    /**
     * 根据token查询用户信息
     * @param token token
     * @return 用户信息
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);
}
