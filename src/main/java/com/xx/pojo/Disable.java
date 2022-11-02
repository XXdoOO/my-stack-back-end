package com.xx.pojo;

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

    public void setStarTime(Timestamp registerTime) throws ParseException {
        this.startTime = FormatTime.timestampToLong(registerTime);
    }

    public void setEndTime(Timestamp registerTime) throws ParseException {
        this.endTime = FormatTime.timestampToLong(registerTime);
    }
}
