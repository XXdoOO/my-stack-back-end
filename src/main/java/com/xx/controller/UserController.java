package com.xx.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xx.pojo.dto.BlogDTO;
import com.xx.pojo.dto.CommentDTO;
import com.xx.pojo.dto.UserDTO;
import com.xx.service.BlogService;
import com.xx.service.CommentService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @GetMapping("/logout")
    public MyResponse logout() {
        userService.logout();

        return MyResponse.success();
    }

    @ResponseBody
    @PostMapping("postComments")
    private MyResponse postComments(@RequestBody @Valid CommentDTO dto) {
        if (StringUtils.isBlank(dto.getContent())) {
            return MyResponse.error("评论内容不能为空");
        }

        return MyResponse.success(commentService.postComments(dto));
    }

    @ResponseBody
    @DeleteMapping("deleteComment/{commentId}")
    private MyResponse deleteComment(@PathVariable long commentId) {
        return MyResponse.success(commentService.deleteComment(commentId));
    }

    @ResponseBody
    @PutMapping("handleBlog")
    private MyResponse handleBlog(long blogId, int type) {
        return MyResponse.success(blogService.handleBlog(blogId, type));
    }

    @ResponseBody
    @PutMapping("handleComment")
    private MyResponse handleComment(long commentId, int type) {
        return MyResponse.success(commentService.handleComment(commentId, type));
    }

    @ResponseBody
    @PostMapping("postBlog")
    private MyResponse postBlog(BlogDTO dto) {
        blogService.postBlog(dto);
        return MyResponse.success();
    }

    @ResponseBody
    @PutMapping("updateBlog")
    private MyResponse updateBlog(BlogDTO dto) {
        if (dto.getId() != null) {
            blogService.updateBlog(dto);
            return MyResponse.success();
        }
        return MyResponse.fail();
    }

    @ResponseBody
    @DeleteMapping("deleteBlog/{blogId}")
    private MyResponse deleteBlog(@PathVariable Long blogId) {
        return MyResponse.success(blogService.deleteBlog(blogId));
    }

    @ResponseBody
    @GetMapping("getBlogList")
    public MyResponse getBlogList(BlogDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());

        return MyResponse.success(new PageInfo<>(blogService.getBlogList(dto)));
    }

    @ResponseBody
    @PutMapping("updateInfo")
    public MyResponse updateInfo(UserDTO dto) {
        return MyResponse.success(userService.updateInfo(dto));
    }
}
