package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xx.util.FormatTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName
public class User {
    @TableId
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private Long time;
    private Long disableTime;
    private Boolean identity;

    public void setTime(Timestamp time) throws ParseException {
        this.time = FormatTime.timestampToLong(time);
    }

    public void setDisableTime(Timestamp time) throws ParseException {
        this.disableTime = FormatTime.timestampToLong(time);
    }

}
