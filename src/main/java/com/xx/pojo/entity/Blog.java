package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class Blog extends BaseEntity {
    private String title;
    private String description;
    private String cover;
    private String content;
    private Long view;
    private Integer status;
    private String ip;
    private String ipTerritory;
}
