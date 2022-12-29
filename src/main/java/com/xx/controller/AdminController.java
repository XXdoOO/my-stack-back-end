package com.xx.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xx.config.FilterConfigurer;
import com.xx.pojo.dto.BlogDTO;
import com.xx.pojo.dto.UserDTO;
import com.xx.service.BlogService;
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

    @ResponseBody
    @GetMapping("getBlogList")
    public MyResponse getBlogList(BlogDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());

        return MyResponse.success(new PageInfo<>(blogService.getBlogList2(dto)));
    }

    @ResponseBody
    @GetMapping("auditBlog")
    public MyResponse auditBlog(@RequestParam long blogId, @RequestParam boolean isPass) {
        return MyResponse.success(blogService.auditBlog(blogId, isPass));
    }

    @ResponseBody
    @GetMapping("getUserList")
    public MyResponse getUserList(UserDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        return MyResponse.success(new PageInfo<>(userService.getUserList(dto)));
    }
}

