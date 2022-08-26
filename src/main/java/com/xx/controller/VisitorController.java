package com.xx.controller;

import com.xx.mapper.BlogMapper;
import com.xx.mapper.CommentsMapper;
import com.xx.pojo.Blog;
import com.xx.pojo.BlogView;
import com.xx.pojo.Category;
import com.xx.pojo.Comments;
import com.xx.service.BlogService;
import com.xx.service.CategoryService;
import com.xx.service.CommentsService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
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

    @Autowired
    private CategoryService categoryService;

    @ResponseBody
    @PostMapping("login")
    public MyResponse login(String username, String password) {
        MyResponse myResponse = new MyResponse();

        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            myResponse.setMsg("用户名或密码格式错误！");
            myResponse.setCode(400);
        } else {
            Map<String, String> result = userService.login(username, password);

            if (result.size() != 0) {
                myResponse.setMsg("登录成功！");
                myResponse.setData(result);
            } else {
                myResponse.setMsg("用户名或密码错误！");
                myResponse.setCode(400);
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
            myResponse.setCode(400);
        } else {
            int result = userService.register(username, password);

            if (result == 1) {
                myResponse.setMsg("注册成功！");
            } else {
                myResponse.setMsg("用户已存在！");
                myResponse.setCode(400);
            }
        }

        return myResponse;
    }

    @ResponseBody
    @GetMapping("getBlogByKeywords")
    public MyResponse getBlogByKeywords(String keywords, Boolean orderBy, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<BlogView> blogList = blogService.getBlogListByKeywords(keywords == null ? "" : keywords, (orderBy == null || !orderBy) ? "up" : "post_time", startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
        myResponse.setData(blogList);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getUserBlogList")
    public MyResponse getUserBlogList(String username, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        if (username == null || username.length() == 0) {
            myResponse.setCode(400);
            myResponse.setMsg("请传入用户名！");
        } else {
            List<BlogView> blogs = blogService.getUserBlogList(username, startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
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
    public MyResponse getCommentsList(int id, Boolean orderBy, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<Comments> commentsList = commentsService.getCommentsList(id, (orderBy == null || !orderBy) ? "up" : "time", startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
        myResponse.setData(commentsList);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getCategories")
    public MyResponse getCategories() {
        MyResponse myResponse = new MyResponse();

        List<Category> categories = categoryService.getCategories();
        myResponse.setData(categories);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getCategory")
    public MyResponse getCategory(String name) {
        MyResponse myResponse = new MyResponse();

        Category category = categoryService.getCategory(name);
        myResponse.setData(category);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getBlogByCategories")
    public MyResponse getBlogListByCategories(@RequestParam("categories") List<String> categories, Boolean orderBy, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        for (String category : categories) {
            System.out.println(category);
        }

        List<BlogView> blogViewList = blogService.getBlogListByCategories(categories, (orderBy == null || !orderBy) ? "up" : "time", startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
        myResponse.setData(blogViewList);
        return myResponse;
    }
}
