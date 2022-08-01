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
    @PostMapping("register")
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
    public MyResponse getUserBlogList(String username, Integer flag, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        if (username == null || username.length() == 0) {
            myResponse.setMsg("请传入有效用户名！");
        } else {
            List<Blog> blogs = blogService.getUserBlogList(username, flag, startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
            myResponse.setData(blogs);
        }

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
    public MyResponse getCommentsList(int id) {
        MyResponse myResponse = new MyResponse();

        List<Comments> commentsList = commentsService.getCommentsList(id);
        myResponse.setData(commentsList);
        return myResponse;
    }
}
