package com.xx.controller;

import com.xx.pojo.Blog;
import com.xx.pojo.BlogView;
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
            myResponse.setCode(400);
        }

        return myResponse;
    }

    @ResponseBody
    @GetMapping("/getPostBlogList")
    public MyResponse getPostBlogList(Integer status, int startIndex, int pageSize) {
        MyResponse myResponse = new MyResponse();

        List<BlogView> blogList = auditService.getPostBlogList(status, startIndex, pageSize);

        myResponse.setData(blogList);
        myResponse.setMsg("获取成功！");

        return myResponse;
    }

    @ResponseBody
    @GetMapping("/getAuditBlogDetails")
    public MyResponse getAuditBlogDetails(int id) {
        MyResponse myResponse = new MyResponse();

        Blog blogDetails = auditService.getAuditBlogDetails(id);
        myResponse.setData(blogDetails);

        return myResponse;
    }

    @ResponseBody
    @GetMapping("/getNearAuditBlogDetails")
    public MyResponse getNearAuditBlogDetails(@RequestParam int id, @RequestParam boolean isNext) {
        MyResponse myResponse = new MyResponse();

        System.out.println(id);
        System.out.println(isNext);
        Blog blogDetails = auditService.getNearAuditBlogDetails(id, isNext);
        myResponse.setData(blogDetails);

        return myResponse;
    }
}

