package com.xx.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xx.pojo.entity.BaseEntity;
import lombok.Data;

@Data
public class CategoryVo {
    private Long id;

    private String name;
}
