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
        return MyResponse.unauthorized("用户未登录");
    }

    @ResponseBody
    @RequestMapping("/handleNotPermission")
    public MyResponse handleNotPermission() {
        return MyResponse.unauthorized();
    }
}
