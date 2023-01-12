package com.xx.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class BlogDTO extends BaseDTO {
    private String keywords;
    private String authorNickname;
    private Long userId;

    // 0为发布的，1为顶过的，2为踩过的
    private Integer type;
    private Long categoryId;
    private String title;
    private String description;
    private String content;
    private MultipartFile coverImg;
    private Integer status;
}
