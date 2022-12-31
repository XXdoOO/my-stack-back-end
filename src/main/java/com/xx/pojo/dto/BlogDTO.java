package com.xx.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
