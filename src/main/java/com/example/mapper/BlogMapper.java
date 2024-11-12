package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Blog;

import java.util.List;

public interface BlogMapper extends BaseMapper<Blog> {
    /**
     * 修改
     */
    int updateById(Blog blog);

    /**
     * 根据博客ID查询
     */
    Blog findById(Long id);

    /**
     * 根据分类ID查询
     */
    List<Blog> findByCategoryId(Long categoryId,Long id);


    /**
     * 查询所有
     */
    List<Blog> selectAll(Blog blog);

    /**
     * 查询所有
     */
    List<Blog> selectAudit(Blog blog);

    /**
     * 查询所有并根据浏览量排序
     */
    List<Blog> selectAllByReadCount(Blog blog);

    /**
     * 查询所有并根据发布时间排序
     */
    List<Blog> selectAllByDate(Blog blog);

    /**
     * 查询点赞的博客
     */
    List<Blog> selectLike(Blog blog);

    /**
     * 查询收藏的博客
     */
    List<Blog> selectCollect(Blog blog);

    /**
     * 查询评论的博客
     */
    List<Blog> selectComment(Blog blog);
}
