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
public class Disable {
    private Long startTime;
    private Long endTime;
    private String username;
    private String reason;

    @TableLogic
    private Boolean logicDelete;

    public void setStartTime(Timestamp startTime) throws ParseException {
        this.startTime = FormatTime.timestampToLong(startTime);
    }

    public void setEndTime(Timestamp endTime) throws ParseException {
        this.endTime = FormatTime.timestampToLong(endTime);
    }
}
