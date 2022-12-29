 package com.xx.controller;

 import com.xx.config.FilterConfigurer;
 import com.xx.pojo.vo.Blog;
 import com.xx.pojo.BlogView;
 import com.xx.pojo.User;
 import com.xx.service.AuditService;
 import com.xx.service.BlogService;
 import com.xx.util.Code;
 import com.xx.util.MyResponse;
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

 }

