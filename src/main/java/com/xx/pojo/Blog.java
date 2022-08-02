package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@TableName
public class Blog {
    private Integer id;
    private String title;
    private String content;
    private Integer star;
    private Integer views;
    private String authorUsername;
    private Timestamp time;
    private Integer commentsId;
    private Boolean logicPost;

    @TableLogic
    private boolean logicDelete;
}
