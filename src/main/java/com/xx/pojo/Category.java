package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName
public class Category {
    private String name;
    private String description;
    private String img;
}
