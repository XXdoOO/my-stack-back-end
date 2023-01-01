package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "disable")
public class Disable extends BaseEntity {
    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "end_time")
    private Date endTime;

    @TableField(value = "reason")
    private String reason;
}
