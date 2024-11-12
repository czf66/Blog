package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Blog;
import com.example.entity.Likes;
import com.example.mapper.BlogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogService extends ServiceImpl<BlogMapper, Blog> {

    @Resource
    private BlogMapper blogMapper;

    @Resource
    private LikesService likesService;


    /**
     * 修改
     * @return
     */
    public boolean updateById(Blog blog) {
        blogMapper.updateById(blog);
        return false;
    }

    /**
     * 查询某个博客数据
     * @return
     */
    public Blog findById(Long id) {
        Blog blog = blogMapper.findById(id);
        return blog;
    }

    /**
     * 根据分类ID查询
     */
    public List<Blog> findByCategoryId(Long categoryId,Long id) {
        List<Blog> blogList = blogMapper.findByCategoryId(categoryId,id);
        return blogList;
    }

    /**
     * 查询各个博客的点赞数据
     */
    private PageInfo<Blog> getBlogPageInfo(List<Blog> list) {
        // 将点赞的数据关联ID与每一个博客ID进行对比，并赋值到博客详细数据中，即可得知每个博客的点赞量
        for(Blog b : list){
            LambdaQueryWrapper<Likes> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Likes::getFid,b.getId());
            int likesCount = likesService.count(lambdaQueryWrapper);
            b.setLikesCount(likesCount);
        }
        return PageInfo.of(list);
    }

    /**
     * 查询所有
     */
    public PageInfo<Blog> selectPage(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 获取全部博客数据
        List<Blog> list = blogMapper.selectAll(blog);
        return getBlogPageInfo(list);
    }

    /**
     * 查询所有
     */
    public PageInfo<Blog> selectAudit(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 获取全部博客数据
        List<Blog> list = blogMapper.selectAudit(blog);
        return getBlogPageInfo(list);
    }


    /**
     * 查询所有并根据浏览量排序
     */
    public PageInfo<Blog> selectAllByReadCount(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 获取全部博客数据
        List<Blog> list = blogMapper.selectAllByReadCount(blog);
        return getBlogPageInfo(list);
    }

    /**
     * 查询所有并根据发布时间排序
     */
    public PageInfo<Blog> selectAllByDate(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 获取全部博客数据
        List<Blog> list = blogMapper.selectAllByDate(blog);
        return getBlogPageInfo(list);
    }

    /**
     * 查询所有点赞过的博客
     */
    public PageInfo<Blog> selectLike(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 获取全部博客数据
        List<Blog> list = blogMapper.selectLike(blog);
        return getBlogPageInfo(list);
    }

    /**
     * 查询所有收藏过的博客
     */
    public PageInfo<Blog> selectCollect(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 获取全部博客数据
        List<Blog> list = blogMapper.selectCollect(blog);
        return getBlogPageInfo(list);
    }

    /**
     * 查询所有评论过的博客
     */
    public PageInfo<Blog> selectComment(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 获取全部博客数据
        List<Blog> list = blogMapper.selectComment(blog);
        return getBlogPageInfo(list);
    }

    public List<Blog> selectTop() {
//        List<Blog> blogList = blogMapper.selectAll(null);
//        blogList = blogList.stream().sorted((b1, b2) -> b2.getReadCount().compareTo(b1.getReadCount()))
//                .limit(20)
//                .collect(Collectors.toList());
        List<Blog> blogs = blogMapper.selectAllByReadCount(null);
        return blogs;
    }


}
