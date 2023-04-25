package com.xx.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.xx.pojo.dto.BlogDTO;
import com.xx.pojo.dto.CommentDTO;
import com.xx.pojo.dto.UserDTO;
import com.xx.service.BlogService;
import com.xx.service.CommentService;
import com.xx.service.UserService;
import com.xx.util.AddressUtils;
import com.xx.util.Code;
import com.xx.util.IpUtils;
import com.xx.util.MyResponse;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/")
public class VisitorController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("ip")
    public MyResponse getIp(HttpServletRequest request) {
        return MyResponse.success(AddressUtils.getRealAddressByIP(IpUtils.getIpAddr(request)));
    }

    //    @LimitRequest(time = 50000)
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
    public void getKaptchaImage() throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String verCode = captchaProducer.createText();

        redisTemplate.opsForValue().set(IpUtils.getIpAddr(request), verCode, 1, TimeUnit.MINUTES);

        BufferedImage bi = captchaProducer.createImage(verCode);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @PostMapping("login")
    public MyResponse login(@RequestBody @Validated UserDTO userDTO) throws Exception {
        String verCode = (String) redisTemplate.opsForValue().get(IpUtils.getIpAddr(request));
        if (verCode == null) {
            getKaptchaImage();
            return fail("验证码错误");
        }
        String token = userService.login(userDTO.getEmail(), userDTO.getPassword());
        return token != null ? success(token) : fail();
    }

    @PostMapping("register")
    public MyResponse register(@RequestBody @Validated UserDTO userDTO) {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        String code = userDTO.getCode();

        String verCode = (String) redisTemplate.opsForValue().get(email);

        if (verCode == null) {
            return MyResponse.fail("验证码已过期");
        } else if (!verCode.equals(code)) {
            return MyResponse.fail("验证码错误");
        } else {
            if (userService.register(email, password)) {
                return MyResponse.success("注册成功");
            } else {
                return MyResponse.fail("用户已存在");
            }
        }
    }

    @GetMapping("getBlogList")
    public MyResponse getBlogList(BlogDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        return MyResponse.success(new PageInfo<>(blogService.getBlogList(dto)));
    }

    @GetMapping("getUserInfo/{userId}")
    public MyResponse getUserInfo(@PathVariable Long userId) {
        return MyResponse.success(userService.getUserInfo(userId));
    }

    @GetMapping("blog/{blogId}")
    public MyResponse getBlogDetails(@PathVariable long blogId) {
        return MyResponse.success(blogService.getBlogDetails(blogId));
    }

    @GetMapping("getCommentsList")
    private MyResponse getCommentsList(CommentDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        return MyResponse.success(new PageInfo<>(commentService.getCommentsList(dto)));
    }
}
