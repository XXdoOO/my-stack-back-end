package com.xx.controller;

import com.xx.pojo.Blog;
import com.xx.service.AuditService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AuditController {
    @Autowired
    private AuditService auditService;

    @ResponseBody
    @GetMapping("/getAuditBlog")
    public MyResponse getAuditBlog() {
        MyResponse myResponse = new MyResponse();

        List<Blog> blogList = auditService.getAuditBlog();

        myResponse.setData(blogList);

        return myResponse;
    }

    @ResponseBody
    @GetMapping("/auditBlog")
    public MyResponse auditBlog(int id, boolean isPass) {
        MyResponse myResponse = new MyResponse();

        int result = auditService.auditBlog(id, isPass);

        if (result == 1) {
            myResponse.setMsg("审核成功！");
        } else {
            myResponse.setMsg("审核失败！");
        }

        return myResponse;
    }

    @ResponseBody
    @GetMapping("/deleteBlog")
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
