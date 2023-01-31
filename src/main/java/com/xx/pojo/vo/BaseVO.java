package com.xx.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class BaseVO {
    private Long id;

    @TableField(value = "is_enabled")
    private Boolean enabled;

    private Date updateTime;
    private Date createTime;
    private Long createBy;
    String createByNickname;
    @TableField(value = "is_deleted")
    private Boolean deleted;
}
