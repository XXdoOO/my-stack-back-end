package com.xx.controller;

import com.xx.util.MyResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class OtherController {
    @ResponseBody
    @RequestMapping("/handleNotLogin")
    public MyResponse handleNotLogin() {
        return new MyResponse(200, "请登录后操作！", null);
    }

    @ResponseBody
    @RequestMapping("/handleNotPermission")
    public MyResponse handleNotPermission() {
        return new MyResponse(200, "您无权访问该接口！", null);
    }
}
