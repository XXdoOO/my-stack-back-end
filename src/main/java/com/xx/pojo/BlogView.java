package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xx.util.FormatTime;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;

@Data
@NoArgsConstructor
@TableName("blog")
public class BlogView {
    private Integer id;
    private String title;
    private String description;
    private Integer up;
    private Integer down;
    private Integer star;
    private Integer views;
    private String authorUsername;
    private Long postTime;

    public void setPostTime(Timestamp postTime) throws ParseException {
        this.postTime = FormatTime.timestampToLong(postTime);
    }
}
