package com.mszlu.blog.admin.service;

import com.mszlu.blog.admin.pojo.Admin;
import com.mszlu.blog.admin.pojo.Permission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
public class AuthService {
    @Autowired
    private AdminService adminService;
    //我们获取当前的登陆用户，有权限和admin的关联表，通过admin_id查找到permission_id在权限表中
    //通过权限id查找path路径，根据请求路径和path路径进行比较比对成功则认为有权限访问
    public boolean auth(HttpServletRequest request, Authentication authentication) {
        //请求路径
        String requestURI = request.getRequestURI();
        log.info("request url:{}", requestURI);
        // true代表放行 false 代表拦截
        //拿到用户信息
        Object principal = authentication.getPrincipal();
        if (principal == null || "anonymousUser".equals(principal)) {
            // 未登录
            return false;
        }
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Admin admin = adminService.findAdminByUserName(username);
        if (admin == null) {
            return false;
        }
        if (admin.getId() == 1) {
            // 认为是超级管理员
            return true;
        }
        List<Permission> permissions = adminService.findPermissionsByAdminId(admin.getId());
        //去除参数部分
        requestURI = StringUtils.split(requestURI, '?')[0];
        for (Permission permission : permissions) {
            if (requestURI.equals(permission.getPath())) {
                log.info("权限通过");
                return true;
            }
        }
        return false;
    }
}
