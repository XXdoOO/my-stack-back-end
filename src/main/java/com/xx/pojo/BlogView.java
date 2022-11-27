package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
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
    private Long id;
    private String title;
    private String description;
    private String cover;
    private Long up;
    private Long down;
    private Long star;
    private Long views;
    private Integer status;
    private String authorUsername;

    @TableField(exist = false)
    private String authorNickname;

    @TableField(exist = false)
    private Boolean isUp = false;

    @TableField(exist = false)
    private Boolean isDown = false;

    @TableField(exist = false)
    private Boolean isStar = false;

    private Long postTime;

    @TableField(exist = false)
    private Long startTime;
    @TableField(exist = false)
    private Long endTime;

    @TableLogic
    private Boolean logicDelete;

    public void setPostTime(Timestamp postTime) throws ParseException {
        this.postTime = FormatTime.timestampToLong(postTime);
    }
}
