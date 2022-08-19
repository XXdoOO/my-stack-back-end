package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@TableName
public class Category {
    @TableId
    private String name;
    private String description;
    private String cover;
    private List<Category> child;
}
