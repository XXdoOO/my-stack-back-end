package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class Record extends BaseEntity {
    private Long blogId;
    private Long commentId;
    private Integer type;
}
