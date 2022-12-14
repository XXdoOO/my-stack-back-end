package com.xx.pojo.vo;

import com.xx.pojo.entity.Category;
import com.xx.pojo.entity.Comment;
import com.xx.pojo.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BlogVo {
    private Long id;
    private String title;
    private String description;
    private String cover;
    private String content;
    private long up;
    private long down;
    private long star;
    private long views;
    private Integer status;
    private UserVo authorInfo;
    private List<Category> categories;
    private boolean isUp;
    private boolean isDown;
    private boolean isStar;
    private Date createTime;
}
