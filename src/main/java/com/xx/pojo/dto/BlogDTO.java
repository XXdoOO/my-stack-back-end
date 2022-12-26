package com.xx.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class BlogDTO extends PageDTO {
    private String keywords;
    private String authorNickname;
    private Long userId;
    private Long authorId;

    // 0为发布的，1为顶过的，2为踩过的
    private Integer type;
    private Long categoryId;

    private Long id;
    private String title;
    private String description;
    private String content;
    private MultipartFile coverImg;

    private Integer status;
    private Date startTime;
    private Date endTime;
}
