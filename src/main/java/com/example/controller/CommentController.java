package com.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Blog;
import com.example.entity.Category;
import com.example.entity.Comment;
import com.example.service.CommentService;
import com.example.service.LogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private LogService logService;

    /**
     * 新增
     */
    @PostMapping("/save")
    public Result<?> save(@RequestBody Comment comment){
        logService.log(StrUtil.format("新增评论{}",comment));
        comment.setTime(DateUtil.formatDateTime(new Date()));
        return Result.success(commentService.save(comment));
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody Comment comment){
        logService.log(StrUtil.format("新增博客分类：{}",comment));
        return Result.success(commentService.updateById(comment));
    }

    /**
     * 删除一个记录
     */
    @DeleteMapping("/remove/{id}")
    public Result<?> deleteById(@PathVariable long id){
        Comment comment = commentService.getById(id);
        logService.log(StrUtil.format("删除评论：{}",comment));

        commentService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("remove/ids")
    public Result<?> removeByIds(@RequestBody List<Integer> ids){
        Collection<Comment> comments = commentService.listByIds(ids);
        for (Comment comment : comments){
            System.out.println(comment);
            logService.log(StrUtil.format("删除博客信息：{}",comment));
        }


        commentService.removeByIds(ids);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result<Comment> findById(@PathVariable Long id) {
        return Result.success(commentService.getById(id));
    }

    @GetMapping("/page")
    public Result<IPage<Comment>> findPage(@RequestParam(required = false, defaultValue = "") String content,
                                            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        IPage page = new Page<>(pageNum,pageSize);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();

        return Result.success(commentService.page(page, queryWrapper.lambda().like(Comment::getContent,content)));
//        return Result.success(categoryService.page(new Page<>(pageNum, pageSize), Wrappers.<Category>lambdaQuery().like(Category::getName, name)));
    }

    /**
     * 查询评论数量
     */
    @GetMapping("/selectAll")
    public Result selectAll(Comment comment) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getFid, comment.getFid());
        int count = commentService.count(lambdaQueryWrapper);
        return Result.success(count);
    }

    /**
     * 查询所有评论
     */
    @GetMapping("/selectForUser")
    public Result selectForUser(Comment comment) {
        List<Comment> list = commentService.selectForUser(comment);
        return Result.success(list);
    }

}
