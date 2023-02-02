package com.xx.pojo.vo;

import com.xx.pojo.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BlogViewVO {
    private Long id;
    private String title;
    private String description;
    private String cover;
    private Long up;
    private Long down;
    private Long star;
    private Long view;
    private Long comment;
    private Integer status;
    private Boolean enabled;
    private Long createBy;
    private String authorNickname;
    private Boolean isUp = false;
    private Boolean isDown = false;
    private Boolean isStar = false;

    private List<Category> categories;

    private Date createTime;
}
