package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.example.common.handler.ListHandler;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "t_user", autoResultMap = true)
public class User extends Account {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;
    private String nickName;

    private String password;

    private String email;

    private String phone;

    private String avatar;
    private String address;
    private String age;

    @TableField(exist = false)
    private String token;

    /** 角色标识 */
    private String role;

    /**
     * 权限
     */
//    @TableField(typeHandler = ListHandler.class)
//    private List<Long> role;

    @TableField(exist = false)
    private List<Long> permission;

}
