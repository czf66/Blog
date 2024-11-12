package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_blog", autoResultMap = true)
public class Blog {
    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 标题 */
    private String title;
    /** 内容 */
    private String content;
    /** 简介 */
    private String descr;
    /** 封面 */
    private String cover;
    /** 标签 */
    private String tags;
    /** 发布人ID */
    private Integer userId;
    /** 发布日期 */
    private String date;
    /** 浏览量 */
    private Integer readCount;
    /** 分类ID */
    private Integer categoryId;

    /** 分类名称 */
    private String categoryName;
    /** 发布人 */
    private String userName;

    /** 点赞数量 */
    private Integer LikesCount;
    /** 该登录用户是否点赞 */
    @TableField(exist = false) //表明数据库没有该字段
    private boolean UserLikes;
    /** 收藏数量 */
    private Integer CollectCount;
    /** 发布文章数量 */
    private Integer BlogCount;
    /** 该登录用户是否收藏 */
    @TableField(exist = false) //表明数据库没有该字段
    private boolean UserCollect;
    /** 该登录用户是否点赞 */
    private String status;
}
