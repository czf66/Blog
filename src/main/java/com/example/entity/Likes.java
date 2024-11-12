package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "likes", autoResultMap = true)
public class Likes {
    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 关联ID（博客ID） */
    private int fid;
    /** 点赞人ID */
    private int userId;
    /** 模块 */
    private String module;
}
