package com.xx.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyResponse {
    private int code;
    private String msg;
    private Object data;

    public static MyResponse success() {
        return success("请求成功");
    }

    public static MyResponse success(String msg) {
        return success(msg, null);
    }

    public static MyResponse success(Object data) {
        return success("请求成功", data);
    }

    public static MyResponse success(String msg, Object data) {
        return new MyResponse(StatusCode.SUCCESS, msg, data);
    }

    public static MyResponse fail() {
        return fail("请求失败");
    }

    public static MyResponse fail(String msg) {
        return fail(msg, null);
    }

    public static MyResponse fail(String msg, Object data) {
        return new MyResponse(StatusCode.FAIL, msg, data);
    }

    public static MyResponse error() {
        return error("请求错误");
    }

    public static MyResponse error(String msg) {
        return error(msg, null);
    }

    public static MyResponse error(String msg, Object data) {
        return new MyResponse(StatusCode.ERROR, msg, data);
    }

    public static MyResponse unauthorized() {
        return unauthorized("权限不足");
    }

    public static MyResponse unauthorized(String msg) {
        return unauthorized(msg, null);
    }

    public static MyResponse unauthorized(String msg, Object data) {
        return new MyResponse(StatusCode.UNAUTHORIZED, msg, data);
    }
}
