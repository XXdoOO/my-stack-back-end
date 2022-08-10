package com.xx.controller;

import com.xx.mapper.BlogMapper;
import com.xx.mapper.CommentsMapper;
import com.xx.pojo.Blog;
import com.xx.pojo.Comments;
import com.xx.service.BlogService;
import com.xx.service.CommentsService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public MyResponse login(String username, String password) {
        MyResponse myResponse = new MyResponse();

        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            myResponse.setMsg("用户名或密码格式错误！");
            myResponse.setStatusCode(400);
        } else {
            Map<String, String> result = userService.login(username, password);

            if (result.size() != 0) {
                myResponse.setMsg("登录成功！");
                myResponse.setData(result);
            } else {
                myResponse.setMsg("用户名或密码错误！");
                myResponse.setStatusCode(400);
            }
        }
        return myResponse;
    }

    @ResponseBody
    @PostMapping("register")
    public MyResponse register(String username, String password) {
        MyResponse myResponse = new MyResponse();

        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            myResponse.setMsg("用户名或密码格式错误！");
            myResponse.setStatusCode(400);
        } else {
            int result = userService.register(username, password);

            if (result == 1) {
                myResponse.setMsg("注册成功！");
            } else {
                myResponse.setMsg("用户已存在！");
                myResponse.setStatusCode(400);
            }
        }

        return myResponse;
    }

    @ResponseBody
    @GetMapping("getBlogByKeywords")
    public MyResponse getBlogByKeywords(String keywords, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<Blog> blogList = blogService.getBlogListByKeywords(keywords == null ? "" : keywords, startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
        myResponse.setData(blogList);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getUserBlogList")
    public MyResponse getUserBlogList(String username, Integer status, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<Blog> blogs = blogService.getUserBlogList(username, status, startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
        myResponse.setData(blogs);

        return myResponse;
    }

    @ResponseBody
    @GetMapping("getBlogDetails")
    public MyResponse getBlogDetails(int id) {
        MyResponse myResponse = new MyResponse();

        Blog blogDetails = blogService.getBlogDetails(id);

        myResponse.setData(blogDetails);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getCommentsList")
    public MyResponse getCommentsList(int id, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<Comments> commentsList = commentsService.getCommentsList(id, startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
        myResponse.setData(commentsList);
        return myResponse;
    }
}
