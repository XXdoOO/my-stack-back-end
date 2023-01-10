package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class DictData extends BaseEntity {
    @TableField(condition = SqlCondition.LIKE)
    private String dictName;
    private String label;
    private String value;
}
