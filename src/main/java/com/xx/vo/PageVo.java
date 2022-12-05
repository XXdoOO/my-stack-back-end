package com.xx.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo {
    private long pageNum = 1;
    private long pageSize = 10;
}
