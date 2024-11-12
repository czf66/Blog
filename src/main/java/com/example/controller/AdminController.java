package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Admin;
import com.example.entity.Category;
import com.example.service.AdminService;
import com.example.service.LogService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员前端操作接口
 **/
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Resource
    private LogService logService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.save(admin);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        Admin admin = adminService.getById(id);
        logService.log(StrUtil.format("更新用户：{} ", admin.getUsername()));
        adminService.removeById(id);
        return Result.success();
    }


    /**
     * 修改
     */
    @PutMapping
    public Result updateById(@RequestBody Admin admin) {
        logService.log(StrUtil.format("更新用户：{} ", admin.getUsername()));
        return Result.success(adminService.updateById(admin));
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin = adminService.getById(id);
        return Result.success(admin);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Admin> list = adminService.list();
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<IPage<Category>> findPage(@RequestParam(required = false, defaultValue = "") String name,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage page = new Page<>(pageNum,pageSize);
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        return Result.success(adminService.page(page, queryWrapper.lambda().like(Admin::getUsername,name)));
    }

}
