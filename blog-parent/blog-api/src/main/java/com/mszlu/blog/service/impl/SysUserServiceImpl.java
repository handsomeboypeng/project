package com.mszlu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mszlu.blog.dao.mapper.SysUserMapper;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.LoginService;
import com.mszlu.blog.service.SysUserService;
import com.mszlu.blog.vo.ErrorCode;
import com.mszlu.blog.vo.LoginUserVO;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("码路博客");
        }
        return sysUser;
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname(" ");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         1.token合法性校验
         是否为空，解析是否成功，redis中是否存在
         2.如果校验失败，返回错误信息
         3.如果校验成功，返回对应的结果 LoginUserVO
         */
        SysUser sysUser = loginService.checkToken(token);
        // 如果返回为空，则token不合法
        if (sysUser == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setAccount(sysUser.getAccount());
        loginUserVO.setAvatar(sysUser.getAvatar());
        loginUserVO.setId(String.valueOf(sysUser.getId()));
        loginUserVO.setNickname(sysUser.getNickname());
        // BeanUtils.copyProperties(sysUser, loginUserVO);
        return Result.success(loginUserVO);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        //保存用户这个id会自动生成
        //这个地方 默认生成的id是 分布式id 雪花算法
        this.sysUserMapper.insert(sysUser);

    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        // 查询该用户的密码
        queryWrapper.select(SysUser::getId, SysUser::getAccount, SysUser::getPassword,  SysUser::getAvatar, SysUser::getNickname);
        queryWrapper.last("LIMIT 1");
        // 执行查询得到用户的密码和加密盐值
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }

}
