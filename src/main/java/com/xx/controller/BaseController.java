package com.xx.controller;

import com.xx.service.CommonService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    @Autowired
    private CommonService commonService;

    public MyResponse success() {
        return MyResponse.success();
    }

    public MyResponse error() {
        return MyResponse.error();
    }

    public MyResponse fail() {
        return MyResponse.fail();
    }
}
