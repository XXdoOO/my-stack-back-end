package com.xx.util;

public class StatusCode {
    // 操作成功
    public final static int SUCCESS = 200;

    // 执行操作，操作失败
    public final static int FAIL = 201;

    // 未执行操作，操作错误
    public final static int ERROR = 400;

    // 权限不足
    public final static int UNAUTHORIZED = 403;

    // 更新token
    public final static int UPDATE_TOKEN = 405;
}
