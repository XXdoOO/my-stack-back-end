package com.xx.controller;

import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    public MyResponse success() {
        return MyResponse.success();
    }

    public MyResponse success(Object data) {
        return MyResponse.success(data);
    }

    public MyResponse error() {
        return MyResponse.error();
    }

    public MyResponse error(String msg) {
        return MyResponse.error(msg);
    }

    public MyResponse fail() {
        return MyResponse.fail();
    }

    public MyResponse fail(String msg) {
        return MyResponse.fail(msg);
    }
}
