package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.xx.util.FormatTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName
public class User {
    @TableId
    @TableField(condition = SqlCondition.LIKE)
    private String username;

    @TableField(select = false)
    private String password;

    private String nickname;
    private String avatar;
    private Long registerTime;

    @TableField(exist = false)
    private Long startTime;
    @TableField(exist = false)
    private Long endTime;

    private Boolean status;
    private Boolean identity;

    @TableField(exist = false)
    private Disable disableInfo;

    @TableField(exist = false)
    private Long passCount;

    @TableField(exist = false)
    private Long noPassCount;

    @TableField(exist = false)
    private Long auditingCount;

    @TableField(exist = false)
    private Long upCount;

    @TableField(exist = false)
    private Long downCount;

    @TableField(exist = false)
    private Long starCount;

    @TableLogic
    private Boolean logicDelete;

    public void setRegisterTime(Timestamp registerTime) throws ParseException {
        this.registerTime = FormatTime.timestampToLong(registerTime);
    }
}
