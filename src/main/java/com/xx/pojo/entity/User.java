package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class User extends BaseEntity {
    private String email;
    private String password;

    @TableField(condition = SqlCondition.LIKE)
    private String nickname;
    private String avatar;
    private String ip;
    private String ipTerritory;

    @TableField(value = "is_admin")
    private Boolean admin;

    @TableField(value = "is_disable")
    private Boolean disable;
}
