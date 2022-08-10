package com.xx.controller;

import com.xx.pojo.Blog;
import com.xx.pojo.Comments;
import com.xx.service.BlogService;
import com.xx.service.CommentsService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentsService commentsService;

    @ResponseBody
    @GetMapping("/logout")
    public MyResponse logout() {
        MyResponse myResponse = new MyResponse();

        userService.logout();

        return myResponse;
    }

    @ResponseBody
    @PostMapping("/postBlog")
    public MyResponse postBlog(@RequestBody Blog blog) {
        MyResponse myResponse = new MyResponse();

        int result = blogService.postBlog(blog);

        if (result == 1) {
            myResponse.setMsg("发布成功！");
        } else {
            myResponse.setMsg("发布失败！");
            myResponse.setStatusCode(400);
        }

        return myResponse;
    }

    @ResponseBody
    @DeleteMapping("/deleteMyBlog")
    public MyResponse deleteMyBlog(int id) {
        MyResponse myResponse = new MyResponse();

        if (blogService.deleteMyBlog(id) == 1) {
            myResponse.setMsg("删除成功！");
        } else {
            myResponse.setMsg("删除失败！");
            myResponse.setStatusCode(400);
        }

        return myResponse;
    }

    @ResponseBody
    @PutMapping("/updateMyBlog")
    public MyResponse updateMyBlog(@RequestBody Blog blog) {
        MyResponse myResponse = new MyResponse();

        if (blogService.updateMyBlog(blog) == 1) {
            myResponse.setMsg("更新成功！");
        } else {
            myResponse.setMsg("更新失败！");
            myResponse.setStatusCode(400);
        }

        return myResponse;
    }

    @ResponseBody
    @PutMapping("/starBlog")
    public MyResponse starBlog(int id, boolean option) {
        MyResponse myResponse = new MyResponse();

        if (blogService.starBlog(id, option)) {
            myResponse.setMsg("收藏成功！");
        } else {
            myResponse.setMsg("收藏失败！");
            myResponse.setStatusCode(400);
        }

        return myResponse;
    }

    @ResponseBody
    @GetMapping("/getMyStarList")
    public MyResponse getMyStarList(Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<Blog> myStar = blogService.getMyStarList(startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
        myResponse.setData(myStar);
        return myResponse;
    }

    @ResponseBody
    @PostMapping("/postComments")
    public MyResponse postComments(@RequestBody Comments comments) {
        MyResponse myResponse = new MyResponse();

        if (commentsService.postComments(comments) == 1) {
            myResponse.setMsg("发布成功！");
        } else {
            myResponse.setMsg("发布失败！");
            myResponse.setStatusCode(400);
        }
        return myResponse;
    }

    @ResponseBody
    @DeleteMapping("/deleteMyComments")
    public MyResponse deleteMyComments(int id) {
        MyResponse myResponse = new MyResponse();

        if (commentsService.deleteMyComments(id) == 1) {
            myResponse.setMsg("删除成功！");
        } else {
            myResponse.setMsg("删除失败！");
            myResponse.setStatusCode(400);
        }
        return myResponse;
    }
}
