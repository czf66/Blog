package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.Log;
import com.example.entity.User;
import com.example.mapper.LogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class LogService extends ServiceImpl<LogMapper, Log> {

    @Resource
    private LogMapper logMapper;

    @Resource
    private HttpServletRequest request;

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    public Account getUser() {
        try {
            String token = request.getHeader("token");
            String tokenName = JWT.decode(token).getAudience().get(0);
            String username = tokenName.split("-")[1];
            String role = tokenName.split("-")[0];
            Account account = null;
            if (RoleEnum.ADMIN.name().equals(role)){
                account = adminService.getOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername, username));
            }
            else if (RoleEnum.USER.name().equals(role)) {
                account = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
            }
            return account;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日志记录
     *
     * @param content
     */
    public void log(String content) {
        Log log = new Log();
        log.setUser(getUser().getUsername());
        log.setTime(DateUtil.formatDateTime(new Date()));
        log.setIp(getIpAddress());
        log.setContent(content);
        save(log);
    }

    /**
     * 日志记录
     *
     * @param username
     * @param content
     */
    public void log(String username, String content) {
        Log log = new Log();
        log.setUser(username);
        log.setTime(DateUtil.formatDateTime(new Date()));
        log.setIp(getIpAddress());
        log.setContent(content);
        save(log);
    }


    /**
     * 描述：获取IP地址
     */
    public String getIpAddress() {

        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
