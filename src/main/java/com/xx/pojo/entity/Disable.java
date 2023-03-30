package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Disable extends BaseEntity {
    private Long userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    private String reason;
}
