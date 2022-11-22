package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName
public class BlogCategory {
    private Long blogId;
    private String categoryName;

    @TableLogic
    private Boolean logicDelete;
}