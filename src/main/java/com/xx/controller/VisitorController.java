package com.xx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
// import com.xx.service.BlogService;
// import com.xx.service.CommentsService;
import com.xx.pojo.BlogView;
import com.xx.pojo.User;
import com.xx.service.BlogService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import com.xx.util.VerCodeGenerate;
import com.xx.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.HashMap;

@Controller
@RequestMapping("/")
public class VisitorController {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @Autowired
    private BlogService blogService;
    //
    // @Autowired
    // private CommentsService commentsService;

    @ResponseBody
    @PostMapping("login")
    public MyResponse login(@RequestBody UserVo userVo) {

        HashMap<String, Object> map =
                (HashMap<String, Object>) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

        String email = userVo.getEmail();
        String password = userVo.getPassword();
        String code = userVo.getCode();

        if (map == null || System.currentTimeMillis() - ((long) map.get(
                "createTime")) >= 60000) {
            session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
            return MyResponse.fail("验证码已过期");
        } else if (!((String) map.get("code")).equalsIgnoreCase(code)) {
            session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
            return MyResponse.fail("验证码错误");
        }

        if (!userService.isExistUser(email)) {
            return MyResponse.fail("用户不存在");
        } else {
            User user = userService.login(email, password);

            if (user == null) {
                return MyResponse.fail("邮箱或密码错误");
            } else if (user.getStatus()) {
                return MyResponse.error("用户已被封禁", user.getDisableInfo());
            } else {
                return MyResponse.success("登录成功", user);
            }
        }
    }

    @ResponseBody
    @PostMapping("register")
    public MyResponse register(@RequestBody UserVo userVo) {
        HashMap<String, Object> map =
                (HashMap<String, Object>) session.getAttribute("EMAIL_CODE");

        String email = userVo.getEmail();
        String password = userVo.getPassword();
        String code = userVo.getCode();

        if (map == null || System.currentTimeMillis() - ((long) map.get(
                "createTime")) >= 300000) {
            return MyResponse.fail("验证码已过期");
        } else if (map.get("email").equals(email) && !map.get("code").equals(code)) {
            return MyResponse.fail("邮箱或验证码错误");
        } else {
            if (userService.register(email, password)) {
                return MyResponse.success("注册成功");
            } else {
                return MyResponse.fail("用户已存在");
            }
        }
    }


    @ResponseBody
    @GetMapping("getBlogByKeywords")
    public MyResponse getBlogByKeywords(String keywords, Boolean orderBy, Integer startIndex, Integer pageSize) {
        MyResponse myResponse = new MyResponse();

        IPage<BlogView> list = blogService.getBlogListByKeywords(keywords);
        myResponse.setData(list);
        return myResponse;
    }

    //
    // @ResponseBody
    // @GetMapping("getUserInfo")
    // public MyResponse getUserInfo(@RequestParam String username) {
    //     MyResponse myResponse = new MyResponse();
    //
    //     User user = userService.getUserInfo(username);
    //     myResponse.setData(user);
    //     return myResponse;
    // }
    //
    // @ResponseBody
    // @GetMapping("getUserPostBlogList")
    // public MyResponse getUserBlogList(@RequestParam String username, Integer startIndex, Integer pageSize) {
    //     MyResponse myResponse = new MyResponse();
    //
    //     Map<String, Object> map = blogService.getUserPostBlogList(username, startIndex == null ? 0 :
    //                     startIndex,
    //             pageSize == null ? 10 : pageSize);
    //     myResponse.setData(map);
    //     return myResponse;
    // }
    //
    // @ResponseBody
    // @GetMapping("getUserUpBlogList")
    // public MyResponse getUserUpBlogList(@RequestParam String username, Integer startIndex, Integer pageSize) {
    //     MyResponse myResponse = new MyResponse();
    //
    //     Map<String, Object> map = blogService.getUserUpBlogList(username, startIndex == null ? 0 :
    //                     startIndex,
    //             pageSize == null ? 10 : pageSize);
    //     myResponse.setData(map);
    //     return myResponse;
    // }
    //
    // @ResponseBody
    // @GetMapping("getUserDownBlogList")
    // public MyResponse getUserDownBlogList(@RequestParam String username, Integer startIndex, Integer pageSize) {
    //     MyResponse myResponse = new MyResponse();
    //
    //     Map<String, Object> map = blogService.getUserDownBlogList(username, startIndex == null ? 0 :
    //                     startIndex,
    //             pageSize == null ? 10 : pageSize);
    //     myResponse.setData(map);
    //     return myResponse;
    // }
    //
    // @ResponseBody
    // @GetMapping("getBlogDetails")
    // public MyResponse getBlogDetails(@RequestParam long id) {
    //     MyResponse myResponse = new MyResponse();
    //
    //     Blog blogDetails = blogService.getBlogDetails(id);
    //
    //     myResponse.setData(blogDetails);
    //     return myResponse;
    // }
    //
    // @ResponseBody
    // @GetMapping("getCommentsList")
    // public MyResponse getCommentsList(@RequestParam long id, Boolean orderBy, Integer startIndex, Integer pageSize) {
    //     MyResponse myResponse = new MyResponse();
    //
    //     List<Comments> commentsList = commentsService.getCommentsList(id, (orderBy == null || !orderBy) ? "up" :
    //             "time", startIndex == null ? 0 : startIndex, pageSize == null ? 10 : pageSize);
    //     myResponse.setData(commentsList);
    //     return myResponse;
    // }
    //
    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @ResponseBody
    @RequestMapping("sendCode")
    public MyResponse commonEmail(@RequestParam String email) {
        String regex = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        if (!email.matches(regex)) {
            return MyResponse.fail("邮箱格式错误");
        }

        if (userService.isExistUser(email)) {
            return MyResponse.fail("用户已存在");
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您本次的验证码是");

        String verCode = VerCodeGenerate.generateVerCode();


        message.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:" + verCode + ",本验证码 5 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");

        mailSender.send(message);

        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("createTime", System.currentTimeMillis());
            put("email", email);
            put("code", verCode);
        }};
        session.setAttribute("EMAIL_CODE", map);
        return MyResponse.success("发送成功");
    }

    @Autowired
    private Producer captchaProducer;

    @RequestMapping("/kaptcha")
    public void getKaptchaImage(HttpServletResponse response) throws Exception {
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
