package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
    private Long registerTime;
    private Long disableTime;
    private Boolean identity;

    @TableLogic
    private Boolean logicDelete;

    public void setRegisterTime(Timestamp registerTime) throws ParseException {
        this.registerTime = FormatTime.timestampToLong(registerTime);
    }

    public void setDisableTime(Timestamp disableTime) throws ParseException {
        this.disableTime = FormatTime.timestampToLong(disableTime);
    }

}
