package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class User extends BaseEntity {
    @TableField(value = "email")
    private String email;

    @TableField(value = "password")
    private String password;

    @TableField(value = "nickname")
    private String nickname;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "ip")
    private String ip;

    @TableField(value = "is_admin")
    private Boolean admin;

    @TableField(value = "is_disable")
    private Boolean disable;
}
