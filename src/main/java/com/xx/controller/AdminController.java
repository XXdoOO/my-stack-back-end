package com.xx.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xx.config.FilterConfigurer;
import com.xx.pojo.dto.BlogDTO;
import com.xx.pojo.dto.CommentDTO;
import com.xx.pojo.dto.DictDTO;
import com.xx.pojo.dto.UserDTO;
import com.xx.service.BlogService;
import com.xx.service.CommentService;
import com.xx.service.DictService;
import com.xx.service.UserService;
import com.xx.util.Code;
import com.xx.util.MyResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DictService dictService;

    @ResponseBody
    @GetMapping("getBlogList")
    public MyResponse getBlogList(BlogDTO dto) {
        System.out.println(dto);
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());

        return MyResponse.success(new PageInfo<>(blogService.getBlogList2(dto)));
    }

    @ResponseBody
    @GetMapping("blog/{blogId}")
    public MyResponse getBlogDetails(@PathVariable long blogId) {
        return MyResponse.success(blogService.getBlogDetails2(blogId));
    }

    @ResponseBody
    @PutMapping("auditBlog")
    public MyResponse auditBlog(@RequestParam long blogId, @RequestParam int status) {
        return MyResponse.success(blogService.auditBlog(blogId, status));
    }

    @ResponseBody
    @GetMapping("getUserList")
    public MyResponse getUserList(UserDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        return MyResponse.success(new PageInfo<>(userService.getUserList(dto)));
    }

    @ResponseBody
    @PutMapping("disableUser")
    public MyResponse disableUser(@RequestBody UserDTO dto) {
        if (dto.getUserId() != null && dto.getEnabled() != null) {
            userService.disableUser(dto);
            return MyResponse.success();
        }
        return MyResponse.fail();
    }

    @ResponseBody
    @GetMapping("getCommentsList")
    public MyResponse getCommentsList(CommentDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        return MyResponse.success(new PageInfo<>(commentService.getCommentsList2(dto)));
    }
}

