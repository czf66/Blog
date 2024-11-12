package com.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Blog;
import com.example.entity.Category;
import com.example.entity.Collect;
import com.example.entity.Likes;
import com.example.service.BlogService;
import com.example.service.CollectService;
import com.example.service.LikesService;
import com.example.service.LogService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @Resource
    private LogService logService;

    @Resource
    private LikesService likesService;

    @Resource
    private CollectService collectService;

    @Resource
    private HttpServletRequest request;

    /**
     * 新增
     */
    @PostMapping("/save")
    public Result<?> save(@RequestBody Blog blog){
        logService.log(StrUtil.format("新增博客信息：{}",blog.getTitle()));
        blog.setDate(DateUtil.formatDateTime(new Date()));
        String status = "待审核";
        blog.setStatus(status);
        return Result.success(blogService.save(blog));
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Blog blog) {
        System.out.println("----------------已修改");
        blog.setDate(DateUtil.formatDateTime(new Date()));
        blog.setStatus("待审核");
        blogService.updateById(blog);
        return Result.success();
    }

    /**
     * 删除一个记录
     */
    @DeleteMapping("remove/{id}")
    public Result<?> removeById(@PathVariable long id){
        Blog blog = blogService.getById(id);
        logService.log(StrUtil.format("删除博客信息：{}",blog.getTitle()));

        blogService.removeById(id);
        return Result.success();
    }

    /**
     * 审核
     */
    @GetMapping("/audit/{id}")
    public Result<?> audit(@PathVariable long id){
        Blog blog = blogService.getById(id);
        System.out.println(blog);
        blog.setStatus("审核通过");

        blogService.updateById(blog);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("remove/ids")
    public Result<?> removeByIds(@RequestBody List<Integer> ids){
        Collection<Blog> blogs = blogService.listByIds(ids);
        for (Blog blog : blogs){
            System.out.println(blog.getTitle());
            logService.log(StrUtil.format("删除博客信息：{}",blog.getTitle()));
        }
        blogService.removeByIds(ids);
        return Result.success();
    }

    /**
     * 根据分类ID查询
     */
    @GetMapping("/category/{categoryId}/{id}")
    public Result<?> findByCategoryId(@PathVariable long categoryId,
                                      @PathVariable long id){
        // 根据分类ID查询博客数据并将当前的博客ID排除
        System.out.println("------------"+id);
        List<Blog> blogList = blogService.findByCategoryId(categoryId,id);
        // 查询每个博客的点赞数量
        for (Blog blog : blogList){
            LambdaQueryWrapper<Likes> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Likes::getFid,blog.getId());
            lambdaQueryWrapper.eq(Likes::getModule,"博客");
            int likesCount = likesService.count(lambdaQueryWrapper);
            blog.setLikesCount(likesCount);
        }
        return Result.success(blogList);
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable long id){
        // 根据ID查询博客数据
        Blog blog = blogService.findById(id);
        // 查询当前博客点赞数据
        LambdaQueryWrapper<Likes> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询当前博客
        lambdaQueryWrapper.eq(Likes::getFid,id);
        lambdaQueryWrapper.eq(Likes::getModule,"博客");
        // 根据博客ID以及分类进行查询点赞数量
        int likesCount = likesService.count(lambdaQueryWrapper);
        // 赋予点赞数量
        blog.setLikesCount(likesCount);
        // 获取登录用户ID
        int userId = (Integer.parseInt(request.getHeader("id")));
        // 根据博客ID、登录用户ID、以及分类进行查询该用户是否在该博客页面点过赞
        Likes userLikes = likesService.getOne(Wrappers.<Likes>lambdaQuery().eq(Likes::getFid,id).eq(Likes::getUserId,userId).eq(Likes::getModule,"博客"));
        // 给blog实体赋值（前端通过该值进行样式改变）
        blog.setUserLikes(userLikes != null);

        // 查询当前博客收藏数据
        LambdaQueryWrapper<Collect> lambdaQuery = new LambdaQueryWrapper<>();
        // 查询当前博客
        lambdaQuery.eq(Collect::getFid,id);
        lambdaQuery.eq(Collect::getModule,"博客");
        // 根据博客ID以及分类进行查询收藏数量
        int CollectCount = collectService.count(lambdaQuery);
        // 赋予收藏数量
        blog.setCollectCount(CollectCount);
        // 获取登录用户ID
        int CollectUserId = (Integer.parseInt(request.getHeader("id")));
        // 根据博客ID、登录用户ID、以及分类进行查询该用户是否在该博客页面收藏过
        Collect CollectUserLikes = collectService.getOne(Wrappers.<Collect>lambdaQuery().eq(Collect::getFid,id).eq(Collect::getUserId,CollectUserId).eq(Collect::getModule,"博客"));
        // 给blog实体赋值（前端通过该值进行样式改变）
        blog.setUserCollect(CollectUserLikes != null);

        // 点进去一次博客详细页则浏览量+1
        blog.setReadCount(blog.getReadCount() + 1);
        // 修改浏览量数据
        blogService.updateById(blog);

        return Result.success(blog);
    }

    /**
     * 根据页面作者查询发布博客数量、点赞量与收藏量
     */
    @GetMapping("/author/{userId}")
    public Result selectAuthor(@PathVariable long userId){
//        long userId = (Integer.parseInt(request.getHeader("id")));
        System.out.println("****"+userId);

        LambdaQueryWrapper<Blog> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(Blog::getUserId,userId);
        int blogCount = blogService.count(lambdaQuery);

        LambdaQueryWrapper<Likes> likesQuery = new LambdaQueryWrapper<>();
        likesQuery.eq(Likes::getUserId,userId);
        int likesCount = likesService.count(likesQuery);

        LambdaQueryWrapper<Collect> collectQuery = new LambdaQueryWrapper<>();
        collectQuery.eq(Collect::getUserId,userId);
        int collectCount = collectService.count(collectQuery);

        Blog blog = new Blog();
        blog.setBlogCount(blogCount);
        blog.setLikesCount(likesCount);
        blog.setCollectCount(collectCount);

        return Result.success(blog);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result selectPage(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        System.out.println(blog.getCategoryName());
        PageInfo<Blog> page = blogService.selectPage(blog, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 分页查询-是否为审核通过的
     */
    @GetMapping("/pageAudit")
    public Result selectAudit(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        System.out.println(blog.getCategoryName());
        PageInfo<Blog> page = blogService.selectAudit(blog, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 查询所有并根据浏览量排序
     */
    @GetMapping("/page/readCount")
    public Result selectAllByReadCount(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        System.out.println(blog.getCategoryName());
        PageInfo<Blog> page = blogService.selectAllByReadCount(blog, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 查询所有并根据发布时间排序
     */
    @GetMapping("/page/date")
    public Result selectAllByDate(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        System.out.println(blog.getCategoryName());
        PageInfo<Blog> page = blogService.selectAllByDate(blog, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 分页查询点赞过的博客
     */
    @GetMapping("/likes")
    public Result selectLike(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Blog> page = blogService.selectLike(blog, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 分页查询收藏过的博客
     */
    @GetMapping("/collect")
    public Result selectCollect(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Blog> page = blogService.selectCollect(blog, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 分页查询评论过的博客
     */
    @GetMapping("/comment")
    public Result selectComment(Blog blog,
                                @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Blog> page = blogService.selectComment(blog, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 博客榜单
     */
    @GetMapping("/selectTop")
    public Result selectTop() {
        List<Blog> list = blogService.selectTop();
        return Result.success(list);
    }


}
