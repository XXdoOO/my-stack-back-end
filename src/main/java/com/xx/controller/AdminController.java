package com.xx.controller;

import com.xx.config.FilterConfigurer;
import com.xx.pojo.Blog;
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
    private AuditService auditService;

    @Autowired
    private BlogService blogService;

    @ResponseBody
    @PutMapping("/auditBlog")
    public MyResponse auditBlog(@RequestParam int id, @RequestParam boolean isPass) {
        MyResponse myResponse = new MyResponse();

        int result = auditService.auditBlog(id, isPass);

        if (result == 1) {
            myResponse.setMsg("审核成功！");
        } else {
            myResponse.setMsg("审核失败！");
            myResponse.setCode(Code.FAIL);
        }

        return myResponse;
    }

    @ResponseBody
    @GetMapping("/getBlogByKeywords")
    public MyResponse getBlogByKeywords(String keywords, Boolean orderBy, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        Map<String, Object> map = blogService.getBlogListByKeywords2(keywords == null ? "" : keywords,
                (orderBy == null || !orderBy) ? "up" : "post_time", startIndex == null ? 0 : startIndex,
                pageSize == null ? 10 : pageSize);
        myResponse.setData(map);
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
    @GetMapping("/getUserList")
    public MyResponse getUserList(Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        Map<String, Object> map = auditService.getUserList(startIndex == null ? 0 : startIndex,
                pageSize == null ? 10 :
                        pageSize);

        myResponse.setData(map);
        myResponse.setMsg("获取成功！");

        return myResponse;
    }

    @ResponseBody
    @PutMapping("/disableUser")
    public MyResponse disableUser(@RequestParam String username, @RequestParam long timestamp,
                                  @RequestParam String reason) {
        MyResponse myResponse = new MyResponse();

        boolean result = auditService.setUserDisableTime(username, timestamp, reason);

        myResponse.setMsg(result ? "设置成功！" : "设置失败！");

        if (result) {
            myResponse.setMsg("设置成功！");

            Map<String, HttpSession> sessionMap = FilterConfigurer.session;
            for (String key : sessionMap.keySet()) {
                User user = (User) sessionMap.get(key).getAttribute("USER_SESSION");
                if (username.equals(user.getUsername())) {
                    sessionMap.get(key).invalidate();
                    break;
                }
            }
        } else {
            myResponse.setCode(Code.FAIL);
            myResponse.setMsg("设置失败！");
        }

        return myResponse;
    }

    @ResponseBody
    @PutMapping("/cancelDisable")
    public MyResponse cancelDisable(@RequestParam String username) {
        MyResponse myResponse = new MyResponse();

        boolean result = auditService.cancelDisable(username);

        myResponse.setMsg(result ? "设置成功！" : "设置失败！");

        if (result) {
            myResponse.setMsg("设置成功！");
        } else {
            myResponse.setCode(Code.FAIL);
            myResponse.setMsg("设置失败！");
        }

        return myResponse;
    }
}

