package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "category")
public class Category extends BaseEntity {
    @TableId
    private Long id;

    @TableField(value = "name")
    private String name;
}
