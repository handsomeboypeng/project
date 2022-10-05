package com.mszlu.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.LoginService;
import com.mszlu.blog.service.SysUserService;
import com.mszlu.blog.utils.JWTUtils;
import com.mszlu.blog.vo.ErrorCode;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.LoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SysUserService sysUserService;
    //redis
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String slat = "mszlu!@#";

    /*
     * 登录
     * */
    @Override
    public Result login(LoginParams loginParams) {
        /*
        	1.检查参数是否合法
        	2.根据用户名和密码去SysUser表中查询是否存在
        	3.如果不存在则登陆失败
        	4.如果存在，使用jwt，生成token，返回给前端
        	5.token放入redis中，token:user信息，设置过期时间(登录认证的时候，先认证token字符串是否合法，去redis认证是否存在)
        */
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        // 如果用户名或者密码有空值，则报用户名或密码为空异常
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password+slat);
        SysUser sysUser = sysUserService.findUser(account,password);
        // 查询结果为空或者用户已被删除的话 则报用户名不存在异常
        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_NOT_EXIST.getMsg());
        }

        // 登录成功，使用JWT生成token，返回token和redis中
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
    }

    /*
     * 校验token
     * */
    @Override
    public SysUser checkToken(String token) {
        // 判断token是否为空
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> map = JWTUtils.checkToken(token);
        // 校验JWT中是否存在
        if (map == null) {
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)) {
            return null;
        }
        //把json转换为实体类
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success("logout success");
    }

    @Override
    public Result register(LoginParams loginParams) {
        /**
         1.判断参数是否合法
         2.判断账户是否存在，如果存在则返回该账户已被注册
         3.不存在则注册该用户
         4.生成token
         5.存入redis并返回
         6.加上事务 一旦中间的任何过程出现问题，需要回滚刚注册的信息
         */

        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        String nickName = loginParams.getNickname();
        // 如果用户名或昵称或密码为空，则报为空异常
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickName)) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NICKNAME_IS_NULL.getCode(), ErrorCode.ACCOUNT_PWD_NICKNAME_IS_NULL.getMsg());
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        // 如果已存在该用户名则报用户已存在异常
        if (sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_NICKNAME_EXIST.getCode(), ErrorCode.ACCOUNT_NICKNAME_EXIST.getMsg());
        }
        sysUser = new SysUser();
        // 添加注册信息
        sysUser.setAccount(account);
        sysUser.setNickname(nickName);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAdmin(1);
        sysUser.setDeleted(0);
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        // 保存注册信息
        this.sysUserService.save(sysUser);
        // token
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
    }


}
