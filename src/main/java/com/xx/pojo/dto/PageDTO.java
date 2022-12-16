package com.xx.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 2;
    private String orderBy = "id";
}