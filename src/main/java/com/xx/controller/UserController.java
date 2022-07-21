package com.xx.controller;

import com.xx.pojo.User;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping("/login")
    public MyResponse login(String username, String password) {
        MyResponse response = new MyResponse();

        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            response.setMsg("用户名或密码格式错误！");
        } else {
            Map<String, String> result = userService.login(username, password);

            if (result.size() != 0) {
                response.setMsg("登录成功！");
                response.setData(result);
            } else {
                response.setMsg("用户名或密码错误！");
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/register")
    public MyResponse register(String username, String password) {
        MyResponse response = new MyResponse();

        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            response.setMsg("用户名或密码格式错误！");
        } else {
            int result = userService.register(username, password);

            if (result == 1) {
                response.setMsg("注册成功！");
            } else {
                response.setMsg("注册失败！");
            }
        }

        return response;
    }

}
