package com.xx.pojo.po;

import com.xx.pojo.entity.Category;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class BlogViewPo {
    private long id;
    private String title;
    private String description;
    private String cover;
    private long up;
    private long down;
    private long star;
    private long views;
    private Boolean isUp = false;
    private Boolean isDown = false;
    private Boolean isStar = false;
    private List<Category> categories;
    private Timestamp createTime;
}
