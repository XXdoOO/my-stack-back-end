package com.xx.pojo.vo;

import com.xx.pojo.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BlogVO {
    private Long id;
    private String title;
    private String description;
    private String cover;
    private String content;
    private Long up;
    private Long down;
    private Long star;
    private Long view;
    private String ip;
    private String ipTerritory;
    private Integer status;
    private UserVO authorInfo;
    private List<Category> categories;
    private Boolean isUp;
    private Boolean isDown;
    private Boolean isStar;
    private Date createTime;
}
