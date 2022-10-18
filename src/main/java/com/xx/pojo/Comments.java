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
    private Integer id;
    private Integer blogId;
    private Integer parent;
    private String authorUsername;
    private String content;
    private Integer up;
    private Integer down;
    private Long postTime;

    @TableField(exist = false)
    private Boolean isUp;

    @TableField(exist = false)
    private Boolean isDown;

    @TableField(exist = false)
    private Map<String, Object> authorInfo;

    @TableField(exist = false)
    private Integer replyCount;

    @TableLogic
    private Boolean logicDelete;

    public void setPostTime(Timestamp postTime) throws ParseException {
        this.postTime = FormatTime.timestampToLong(postTime);
    }
}
