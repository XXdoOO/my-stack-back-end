package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "star")
public class Star extends BaseEntity {
    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "blog_id")
    private Long blogId;
}
