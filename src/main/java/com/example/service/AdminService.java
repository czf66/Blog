package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 管理员业务处理
 **/
@Service
public class AdminService extends ServiceImpl<AdminMapper,Admin> {

    @Resource
    private AdminMapper adminMapper;

    /**
     * 登录
     */
    public Account login(Account account) {
        LambdaQueryWrapper<Admin> queryWrapper = Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername, account.getUsername());
        Admin one = getOne(queryWrapper);
        if (ObjectUtil.isNull(one)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(one.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 将管理员中的role角色标识以及用户名username保存到token中
        String tokenData = one.getRole() + "-" + one.getUsername();
        String token = JWT.create().withAudience(tokenData)
                                    .sign(Algorithm.HMAC256(one.getPassword())); //以 password 作为 token 的密钥
        one.setToken(token);
        return one;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        Admin admin = new Admin();
        BeanUtils.copyProperties(account, admin);
        save(admin);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Admin dbAdmin = adminMapper.selectById(account.getId());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbAdmin.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbAdmin.setPassword(account.getNewPassword());
        adminMapper.updateById(dbAdmin);
    }

}