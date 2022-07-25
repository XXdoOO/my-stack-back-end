package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@TableName
public class Blog {
    private int id;
    private String title;
    private String content;
    private int star;
    private int views;
    private String authorUsername;
    private Timestamp time;
    private int commentsId;
}
