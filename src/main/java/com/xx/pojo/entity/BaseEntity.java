package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.xx.service.UserService;
import com.xx.util.SessionUtil;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

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

    public BaseEntity() {
        this.createBy = SessionUtil.getUser().getId();
    }
}
