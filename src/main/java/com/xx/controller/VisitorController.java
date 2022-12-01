package com.xx.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.xx.pojo.*;
import com.xx.service.BlogService;
import com.xx.service.CommentsService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import com.xx.util.Code;
import com.xx.util.VerCodeGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class VisitorController {
    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentsService commentsService;

    @ResponseBody
    @PostMapping("login")
    public MyResponse login(@RequestParam String username, @RequestParam String password, @RequestParam String code,
                            HttpSession session) {
        MyResponse myResponse = new MyResponse();

        HashMap<String, Object> map =
                (HashMap<String, Object>) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

        if (System.currentTimeMillis() - ((long) map.get(
                "createTime")) >= 60000) {
            myResponse.setData("验证码已过期！");
            myResponse.setCode(Code.FAIL);
            return myResponse;
        } else if (!((String) map.get("code")).equalsIgnoreCase(code)) {
            myResponse.setData("验证码错误！");
            myResponse.setCode(Code.FAIL);
            return myResponse;
        }

        User userInfo = userService.getUserInfo(username);

        if (userInfo == null) {
            myResponse.setMsg("用户不存在！");
            myResponse.setCode(Code.USER_NOT_EXIST);
        } else {
            User user = userService.login(username, password);

            if (user == null) {
                myResponse.setMsg("用户名或密码错误！");
                myResponse.setCode(Code.USER_ERROR);
            } else if (user.getStatus()) {
                myResponse.setData(user.getDisableInfo());
                myResponse.setMsg("用户已被封禁！");
                myResponse.setCode(Code.USER_DISABLE);
            } else {
                myResponse.setData(user);
                myResponse.setMsg("登录成功");
            }
        }
        return myResponse;
    }

    @ResponseBody
    @PostMapping("register")
    public MyResponse register(@RequestParam String username, @RequestParam String password) {
        MyResponse myResponse = new MyResponse();

        if (userService.register(username, password)) {
            myResponse.setMsg("注册成功！");
        } else {
            myResponse.setMsg("用户已存在！");
            myResponse.setCode(Code.USER_ALREADY_EXIST);
        }

        return myResponse;
    }

    @ResponseBody
    @GetMapping("getBlogByKeywords")
    public MyResponse getBlogByKeywords(String keywords, Boolean orderBy, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        Map<String, Object> map = blogService.getBlogListByKeywords(keywords == null ? "" : keywords,
                (orderBy == null || !orderBy) ? "up" : "post_time", startIndex == null ? 0 : startIndex,
                pageSize == null ? 10 : pageSize);
        myResponse.setData(map);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getUserInfo")
    public MyResponse getUserInfo(@RequestParam String username) {
        MyResponse myResponse = new MyResponse();

        User user = userService.getUserInfo(username);
        myResponse.setData(user);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getUserPostBlogList")
    public MyResponse getUserBlogList(@RequestParam String username, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        Map<String, Object> map = blogService.getUserPostBlogList(username, startIndex == null ? 0 :
                        startIndex,
                pageSize == null ? 10 : pageSize);
        myResponse.setData(map);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getUserUpBlogList")
    public MyResponse getUserUpBlogList(@RequestParam String username, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        Map<String, Object> map = blogService.getUserUpBlogList(username, startIndex == null ? 0 :
                        startIndex,
                pageSize == null ? 10 : pageSize);
        myResponse.setData(map);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getUserDownBlogList")
    public MyResponse getUserDownBlogList(@RequestParam String username, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        Map<String, Object> map = blogService.getUserDownBlogList(username, startIndex == null ? 0 :
                        startIndex,
                pageSize == null ? 10 : pageSize);
        myResponse.setData(map);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getBlogDetails")
    public MyResponse getBlogDetails(@RequestParam long id) {
        MyResponse myResponse = new MyResponse();

        Blog blogDetails = blogService.getBlogDetails(id);

        myResponse.setData(blogDetails);
        return myResponse;
    }

    @ResponseBody
    @GetMapping("getCommentsList")
    public MyResponse getCommentsList(@RequestParam long id, Boolean orderBy, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        List<Comments> commentsList = commentsService.getCommentsList(id, (orderBy == null || !orderBy) ? "up" :
                "time", startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
        myResponse.setData(commentsList);
        return myResponse;
    }

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @ResponseBody
    @RequestMapping("sendEmail")
    public MyResponse commonEmail(ToEmail toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);

        message.setTo(toEmail.getTos());

        message.setSubject("您本次的验证码是");

        String verCode = VerCodeGenerate.generateVerCode();



        message.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:" + verCode + ",本验证码 5 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");

        mailSender.send(message);

        return new MyResponse();
    }

    @Autowired
    private Producer captchaProducer;

    @RequestMapping("/kaptcha")
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();

        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("createTime", System.currentTimeMillis());
            put("code", capText);
        }};
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, map);
        //向客户端写出
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
}
