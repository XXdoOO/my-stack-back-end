package com.xx.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyResponse {
    private int code = Code.SUCCESS;
    private String msg = "请求成功！";
    private Object data;
}
