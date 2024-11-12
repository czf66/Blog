package com.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Comment;
import com.example.entity.Message;
import com.example.entity.User;
import com.example.service.LogService;
import com.example.service.MessageService;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Resource
    private MessageService messageService;
    @Resource
    private UserService userService;
    @Resource
    HttpServletRequest request;

    public User getUser() {
        String token = request.getHeader("token");
        String tokenName = JWT.decode(token).getAudience().get(0);
        String username = tokenName.split("-")[1];
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    /**
     * 新增
     */
    @PostMapping
    public Result<?> save(@RequestBody Message message) {
        message.setTime(DateUtil.formatDateTime(new Date()));
        return Result.success(messageService.save(message));
    }

    /**
     * 修改
     */
    @PutMapping
    public Result<?> update(@RequestBody Message message) {
        message.setTime(DateUtil.formatDateTime(new Date()));
        return Result.success(messageService.updateById(message));
    }

    /**
     * 根据ID删除
     */
    @DeleteMapping("/remove/{id}")
    public Result<?> delete(@PathVariable Long id) {
        messageService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("remove/ids")
    public Result<?> removeByIds(@RequestBody List<Integer> ids){
        messageService.removeByIds(ids);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(messageService.getById(id));
    }

    /**
     * 查询所有记录
     */
    @GetMapping
    public Result<?> findAll() {
        return Result.success(messageService.list());
    }

    /**
     * 生成回复列表
     */
    @GetMapping("/foreign")
    public Result<?> findByForeign(Message message) {
        return Result.success(messageService.findByForeign(message));
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Message> query = Wrappers.<Message>lambdaQuery().like(Message::getContent, name).orderByAsc(Message::getId);
        return Result.success(messageService.page(new Page<>(pageNum, pageSize), query));
    }

//    /**
//     * 分页查询
//     */
//    @GetMapping("/page")
//    public Result<IPage<Message>> findPage(@RequestParam(required = false, defaultValue = "") String content,
//                                           @RequestParam(required = false, defaultValue = "1") Integer pageNum,
//                                           @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
//        IPage page = new Page<>(pageNum,pageSize);
//        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
//
//        return Result.success(messageService.page(page, queryWrapper.lambda().like(Message::getContent,content)));
////        return Result.success(categoryService.page(new Page<>(pageNum, pageSize), Wrappers.<Category>lambdaQuery().like(Category::getName, name)));
//    }

}
