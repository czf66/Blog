package com.example.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Likes;
import com.example.mapper.LikesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LikesService extends ServiceImpl<LikesMapper, Likes> {

    @Resource
    private LikesMapper likesMapper;
}
