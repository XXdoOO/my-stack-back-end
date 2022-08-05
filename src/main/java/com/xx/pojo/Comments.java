package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xx.util.FormatTime;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@TableName
public class Comments {
    private Integer id;
    private Integer blogId;
    private Integer parentComments;
    private String senderUsername;
    private String content;
    private Long time;

    public void setTime(Timestamp time) throws ParseException {
        this.time = FormatTime.timestampToLong(time);
    }

    @TableLogic
    private boolean logicDelete;
}
