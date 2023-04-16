package com.xx.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xx.pojo.dto.BlogDTO;
import com.xx.pojo.dto.CommentDTO;
import com.xx.pojo.dto.UserDTO;
import com.xx.pojo.entity.Blog;
import com.xx.service.BlogService;
import com.xx.service.CommentService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import com.xx.util.SaveFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/logout")
    public MyResponse logout() {
        userService.logout();

        return MyResponse.success();
    }

    @GetMapping("/userInfo")
    public MyResponse getUserInfo() {
        return MyResponse.success(userService.getUserInfo());
    }

    @PostMapping("postComments")
    private MyResponse postComments(@Validated @RequestBody CommentDTO dto) {
        return MyResponse.success(commentService.postComments(dto));
    }

    @DeleteMapping("deleteComment/{commentId}")
    private MyResponse deleteComment(@PathVariable long commentId) {
        return MyResponse.success(commentService.deleteComment(commentId));
    }

    @PutMapping("handleBlog")
    private MyResponse handleBlog(long blogId, int type) {
        return MyResponse.success(blogService.handleBlog(blogId, type));
    }

    @PutMapping("handleComment")
    private MyResponse handleComment(long commentId, int type) {
        return MyResponse.success(commentService.handleComment(commentId, type));
    }

    @PostMapping("postBlog")
    private MyResponse postBlog(BlogDTO dto) {
        return blogService.postBlog(dto) ? success() : fail();
    }

    @PutMapping("updateBlog")
    private MyResponse updateBlog(BlogDTO dto) {
        if (dto.getId() != null) {
            blogService.updateBlog(dto);
            return MyResponse.success();
        }
        return MyResponse.fail();
    }

    @DeleteMapping("deleteBlog/{blogId}")
    private MyResponse deleteBlog(@PathVariable Long blogId) {
        return MyResponse.success(blogService.deleteBlog(blogId));
    }

    @GetMapping("getBlogList")
    public MyResponse getBlogList(BlogDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());

        return MyResponse.success(new PageInfo<>(blogService.getBlogList(dto)));
    }

    @PutMapping("updateInfo")
    public MyResponse updateInfo(UserDTO dto) {
        return MyResponse.success(userService.updateInfo(dto));
    }

    @PostMapping("uploadImage")
    public MyResponse uploadImage(MultipartFile image, Long blogId) {
        String s = SaveFile.uploadBlogImage(image, blogId);
        if (s != null) {
            return success(s);
        }
        return fail();
    }

    @DeleteMapping("cancelAccount")
    public MyResponse cancelAccount() {
        return userService.cancelAccount() ? success() : fail();
    }
}
