package com.xx.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
// import com.xx.service.BlogService;
import com.xx.pojo.dto.CommentDTO;
import com.xx.service.CommentService;
import com.xx.mapper.BlogMapper;
import com.xx.pojo.dto.UserDTO;
import com.xx.pojo.vo.UserVo;
import com.xx.service.BlogService;
import com.xx.service.UserService;
import com.xx.util.Code;
import com.xx.util.IpUtil;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private Producer captchaProducer;

    @ResponseBody
    @RequestMapping("ip")
    public MyResponse getIp(HttpServletRequest request) {
        System.out.println(IpUtil.getIpAddr(request));
        return MyResponse.success(IpUtil.getIpAddr(request));
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

        if (userService.sendRegisterCode(email)) {
            return MyResponse.success("发送成功");
        }
        return MyResponse.fail("邮箱无效");
    }

    @RequestMapping("/kaptcha")
    public void getKaptchaImage(HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();

        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, new Code(capText));

        Code map = (Code) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

        System.out.println(map);
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

    @ResponseBody
    @PostMapping("login")
    public MyResponse login(@RequestBody @Validated UserDTO userDTO) {
        Code map = (Code) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

        System.out.println(map);

        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        String code = userDTO.getCode();

        if (StringUtils.isBlank(email)) {
            return MyResponse.error("邮箱为空或格式错误");
        }
        if (StringUtils.isBlank(password)) {
            return MyResponse.error("密码为空或格式错误");
        }
        if (StringUtils.isBlank(code) && code.length() == 4) {
            return MyResponse.error("验证码为空或格式错误");
        }

        if (map == null || System.currentTimeMillis() - map.getCreateTime() >= 60000) {
            session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
            return MyResponse.fail("验证码已过期");
        } else if (!(map.getCode()).equalsIgnoreCase(userDTO.getCode())) {
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
    public MyResponse register(@RequestBody @Validated UserDTO userDTO) {
        final Code map = (Code) session.getAttribute(Code.REGISTER_CODE);

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


        if (map == null || System.currentTimeMillis() - map.getCreateTime() >= 300000) {
            return MyResponse.fail("验证码已过期");
        } else if (map.getEmail().equals(email) && !map.getCode().equals(code)) {
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

    // @ResponseBody
    // @GetMapping("user/{userId}")
    // public MyResponse getUserInfo(@PathVariable Long userId) {
    //     return MyResponse.success(userService.getUserInfo(userId));
    // }

    @ResponseBody
    @GetMapping("blog/{blogId}")
    public MyResponse getBlogDetails(@PathVariable long blogId) {
        return MyResponse.success(blogService.getBlogDetails(blogId));
    }

    @GetMapping("getCommentsList")
    private MyResponse getCommentsList(@Validated CommentDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        return MyResponse.success(new PageInfo<>(commentService.getCommentsList(dto)));
    }
}
