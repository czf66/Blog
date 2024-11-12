package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Comment;
import com.example.entity.Message;
import com.example.entity.User;
import com.example.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserService userService;

    public List<Comment> selectAll(Comment comment){
        List<Comment> commentList  = commentMapper.selectAll(comment);
        return commentList ;
    }

    /**
     * 生成回复列表
     */
    public List<Comment> selectForUser(Comment comment){
        // 查询没有回复的评论
        List<Comment> commentList  = commentMapper.selectForUser(comment);
        for (Comment c : commentList) {  // 查询回复列表
            Comment param = new Comment();
            // 将没有评论的ID赋予
            param.setRootId(c.getId());
            // 依次查询有回复的评论
            List<Comment> children = this.selectAll(param);
            children = children.stream().filter(child -> !child.getId().equals(c.getId())).collect(Collectors.toList());  // 排除当前查询结果里最外层节点
            c.setChildren(children);
        }
        return commentList ;
    }
}
