package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class DictType extends BaseEntity{
    @TableField(condition = SqlCondition.LIKE)
    private String name;
}
