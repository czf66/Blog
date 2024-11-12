package com.example.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.UserService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Resource
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1.从http请求的header中获取token
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            throw new CustomException("401", "未获取到token, 请重新登录");
        }
        String tokenName;
        String role;
        Account account = null;
        try {
            tokenName = JWT.decode(token).getAudience().get(0);
            // 根据'-'将数据分离开来，[1]代表拿的是'-'后面的数据
            String username = tokenName.split("-")[1];
            System.out.println(username);
            // 根据'-'将数据分离开来，[0]代表拿的是'-'前面的数据
            role = tokenName.split("-")[0];
            System.out.println(role);
            if (RoleEnum.ADMIN.name().equals(role)){
                account = adminService.getOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername, username));
            }
            else if (RoleEnum.USER.name().equals(role)) {
                account = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
            }
        } catch (JWTDecodeException j) {
            throw new CustomException("401", "权限验证失败, 请重新登录");
        }
        System.out.println("///////////");
        System.out.println(token);
        if (account == null) {
            throw new CustomException("401", "用户不存在, 请重新登录");
        }
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new CustomException("401", "token不合法, 请重新登录");
        }

        return true;
    }

}
