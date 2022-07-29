package com.xx.util;

public class MyResponse {
    private int statusCode = 200;
    private String msg = "操作成功！";
    private Object data;

    public MyResponse() {
    }

    public MyResponse(int statusCode, String msg, Object data) {
        this.statusCode = statusCode;
        this.msg = msg;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MyResponse{" +
                "statusCode=" + statusCode +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
