package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "collect", autoResultMap = true)
public class Collect {
    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 关联ID（博客ID） */
    private int fid;
    /** 收藏人ID */
    private int userId;
    /** 模块 */
    private String module;
}
