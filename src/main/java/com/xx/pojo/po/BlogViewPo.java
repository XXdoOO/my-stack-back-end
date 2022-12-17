package com.xx.pojo.po;

import com.xx.pojo.entity.Category;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class BlogViewPo {
    private Long id;
    private String title;
    private String description;
    private String cover;
    private Long up;
    private Long down;
    private Long star;
    private Long views;
    private Boolean isUp = false;
    private Boolean isDown = false;
    private Boolean isStar = false;
    private List<Category> categories;
    private Timestamp createTime;
}
