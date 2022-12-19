package com.xx.pojo.dto;

import lombok.Data;

@Data
public class BlogDTO extends PageDTO {
    private String keywords;
    private Long userId;
    private Long authorId;

    // 0为发布的，1为顶过的，2为踩过的
    private Integer type;
    private Long categoryId;
}
