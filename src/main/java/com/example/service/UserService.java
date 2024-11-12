package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    /**
     * 登录
     */
    public Account login(Account account) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery().eq(User::getUsername, account.getUsername());
        User one = getOne(queryWrapper);
        if (ObjectUtil.isNull(one)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(one.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 将用户中的role角色标识以及用户名username保存到token中
        String tokenData = one.getRole() + "-" + one.getUsername();
        String token = JWT.create().withAudience(tokenData).sign(Algorithm.HMAC256(account.getPassword()));
        one.setToken(token);
        return one;
    }


//    public User login(User user) {
//        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword());
//        User one = getOne(queryWrapper);
//        if (one == null) {
//            throw new CustomException("-1", "账号或密码错误");
//        }
//        List<Long> permissions = getPermissions(one.getId());
//        one.setPermission(permissions);
//        return one;
//    }

    public User register(User user) {
        // 查询用户是否注册过
        User one = getOne((Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername())));
        if (one != null) {
            throw new CustomException("-1", "用户已注册"); // 若已注册过，返回用户已注册
        }
        if (user.getPassword() == null) {
            user.setPassword("123456");
        }
        user.setRole("USER");  // 设计权限、默认普通用户角色
        save(user); // MybatisPlus自带的新增方法，用该方法存入数据库
        return getOne((Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername())));
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectById(account.getId()); // 根据传过来的用户ID查询用户信息
        // 查不到用户，返回用户不存在
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        // 密码跟用户不匹配，返回密码输入错误
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword()); // 将密码重新赋值
        userMapper.updateById(dbUser);
    }

    /**
     * 设置权限
     *
     * @param userId
     * @return
     */
//    public List<Long> getPermissions(Long userId) {
//        Set<Long> permissions = new HashSet<>();
//        User user = getById(userId);
//        List<Long> role = user.getRole();
//        if (role != null) {
//            for (Object roleId : role) {
//                Role realRole = roleService.getById((int) roleId);
//                List<Integer> rolePermissions = realRole.getPermission();
//                for (Integer rolePermission : rolePermissions) {
//                    permissions.add(Long.valueOf(rolePermission + ""));
//                }
//            }
//        }
//        return new ArrayList<>(permissions);
//    }

//    public User findById(Long id) {
//        User user = getById(id);
//        user.setPermission(getPermissions(id));
//        return user;
//    }
}
