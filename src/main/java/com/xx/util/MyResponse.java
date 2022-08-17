package com.xx.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyResponse {
    private int code = 200;
    private String msg = "操作成功！";
    private Object data;
}
