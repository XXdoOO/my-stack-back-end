package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private Integer id;
    private String title;
    private String description;
    private String cover;
    private String content;
    private Integer up;
    private Integer down;
    private Integer star;
    private Integer views;
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
