package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Blog;
import com.example.entity.Category;
import com.example.entity.Permission;
import com.example.entity.Role;
import com.example.service.CategoryService;
import com.example.service.LogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private LogService logService;

    /**
     * 新增
     */
    @PostMapping("/save")
    public Result<?> save(@RequestBody Category category){
        logService.log(StrUtil.format("新增博客分类：{}",category.getName()));
        return Result.success(categoryService.save(category));
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody Category category){
        logService.log(StrUtil.format("修改博客分类：{}",category.getName()));
        return Result.success(categoryService.updateById(category));
    }

    /**
     * 删除一个记录
     */
    @DeleteMapping("/remove/{id}")
    public Result<?> removeById(@PathVariable long id){
        Category category = categoryService.getById(id);
        logService.log(StrUtil.format("删除博客分类：{}",category.getName()));

        categoryService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/remove/ids")
    public Result<?> removeByIds(@RequestBody List<Integer> ids){
//        Collection<Category> Categorys = categoryService.listByIds(ids);
//        Category category = (Category) Categorys;
        Collection<Category> categories = categoryService.listByIds(ids);
        logService.log(StrUtil.format("删除博客分类：{}",categories));

        categoryService.removeByIds(ids);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result<Category> findById(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }

    /**
     * 查询所有
     */
    @GetMapping
    public Result<List<Category>> findAll() {
        return Result.success(categoryService.list());
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<IPage<Category>> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                              @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        IPage page = new Page<>(pageNum,pageSize);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();

        return Result.success(categoryService.page(page, queryWrapper.lambda().like(Category::getName,name)));
//        return Result.success(categoryService.page(new Page<>(pageNum, pageSize), Wrappers.<Category>lambdaQuery().like(Category::getName, name)));
    }
}
