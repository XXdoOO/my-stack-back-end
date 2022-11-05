package com.xx.controller;

import com.xx.pojo.*;
import com.xx.service.BlogService;
import com.xx.service.CommentsService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import com.xx.util.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class VisitorController {
    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentsService commentsService;

    @ResponseBody
    @PostMapping("login")
    public MyResponse login(@RequestParam String username, @RequestParam String password) {
        MyResponse myResponse = new MyResponse();

        User userInfo = userService.getUserInfo(username);

        if (userInfo == null) {
            myResponse.setMsg("用户不存在！");
            myResponse.setCode(Code.USER_NOT_EXIST);
        } else {
            User user = userService.login(username, password);

            if (user == null) {
                myResponse.setMsg("用户名或密码错误！");
                myResponse.setCode(Code.USER_ERROR);
            } else if (user.getStatus()) {
                myResponse.setData(user.getDisableInfo());
                myResponse.setMsg("用户已被封禁！");
                myResponse.setCode(Code.USER_DISABLE);
            } else {
                myResponse.setData(user);
                myResponse.setMsg("登录成功");
            }
        }
        return myResponse;
    }

    @ResponseBody
    @PostMapping("register")
    public MyResponse register(@RequestParam String username, @RequestParam String password) {
        MyResponse myResponse = new MyResponse();

        if (userService.register(username, password)) {
            myResponse.setMsg("注册成功！");
        } else {
            myResponse.setMsg("用户已存在！");
            myResponse.setCode(Code.USER_ALREADY_EXIST);
        }

        return myResponse;
    }

    @ResponseBody
    @GetMapping("getBlogByKeywords")
    public MyResponse getBlogByKeywords(String keywords, Boolean orderBy, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        Map<String, Object> map = blogService.getBlogListByKeywords(keywords == null ? "" : keywords,
                (orderBy == null || !orderBy) ? "up" : "post_time", startIndex == null ? 0 : startIndex,
                pageSize == null ? 10 : pageSize);
        myResponse.setData(map);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getUserInfo")
    public MyResponse getUserInfo(@RequestParam String username) {
        MyResponse myResponse = new MyResponse();

        User user = userService.getUserInfo(username);
        myResponse.setData(user);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getUserPostBlogList")
    public MyResponse getUserBlogList(@RequestParam String username, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        Map<String, Object> map = blogService.getUserPostBlogList(username, startIndex == null ? 0 :
                        startIndex,
                pageSize == null ? 10 : pageSize);
        myResponse.setData(map);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getUserUpBlogList")
    public MyResponse getUserUpBlogList(@RequestParam String username, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        Map<String, Object> map = blogService.getUserUpBlogList(username, startIndex == null ? 0 :
                        startIndex,
                pageSize == null ? 10 : pageSize);
        myResponse.setData(map);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getUserDownBlogList")
    public MyResponse getUserDownBlogList(@RequestParam String username, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        Map<String, Object> map = blogService.getUserDownBlogList(username, startIndex == null ? 0 :
                        startIndex,
                pageSize == null ? 10 : pageSize);
        myResponse.setData(map);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getBlogDetails")
    public MyResponse getBlogDetails(@RequestParam int id) {
        MyResponse myResponse = new MyResponse();

        Blog blogDetails = blogService.getBlogDetails(id);

        myResponse.setData(blogDetails);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getCommentsList")
    public MyResponse getCommentsList(@RequestParam int id, Boolean orderBy, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<Comments> commentsList = commentsService.getCommentsList(id, (orderBy == null || !orderBy) ? "up" :
                "time", startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
        myResponse.setData(commentsList);
        return myResponse;
    }
}
