package com.xx.controller;

import com.xx.pojo.Blog;
import com.xx.pojo.BlogView;
import com.xx.pojo.Comments;
import com.xx.pojo.User;
import com.xx.service.BlogService;
import com.xx.service.CommentsService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

        int blogId = blogService.postBlog(blog);
        myResponse.setData(blogId);
        myResponse.setMsg("发布成功！");

        return myResponse;
    }

    @ResponseBody
    @PostMapping("/uploadCover")
    public MyResponse upload(@RequestParam("file") MultipartFile file) {
        MyResponse myResponse = new MyResponse();

        String result = blogService.saveFile(file);
        myResponse.setData(result);
        return myResponse;
    }

    @ResponseBody
    @DeleteMapping("/deleteMyBlog")
    public MyResponse deleteMyBlog(int id) {
        MyResponse myResponse = new MyResponse();

        if (blogService.deleteMyBlog(id)) {
            myResponse.setMsg("删除成功！");
        } else {
            myResponse.setMsg("删除失败！");
            myResponse.setCode(400);
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
            myResponse.setCode(400);
        }

        return myResponse;
    }

    @ResponseBody
    @PutMapping("/upBlog")
    public MyResponse upBlog(int id) {
        MyResponse myResponse = new MyResponse();

        if (blogService.upBlog(id)) {
            myResponse.setMsg("顶成功！");
        } else {
            myResponse.setMsg("顶失败！");
            myResponse.setCode(400);
        }

        return myResponse;
    }

    @ResponseBody
    @PutMapping("/downBlog")
    public MyResponse downBlog(int id) {
        MyResponse myResponse = new MyResponse();

        if (blogService.downBlog(id)) {
            myResponse.setMsg("踩成功！");
        } else {
            myResponse.setMsg("踩失败！");
            myResponse.setCode(400);
        }

        return myResponse;
    }

    @ResponseBody
    @PutMapping("/starBlog")
    public MyResponse starBlog(int id) {
        MyResponse myResponse = new MyResponse();

        if (blogService.starBlog(id)) {
            myResponse.setMsg("收藏成功！");
        } else {
            myResponse.setMsg("收藏失败！");
            myResponse.setCode(400);
        }

        return myResponse;
    }

    @ResponseBody
    @GetMapping("/getMyInfo")
    public MyResponse getMyInfo() {
        MyResponse myResponse = new MyResponse();

        User user = userService.getMyInfo();
        myResponse.setData(user);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("/getMyPostList")
    public MyResponse getMyPostList(Integer status, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<BlogView> myPostList = blogService.getMyPostList(status, startIndex == null ? 0 : startIndex,
                pageSize == null ? 10
                        : pageSize);
        myResponse.setData(myPostList);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("/getMyStarList")
    public MyResponse getMyStarList(Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<BlogView> myStar = blogService.getMyStarList(startIndex == null ? 0 : startIndex, pageSize == null ? 10
                : pageSize);
        myResponse.setData(myStar);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("/getMyUpList")
    public MyResponse getMyUpList(Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<BlogView> myStar = blogService.getMyUpList(startIndex == null ? 0 : startIndex, pageSize == null ? 10 :
                pageSize);
        myResponse.setData(myStar);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("/getMyDownList")
    public MyResponse getMyDownList(Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<BlogView> myStar = blogService.getMyDownList(startIndex == null ? 0 : startIndex, pageSize == null ? 10
                : pageSize);
        myResponse.setData(myStar);
        return myResponse;
    }

    @ResponseBody
    @PostMapping("/postComments")
    public MyResponse postComments(@RequestBody Comments comments) {
        MyResponse myResponse = new MyResponse();

        int commentsId = commentsService.postComments(comments);
        myResponse.setData(commentsId);
        myResponse.setMsg("发布成功！");

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
            myResponse.setCode(400);
        }
        return myResponse;
    }

    @ResponseBody
    @PutMapping("/upComments")
    public MyResponse upComments(int id) {
        MyResponse myResponse = new MyResponse();

        if (commentsService.upComments(id)) {
            myResponse.setMsg("顶成功！");
        } else {
            myResponse.setMsg("顶失败！");
            myResponse.setCode(400);
        }

        return myResponse;
    }

    @ResponseBody
    @PutMapping("/downComments")
    public MyResponse downComments(int id) {
        MyResponse myResponse = new MyResponse();

        if (commentsService.downComments(id)) {
            myResponse.setMsg("顶成功！");
        } else {
            myResponse.setMsg("顶失败！");
            myResponse.setCode(400);
        }

        return myResponse;
    }
}
