package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "t_comment",autoResultMap = true)
public class Comment {
    /** ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /** 内容 */
    private String content;
    /** 评论人 */
    private Integer userId;
    /** 父级ID */
    private Integer pid;
    /** 根节点ID */
    private Integer rootId;
    /** 评论时间 */
    private String time;


    /** 博客ID */
    private Integer fid;
    /** 模块 */
    private String module;
    /** 用户名 */
    private String userName;
    /** 用户头像 */
    private String avatar;
    /** 回复给谁 就是谁的名称 */
    private String replyUser;  // 回复给谁 就是谁的名称

    @TableField(exist = false) //表明数据库没有该字段
    private List<Comment> children;

}
