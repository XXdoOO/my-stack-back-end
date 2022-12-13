package com.xx.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xx.pojo.entity.Category;
import com.xx.util.FormatTime;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BlogViewVo {
    private Long id;
    private String title;
    private String description;
    private String cover;
    private Long up;
    private Long down;
    private Long star;
    private Long views;
    private Integer status;
    private Long authorId;
    private String authorNickname;
    private Boolean isUp = false;
    private Boolean isDown = false;
    private Boolean isStar = false;

    private List<Category> categories;

    private Date createTime;
}
