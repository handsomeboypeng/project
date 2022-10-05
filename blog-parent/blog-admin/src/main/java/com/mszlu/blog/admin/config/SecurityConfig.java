package com.mszlu.blog.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration  //标识为一个配置类让spring能够扫描到
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean//密码配置 代表spring-security密码策略是用的BCrypt这种
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        //加密策略 MD5 不安全 彩虹表  MD5 加盐
        String mszlu = new BCryptPasswordEncoder().encode("mszlu");
        System.out.println(mszlu);
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests() //开启登录认证
//                .antMatchers("/user/findAll").hasRole("admin") //访问接口需要admin的角色
                .antMatchers("/css/**").permitAll()//css放行 不需要登陆也可以进行访问
                .antMatchers("/img/**").permitAll()//img放行
                .antMatchers("/js/**").permitAll()//js放行
                .antMatchers("/plugins/**").permitAll()//plugin放行
                //权限认证靠auth来实现
                .antMatchers("/admin/**").access("@authService.auth(request,authentication)") //自定义service 来去实现实时的权限认证
                .antMatchers("/pages/**").authenticated()//只要登陆成功就可以
                .and().formLogin()
                .loginPage("/login.html") //自定义的登录页面
                .loginProcessingUrl("/login") //登录处理接口
                .usernameParameter("username") //定义登录时的用户名的key 默认为username
                .passwordParameter("password") //定义登录时的密码key，默认是password
                .defaultSuccessUrl("/pages/main.html")//登陆成功的页面
                .failureUrl("/login.html")
                .permitAll() //通过 不拦截，更加前面配的路径决定，这是指和登录表单相关的接口 都通过
                .and().logout() //退出登录配置
                .logoutUrl("/logout") //退出登录接口
                .logoutSuccessUrl("/login.html")
                .permitAll() //退出登录的接口放行
                .and()
                .httpBasic()
                .and()
                .csrf().disable() //csrf关闭 如果自定义登录 需要关闭
                .headers().frameOptions().sameOrigin();//支持iframe页面嵌套

        //配置完以后需要做两件事情 1.加登陆认证 SecurityUserService 2.写authservice完成权限控制
    }
}
