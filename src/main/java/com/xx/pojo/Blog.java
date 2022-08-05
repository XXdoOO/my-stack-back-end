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
@TableName
public class Blog {
    private Integer id;
    private String title;
    private String content;
    private Integer star;
    private Integer views;
    private String authorUsername;
    private Long time;
    private Integer commentsId;
    private Boolean status;

    public void setTime(Timestamp time) throws ParseException {
        this.time = FormatTime.timestampToLong(time);
    }

    @TableLogic
    private boolean logicDelete;
}
