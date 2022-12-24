package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "is_deleted")
    @TableLogic
    private Boolean deleted;
}
