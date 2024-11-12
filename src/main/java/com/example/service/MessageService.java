package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Comment;
import com.example.entity.Message;
import com.example.entity.User;
import com.example.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService extends ServiceImpl<MessageMapper, Message> {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private UserService userService;

    public List<Message> selectAll(Message message){
        List<Message> list  = messageMapper.selectAll(message);
        return list ;
    }

    public List<Message> findByForeign(Message message) {
//        LambdaQueryWrapper<Message> queryWrapper = Wrappers.<Message>lambdaQuery().eq(Message::getForeignId, 0).orderByDesc(Message::getId);
//        List<Message> list = list(queryWrapper);
        // 查询没有回复的评论
        List<Message> messageList  = messageMapper.selectForUser(message);
        for (Message message1 : messageList) { // 查询回复列表
            User one = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, message1.getUsername()));
            message1.setAvatar("http://localhost:9999/files/" + one.getAvatar()); // 赋予用户头像
//            Long parentId = message1.getParentId();
//            list.stream().filter(c -> c.getId().equals(parentId)).findFirst().ifPresent(message::setParentMessage);
            Message param = new Message();
            // 将没有评论的ID赋予
            param.setParentId(message1.getId());
            List<Message> children = this.selectAll(param); // 将没有父级ID的ID去跟每个ParentId对比
            children = children.stream().filter(child -> !child.getId().equals(message1.getId())).collect(Collectors.toList());  // 排除当前查询结果里最外层节点
            message1.setChildren(children);
//            List<Message> collect = list.stream().filter(c -> c.getId().equals(parentId)).collect(Collectors.toList());
//            message.setChildren(collect);
        }
        return messageList;
    }

}
