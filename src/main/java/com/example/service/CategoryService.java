package com.example.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper,Category> {

    @Resource
    private CategoryMapper categoryMapper;
}
