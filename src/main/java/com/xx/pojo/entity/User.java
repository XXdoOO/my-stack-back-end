package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class User extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String email;
    private String password;

    @TableField(condition = SqlCondition.LIKE)
    private String nickname;
    private String avatar;
    private String ip;
    private String ipTerritory;

    @TableField(value = "is_admin")
    private Boolean admin;

    @TableField(value = "is_enabled")
    private Boolean enabled;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Long createBy;

    @TableField(value = "is_deleted")
    @TableLogic
    private Boolean deleted;
}
