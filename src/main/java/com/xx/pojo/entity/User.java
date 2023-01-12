package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "is_enabled")
    private Boolean enabled;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "is_deleted")
    @TableLogic
    private Boolean deleted;

    @TableField(condition = SqlCondition.LIKE)
    private String email;
    private String password;

    @TableField(condition = SqlCondition.LIKE)
    private String nickname;
    private String avatar;
    private String ip;

    @TableField(condition = SqlCondition.LIKE)
    private String ipTerritory;

    @TableField(value = "is_admin")
    private Boolean admin;
}
