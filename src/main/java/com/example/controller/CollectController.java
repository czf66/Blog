package com.example.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.common.Result;
import com.example.entity.Collect;
import com.example.entity.Likes;
import com.example.service.CollectService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/collect")
public class CollectController {
    @Resource
    private CollectService collectService;

    @Resource
    private HttpServletRequest request;

    // 收藏和取消
    @PostMapping("/set")
    public Result<?> set(@RequestBody Collect collect){
        // 获取登录用户ID
        int userId = (Integer.parseInt(request.getHeader("id"))) ;
        System.out.println(userId);
        collect.setUserId(userId);
        //查询用户是否收藏过
        Collect dbCollect = collectService.getOne(Wrappers.<Collect>lambdaQuery().eq(Collect::getFid,collect.getFid())
                                                                                 .eq(Collect::getUserId,collect.getUserId())
                                                                                 .eq(Collect::getModule,collect.getModule()));
        // 如果没收藏过，则添加收藏数据
        if (dbCollect == null){
            collectService.save(collect);
        }else {
            // 如果收藏过，则删除收藏数据
            collectService.removeById(dbCollect.getId());
        }
        return Result.success();
    }
}
