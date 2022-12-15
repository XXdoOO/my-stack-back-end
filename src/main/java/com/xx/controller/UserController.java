package com.xx.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xx.pojo.dto.CommentDTO;
import com.xx.service.BlogService;
import com.xx.service.CommentService;
import com.xx.service.UserService;
import com.xx.util.Code;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
    private CommentService commentService;

    @ResponseBody
    @GetMapping("/logout")
    public MyResponse logout() {
        MyResponse myResponse = new MyResponse();

        userService.logout();

        return myResponse;
    }

    @PostMapping("postComments")
    private MyResponse postComments(@RequestBody @Valid CommentDTO dto) {
        if (StringUtils.isBlank(dto.getContent())) {
            return MyResponse.error("评论内容不能为空");
        }

        return MyResponse.success(commentService.postComments(dto));
    }

    @DeleteMapping("deleteComment")
    private MyResponse deleteComment(@RequestParam long id) {
        return MyResponse.success(commentService.deleteComment(id));
    }
}
