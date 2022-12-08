package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseEntity {
    @TableId
    private Long id;

    @TableField(value = "update_time")
    private Timestamp updateTime;

    @TableField(value = "create_time")
    private Timestamp createTime;

    @TableField(value = "is_deleted")
    private Timestamp deleted;
}
