package com.xx.controller;

import com.xx.mapper.BlogMapper;
import com.xx.pojo.Blog;
import com.xx.service.BlogService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;

    @ResponseBody
    @PostMapping("/postBlog")
    public MyResponse postBlog(@RequestBody Blog blog) {
        MyResponse myResponse = new MyResponse();

        int result = blogService.postBlog(blog);

        if (result == 1) {
            myResponse.setMsg("发布成功！");
        } else {
            myResponse.setMsg("发布失败！");
        }

        return myResponse;
    }

    @ResponseBody
    @DeleteMapping("/deleteBlog")
    public MyResponse deleteBlog(int id) {
        MyResponse myResponse = new MyResponse();

        if (blogService.deleteBlog(id) == 1) {
            myResponse.setMsg("删除成功！");
        } else {
            myResponse.setMsg("删除失败！");
        }

        return myResponse;
    }

    @ResponseBody
    @PutMapping("/updateBlog")
    public MyResponse updateBlog(@RequestBody Blog blog) {
        MyResponse myResponse = new MyResponse();

        if (blogService.updateBlog(blog) == 1) {
            myResponse.setMsg("更新成功！");
        } else {
            myResponse.setMsg("更新失败！");
        }

        return myResponse;
    }

    @ResponseBody
    @GetMapping("/getBlogList")
    public MyResponse getBlogList(int startIndex, int pageSize) {
        MyResponse myResponse = new MyResponse();
        List<Blog> blogs = blogService.getBlogList(startIndex, pageSize);
        if (blogs.size() != 0) {
            myResponse.setMsg("获取成功！");
            myResponse.setData(blogs);
        } else {
            myResponse.setMsg("获取失败！");
        }
        return myResponse;
    }

}
