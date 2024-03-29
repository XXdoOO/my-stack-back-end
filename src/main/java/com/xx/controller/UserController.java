package com.xx.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xx.pojo.dto.BlogDTO;
import com.xx.pojo.dto.CommentDTO;
import com.xx.pojo.dto.UserDTO;
import com.xx.service.BlogService;
import com.xx.service.CommentService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import com.xx.util.SaveFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/logout")
    public MyResponse logout() {
        return userService.logout() ? success() : fail();
    }

    @GetMapping("/userInfo")
    public MyResponse getUserInfo() {
        return success(userService.getUserInfo());
    }

    @PostMapping("postComments")
    private MyResponse postComments(@Validated @RequestBody CommentDTO dto) {
        return success(commentService.postComments(dto));
    }

    @DeleteMapping("deleteComment/{commentId}")
    private MyResponse deleteComment(@PathVariable long commentId) {
        return success(commentService.deleteComment(commentId));
    }

    @PutMapping("handleBlog")
    private MyResponse handleBlog(long blogId, int type) {
        return success(blogService.handleBlog(blogId, type));
    }

    @PutMapping("handleComment")
    private MyResponse handleComment(long commentId, int type) {
        return success(commentService.handleComment(commentId, type));
    }

    @PostMapping("postBlog")
    private MyResponse postBlog(BlogDTO dto) {
        Long l = blogService.postBlog(dto);
        return l != null ? success(l) : fail();
    }

    @PutMapping("updateBlog")
    private MyResponse updateBlog(BlogDTO dto) {
        if (dto.getId() != null) {
            blogService.updateBlog(dto);
            return MyResponse.success();
        }
        return fail();
    }

    @DeleteMapping("deleteBlog/{blogId}")
    private MyResponse deleteBlog(@PathVariable Long blogId) {
        return success(blogService.deleteBlog(blogId));
    }

    @GetMapping("getBlogList")
    public MyResponse getBlogList(BlogDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());

        return success(new PageInfo<>(blogService.getBlogList(dto)));
    }

    @PutMapping("updateInfo")
    public MyResponse updateInfo(UserDTO dto) {
        return success(userService.updateInfo(dto));
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

    @GetMapping("getToken")
    public MyResponse getToken() {
        return success(userService.getToken());
    }
}
