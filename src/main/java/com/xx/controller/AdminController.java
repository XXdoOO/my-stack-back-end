package com.xx.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xx.pojo.dto.BlogDTO;
import com.xx.pojo.dto.CommentDTO;
import com.xx.pojo.dto.UserDTO;
import com.xx.service.BlogService;
import com.xx.service.CommentService;
import com.xx.service.DictService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DictService dictService;

    @GetMapping("getBlogList")
    public MyResponse getBlogList(BlogDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());

        return success(new PageInfo<>(blogService.getBlogList2(dto)));
    }

    @GetMapping("blog/{blogId}")
    public MyResponse getBlogDetails(@PathVariable long blogId) {
        return success(blogService.getBlogDetails2(blogId));
    }

    @PutMapping("auditBlog")
    public MyResponse auditBlog(@RequestParam long blogId, @RequestParam int status) {
        return success(blogService.auditBlog(blogId, status));
    }

    @PutMapping("enableBlog/{blogId}")
    public MyResponse enableBlog(@PathVariable long blogId) {
        return MyResponse.success(blogService.enableBlog(blogId));
    }

    @GetMapping("getUserList")
    public MyResponse getUserList(UserDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        return MyResponse.success(new PageInfo<>(userService.getUserList(dto)));
    }

    @PutMapping("disableUser")
    public MyResponse disableUser(@RequestBody UserDTO dto) {
        if (dto.getUserId() != null && dto.getEnabled() != null) {
            userService.disableUser(dto);
            return MyResponse.success();
        }
        return MyResponse.fail();
    }

    @GetMapping("getCommentsList")
    public MyResponse getCommentsList(CommentDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        return MyResponse.success(new PageInfo<>(commentService.getCommentsList2(dto)));
    }
}

