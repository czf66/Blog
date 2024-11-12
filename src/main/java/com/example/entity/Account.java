package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 角色用户父类
 */
@Data
public class Account {
    private Long id;
    /** 用户名 */
    private String username;
    /** 名称 */
    private String nickName;
    /** 密码 */
    private String password;
    /** 角色标识 */
    private String role;
    /** 新密码 */
    @TableField(exist = false)
    private String newPassword;
    /** 头像 */
    private String avatar;

    @TableField(exist = false)
    private String token;

}
