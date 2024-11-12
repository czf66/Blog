package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Comment;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询所有
     * **/
    List<Comment> selectAll(Comment comment);

    /**
     * 查询前台展示的评论信息
     * **/
    List<Comment> selectForUser(Comment comment);
}
