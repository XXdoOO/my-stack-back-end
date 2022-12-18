package com.xx.util;

import lombok.Data;

@Data
public class Code {
    public static final String REGISTER_CODE = "REGISTER_CODE";
    private String email;
    private String code;
    private Long createTime;

    public Code(String code) {
        this.code = code;
        this.createTime = System.currentTimeMillis();
    }

    public Code(String email, String code) {
        this.email = email;
        this.code = code;
        this.createTime = System.currentTimeMillis();
    }
}
