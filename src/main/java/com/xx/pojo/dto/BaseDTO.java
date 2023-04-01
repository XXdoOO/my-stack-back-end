package com.xx.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
abstract class BaseDTO {

    private Long id;
    private Boolean enabled;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date[] updateTime = new Date[]{null, null};

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date[] createTime = new Date[]{null, null};

    private Long createBy;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String orderBy = "id";

    public void setPageNum(Integer pageNum) {
        if (pageNum == null || pageNum <= 0) {
            this.pageNum = 1;
        } else {
            this.pageNum = pageNum;
        }
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) {
            this.pageSize = 10;
        } else {
            this.pageSize = pageSize;
        }
    }
}