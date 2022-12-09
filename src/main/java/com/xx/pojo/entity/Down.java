package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "down")
public class Down extends BaseEntity{
    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "blog_id")
    private Long blogId;

    @TableField(value = "comment_id")
    private Long commentId;
}