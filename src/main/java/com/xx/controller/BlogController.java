package com.xx.controller;

import com.xx.mapper.BlogMapper;
import com.xx.pojo.Blog;
import com.xx.service.BlogService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;

    @ResponseBody
    @PostMapping("/pushBlog")
    public MyResponse pushBlog(@RequestBody Blog blog) {
        MyResponse response = new MyResponse();

        int result = blogService.pushBlog(blog);

        if (result == 1) {
            response.setMsg("发布失败！");
        } else {
            response.setMsg("发布成功！");
        }

        return response;
    }

    @ResponseBody
    @PostMapping("/getBlogList")
    public MyResponse getBlogList() {
        MyResponse response = new MyResponse();


        return response;
    }

}
