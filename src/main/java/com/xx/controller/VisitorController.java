package com.xx.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
// import com.xx.service.BlogService;
import com.xx.service.CommentService;
import com.xx.mapper.BlogMapper;
import com.xx.pojo.dto.UserDTO;
import com.xx.pojo.vo.UserVo;
import com.xx.service.BlogService;
import com.xx.service.UserService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.HashMap;

@RestController
@RequestMapping("/")
public class VisitorController {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Resource
    private BlogMapper blogMapper;

    @ResponseBody
    @PostMapping("login")
    public MyResponse login(@RequestBody UserDTO userDTO) {
        HashMap<String, Object> map =
                (HashMap<String, Object>) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        String code = userDTO.getCode();

        if (StringUtils.isNotBlank(email)) {
            return MyResponse.error("邮箱为空或格式错误");
        }
        if (StringUtils.isNotBlank(password)) {
            return MyResponse.error("密码为空或格式错误");
        }
        if (StringUtils.isNotBlank(code) && code.length() == 4) {
            return MyResponse.error("验证码为空或格式错误");
        }

        if (map == null || System.currentTimeMillis() - ((long) map.get(
                "createTime")) >= 60000) {
            session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
            return MyResponse.fail("验证码已过期");
        } else if (!((String) map.get("code")).equalsIgnoreCase(userDTO.getCode())) {
            session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
            return MyResponse.fail("验证码错误");
        }

        if (!userService.isExistUser(userDTO.getEmail())) {
            return MyResponse.fail("用户不存在");
        } else {
            UserVo user = userService.login(userDTO.getEmail(), userDTO.getPassword());

            if (user == null) {
                return MyResponse.fail("邮箱或密码错误");
            } else if (user.getIsDisable()) {
                return MyResponse.error("用户已被封禁", user.getDisableInfo());
            } else {
                return MyResponse.success("登录成功", user);
            }
        }
    }

    @ResponseBody
    @PostMapping("register")
    public MyResponse register(@RequestBody UserDTO userDTO) {
        HashMap<String, Object> map =
                (HashMap<String, Object>) session.getAttribute("REGISTER_CODE");

        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        String code = userDTO.getCode();

        if (StringUtils.isNotBlank(email)) {
            return MyResponse.error("邮箱为空或格式错误");
        }
        if (StringUtils.isNotBlank(password)) {
            return MyResponse.error("密码为空或格式错误");
        }
        if (StringUtils.isNotBlank(code) && code.length() == 6) {
            return MyResponse.error("验证码为空或格式错误");
        }


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
    public MyResponse getBlogByKeywords(String keywords, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(1, 10);

        return MyResponse.success(new PageInfo<>(blogService.getBlogListByKeywords(keywords)));
    }


    @ResponseBody
    @GetMapping("user/{userId}")
    public MyResponse getUserInfo(@PathVariable Long userId) {
        return MyResponse.success(userService.getUserInfo(userId));
    }

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
    @ResponseBody
    @GetMapping("blog/{blogId}")
    public MyResponse getBlogDetails(@PathVariable long blogId) {
        return MyResponse.success(blogService.getBlogDetails(blogId));
    }


    @ResponseBody
    @GetMapping("getCommentsList")
    public MyResponse getCommentsList(@RequestParam long blogId, Long parent, String orderBy) {
        return MyResponse.success(new PageInfo<>(commentService.getCommentsList(blogId, parent == null ? 0 : parent, orderBy)));
    }

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

        userService.sendRegisterCode(email);
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
