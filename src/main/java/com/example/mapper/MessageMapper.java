package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Comment;
import com.example.entity.Message;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {
    /**
     * 查询所有
     * **/
    List<Message> selectAll(Message message);

    /**
     * 查询前台展示的评论信息
     * **/
    List<Message> selectForUser(Message message);
}
