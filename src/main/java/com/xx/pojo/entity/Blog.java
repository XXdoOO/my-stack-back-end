package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "blog")
public class Blog extends BaseEntity {
    @TableField(value = "title")
    private String title;

    @TableField(value = "description")
    private String description;

    @TableField(value = "cover")
    private String cover;

    @TableField(value = "content")
    private String content;

    @TableField(value = "views")
    private Long views;

    @TableField(value = "author_id")
    private Long authorId;

    @TableField(value = "status")
    private Boolean status;

    @TableField(value = "ip")
    private String ip;
}
