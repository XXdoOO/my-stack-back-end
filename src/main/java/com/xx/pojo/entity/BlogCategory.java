package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "blog_category")
public class BlogCategory extends BaseEntity {
    @TableField(value = "blog_id")
    private Long blogId;

    @TableField(value = "category_name")
    private String categoryName;
}
