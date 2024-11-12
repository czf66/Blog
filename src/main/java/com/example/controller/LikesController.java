package com.example.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.common.Result;
import com.example.entity.Likes;
import com.example.entity.User;
import com.example.service.LikesService;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikesController {

    @Resource
    private LikesService likesService;

    @Resource
    private HttpServletRequest request;

    // 点赞和取消
    @PostMapping("/set")
    public Result<?> set(@RequestBody Likes likes){
        // 获取登录用户ID
        int userId = (Integer.parseInt(request.getHeader("id"))) ;
        System.out.println(userId);
        likes.setUserId(userId);
        //查询用户是否点过赞
        Likes dbLikes = likesService.getOne(Wrappers.<Likes>lambdaQuery().eq(Likes::getFid,likes.getFid())
                                                                         .eq(Likes::getUserId,likes.getUserId())
                                                                         .eq(Likes::getModule,likes.getModule()));
        // 如果没点过，则添加点赞数据
        if (dbLikes == null){
            likesService.save(likes);
        }else {
            // 如果点过，则删除点赞数据
            likesService.removeById(dbLikes.getId());
        }
        return Result.success();
    }
}
