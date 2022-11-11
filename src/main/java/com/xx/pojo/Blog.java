package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xx.util.FormatTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@TableName

public class Blog {
    private Long id;
    private String title;
    private String description;
    private String cover;
    private String content;
    private Long up;
    private Long down;
    private Long star;
    private Long views;
    private String authorUsername;
    private Long postTime;
    private Boolean status;

    @TableField(exist = false)
    private User authorInfo;

    @TableField(exist = false)
    private List<String> categories;

    @TableField(exist = false)
    private Map<String, List<Comments>> commentsList;

    @TableField(exist = false)
    private Boolean isUp = false;

    @TableField(exist = false)
    private Boolean isDown = false;

    @TableField(exist = false)
    private Boolean isStar = false;

    @TableField(exist = false)
    private MultipartFile coverImg;

    @TableLogic
    private Boolean logicDelete;

    public void setPostTime(Timestamp postTime) throws ParseException {
        this.postTime = FormatTime.timestampToLong(postTime);
    }
}
