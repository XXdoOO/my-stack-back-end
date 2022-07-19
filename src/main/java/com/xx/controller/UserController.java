package com.xx.controller;

import com.xx.mapper.UserMapper;
import com.xx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping("/login")
    public Map<String, Object> login(String username, String password) {
        return userService.login(username, password);
    }

}
