package com.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.example.common.Result;
import com.example.entity.Comment;
import com.example.service.UserService;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import com.example.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;

//    public User getUser() {
//        String token = request.getHeader("token");
//        String username = JWT.decode(token).getAudience().get(0);
//        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
//    }


    /**
     * 新增
     */
    @PostMapping
    public Result<?> save(@RequestBody Notice notice) {
        notice.setTime(DateUtil.formatDateTime(new Date()));
        return Result.success(noticeService.save(notice));
    }

    /**
     * 修改
     */
    @PutMapping
    public Result<?> update(@RequestBody Notice notice) {
        notice.setTime(DateUtil.formatDateTime(new Date()));
        return Result.success(noticeService.updateById(notice));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        noticeService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("remove/ids")
    public Result<?> removeByIds(@RequestBody List<Integer> ids){
        noticeService.removeByIds(ids);
        return Result.success();
    }


    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(noticeService.getById(id));
    }

    /**
     * 查询所有记录
     */
    @GetMapping
    public Result<?> findAll() {
        return Result.success(noticeService.list());
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Notice> query = Wrappers.<Notice>lambdaQuery().like(Notice::getTitle, name).orderByDesc(Notice::getTime);;
        return Result.success(noticeService.page(new Page<>(pageNum, pageSize), query));
    }

}
