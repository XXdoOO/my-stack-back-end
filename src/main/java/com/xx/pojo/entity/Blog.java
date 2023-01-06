package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "blog")
public class Blog extends BaseEntity {
    private String title;
    private String description;
    private String cover;
    private String content;
    private Long views;
    private Long authorId;
    private Boolean status;
    private String ip;
    private String ipTerritory;
}
