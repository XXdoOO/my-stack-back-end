package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.mapper.*;
import com.xx.pojo.dto.UserDTO;
import com.xx.pojo.entity.Disable;
import com.xx.pojo.entity.User;
import com.xx.pojo.po.UserPo;
import com.xx.pojo.vo.BlogVo;
import com.xx.pojo.vo.UserVo;
import com.xx.util.SaveFile;
import com.xx.util.VerCodeGenerate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    //    @Autowired
//    private BlogMapper blogMapper;
//
//    @Autowired
//    private BlogStarMapper blogStarMapper;
//
//    @Autowired
//    private BlogUpMapper blogUpMapper;
//
//    @Autowired
//    private BlogDownMapper blogDownMapper;
//
    @Autowired
    private DisableMapper disableMapper;

    @Autowired
    private HttpSession session;

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;


    @Value("${images.local-path}")
    private String locPath;

    public UserVo login(String email, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        User user = userMapper.selectOne(wrapper.
                eq("email", email).
                eq("password", password));

        if (user == null) {
            return null;
        }

        UserVo userVo = new UserVo();

        userVo.setId(user.getId());
        userVo.setEmail(user.getEmail());
        userVo.setAvatar(user.getAvatar());
        userVo.setCreateTime(user.getCreateTime());
        userVo.setIsDisable(user.getDisable());
        userVo.setIsAdmin(user.getAdmin());

        if (user.getDisable()) {
            userVo.setDisableInfo(getUserDisableInfo(user.getId()));

            if (!user.getDisable()) {
                user.setDisable(false);
                session.setAttribute("USER_SESSION", user);
            }
        } else {
            session.setAttribute("USER_SESSION", user);
            UserPo userInfo = userMapper.getMyInfo(user.getId());

            userVo.setNickname(userInfo.getNickname());
            userVo.setAuditingCount(userInfo.getAuditingCount());
            userVo.setPassCount(userInfo.getPassCount());
            userVo.setNoPassCount(userInfo.getNoPassCount());
            userVo.setUpCount(userInfo.getUpCount());
            userVo.setDownCount(userInfo.getDownCount());
            userVo.setStarCount(userInfo.getStarCount());
        }

        return userVo;
    }

    public Disable getUserDisableInfo(long id) {
        QueryWrapper<Disable> disableWrapper = new QueryWrapper<>();
        Disable disable = disableMapper.selectOne(disableWrapper.
                eq("user_id", id).
                orderByDesc("end_time").
                last("limit 1"));

        // 确认用户被封禁
        if (disable != null && disable.getEndTime() > System.currentTimeMillis()) {
            return disable;
        } else { // 用户已解封
            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            userMapper.update(null, wrapper.set("status", false).
                    eq("id", id));

            QueryWrapper<Disable> queryWrapper = new QueryWrapper<>();
            disableMapper.delete(queryWrapper.eq("user_id", id));
            return null;
        }
    }

    public boolean isExistUser(String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        return userMapper.selectCount(wrapper.eq("email", email)) > 0;
    }


    public boolean register(String email, String password) {
        if (isExistUser(email)) {
            return false;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        // 随机头像
        String avatar = "/avatar/" + (new Random().nextInt(48) + 1) + ".jpg";
        user.setAvatar(avatar);

        return userMapper.insert(user) == 1;
    }

    public void logout() {
        session.invalidate();
    }

    public UserVo getUserInfo(long id) {
        UserPo userInfo = userMapper.getUserInfo(id);

        UserVo userVo = new UserVo();

        userVo.setNickname(userInfo.getNickname());
        userVo.setPassCount(userInfo.getPassCount());
        userVo.setUpCount(userInfo.getUpCount());
        userVo.setDownCount(userInfo.getDownCount());
        return userVo;
    }

    public boolean updateMyInfo(UserDTO userDTO) {
        Long id = ((User) session.getAttribute("USER_SESSION")).getId();

        User user = new User();
        // user.setEmail(userDTO.getEmail());
        // user.setPassword(userDTO.getPassword());
        user.setNickname(userDTO.getNickname());

        MultipartFile avatar = userDTO.getAvatar();
        if (avatar != null) {
            SaveFile.saveFile(avatar, locPath + "/avatar/", id + ".jpg");

            user.setAvatar("/avatar/" + id + ".jpg");
        }

        return userMapper.updateById(user) == 1;
    }

    public boolean deleteSelf() {
        Long id = ((User) session.getAttribute("USER_SESSION")).getId();

        return userMapper.deleteById(id) == 1;
    }

    public void sendRegisterCode(String email) {
        String code = sendEmailCode(email);

        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("createTime", System.currentTimeMillis());
            put("email", email);
            put("code", code);
        }};
        session.setAttribute("REGISTER_CODE", map);
    }

    public String sendEmailCode(String email) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您本次的验证码是");

        String verCode = VerCodeGenerate.generateVerCode();


        message.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:" + verCode + ",本验证码 5 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");

        mailSender.send(message);

        return verCode;
    }
}
