package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xx.util.FormatTime;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Map;

@Data
@NoArgsConstructor
@TableName
public class Comments {
    private Long id;
    private Long blogId;
    private Long parent;
    private String authorUsername;
    private String content;
    private Long up = 0L;
    private Long down = 0L;
    private Long postTime;

    @TableField(exist = false)
    private Boolean isUp = false;

    @TableField(exist = false)
    private Boolean isDown = false;

    @TableField(exist = false)
    private Map<String, Object> authorInfo;

    @TableField(exist = false)
    private Long replyCount;

    @TableLogic
    private Boolean logicDelete;

    public void setPostTime(Timestamp postTime) throws ParseException {
        this.postTime = FormatTime.timestampToLong(postTime);
    }
}
