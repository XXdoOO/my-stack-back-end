package com.xx.pojo.vo;

import com.xx.pojo.entity.Comment;
import com.xx.pojo.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
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
    private User authorInfo;
    private List<String> categories;
    private List<Comment> commentsList;
    private boolean isUp;
    private boolean isDown;
    private boolean isStar;
    private Timestamp createTime;
}
