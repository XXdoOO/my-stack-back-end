package com.xx.controller;

import com.xx.pojo.Blog;
import com.xx.service.AuditService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AuditService auditService;

    @ResponseBody
    @PutMapping("/auditBlog")
    public MyResponse auditBlog(int id, boolean status) {
        MyResponse myResponse = new MyResponse();

        int result = auditService.auditBlog(id, status);

        if (result == 1) {
            myResponse.setMsg("审核成功！");
        } else {
            myResponse.setMsg("审核失败！");
        }

        return myResponse;
    }

    @ResponseBody
    @DeleteMapping("/deleteBlog")
    public MyResponse deleteBlog(int id) {
        MyResponse myResponse = new MyResponse();

        int result = auditService.deleteBlog(id);

        if (result == 1) {
            myResponse.setMsg("删除成功！");
        } else {
            myResponse.setMsg("删除失败！");
        }

        return myResponse;
    }
}