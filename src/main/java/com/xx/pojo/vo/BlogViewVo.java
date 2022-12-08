package com.xx.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xx.util.FormatTime;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;

@Data
@NoArgsConstructor
public class BlogViewVo {
    private Long id;
    private String title;
    private String description;
    private String cover;
    private long up;
    private long down;
    private long star;
    private long views;
    private Integer status;
    private long authorId;
    private String authorNickname;
    private boolean isUp;
    private boolean isDown;
    private boolean isStar;
    private Timestamp createTime;
}
