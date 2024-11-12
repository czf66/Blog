package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Log;
import com.example.entity.User;
import com.example.service.LogService;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/log")
public class LogController {
    @Resource
    private LogService logService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;

    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    /**
     * 新增
     */
    @PostMapping
    public Result<?> save(@RequestBody Log log) {
        return Result.success(logService.save(log));
    }

    /**
     * 修改
     */
    @PutMapping
    public Result<?> update(@RequestBody Log log) {
        return Result.success(logService.updateById(log));
    }

    /**
     * 根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        logService.removeById(id);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(logService.getById(id));
    }

    /**
     * 查询所有记录
     */
    @GetMapping
    public Result<?> findAll() {
        return Result.success(logService.list());
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                              @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Log> query = Wrappers.<Log>lambdaQuery().orderByDesc(Log::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Log::getContent, name);
        }
        return Result.success(logService.page(new Page<>(pageNum, pageSize), query));
    }

}
