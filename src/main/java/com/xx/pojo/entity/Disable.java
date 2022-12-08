package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "disable")
public class Disable extends BaseEntity {
    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "end_time")
    private Long endTime;

    @TableField(value = "reason")
    private String reason;
}
