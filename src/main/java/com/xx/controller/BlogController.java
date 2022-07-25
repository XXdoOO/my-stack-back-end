package com.xx.controller;

import com.xx.mapper.BlogMapper;
import com.xx.pojo.Blog;
import com.xx.service.BlogService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;

    @ResponseBody
    @PostMapping("/pushBlog")
    public MyResponse pushBlog(String title, String content) {
        MyResponse response = new MyResponse();

        int result = blogService.pushBlog(title, content);

        if (result == 1) {
            response.setMsg("发布失败！");
        } else {
            response.setMsg("发布成功！");
        }

        return response;
    }

    @ResponseBody
    @GetMapping("/getBlogList")
    public MyResponse getBlogList(int startIndex, int pageSize) {
        MyResponse response = new MyResponse();
        List<Blog> blogs = blogService.getBlogList(startIndex, pageSize);
        if (blogs.size() != 0) {
            response.setMsg("获取成功！");
            response.setData(blogs);
        } else {
            response.setMsg("获取失败！");
        }
        return response;
    }

}
