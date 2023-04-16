package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xx.config.SystemConfig;
import com.xx.mapper.BlogMapper;
import com.xx.mapper.DisableMapper;
import com.xx.mapper.RecordMapper;
import com.xx.mapper.UserMapper;
import com.xx.pojo.dto.UserDTO;
import com.xx.pojo.entity.Blog;
import com.xx.pojo.entity.Disable;
import com.xx.pojo.entity.Record;
import com.xx.pojo.entity.User;
import com.xx.pojo.vo.UserVO;
import com.xx.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DisableMapper disableMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public UserVO login(String email, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        User user = userMapper.selectOne(wrapper.
                eq("email", email).
                eq("password", password));

        if (user == null) {
            return null;
        }

        UserVO userVo = new UserVO();

        BeanUtils.copyProperties(user, userVo, "password");

        User user1 = new User();
        user1.setId(user.getId());

        if (!user.getEnabled()) {
            userVo.setDisableInfo(getUserDisableInfo(user.getId()));

            if (userVo.getDisableInfo() == null) {
                userVo.setEnabled(true);

                user1.setEnabled(true);
                user1.setIp(IpUtils.getIpAddr(request));
                user1.setIpTerritory(AddressUtils.getRealAddressByIP(user1.getIp()));
                session.setAttribute("USER_SESSION", user);
            } else {
                return userVo;
            }
        } else {
            user1.setIp(IpUtils.getIpAddr(request));
            user1.setIpTerritory(AddressUtils.getRealAddressByIP(user1.getIp()));

            UserVO myInfo = userMapper.getUserInfo(null, user.getId());

            userVo.setAuditingCount(myInfo.getAuditingCount());
            userVo.setPassCount(myInfo.getPassCount());
            userVo.setNoPassCount(myInfo.getNoPassCount());
            userVo.setUp(myInfo.getUp());
            userVo.setDown(myInfo.getDown());
            userVo.setStar(myInfo.getStar());
            userVo.setHistory(myInfo.getHistory());

            session.setAttribute("USER_SESSION", user);
        }
        userMapper.updateById(user1);
        return userVo;
    }

    public UserVO getUserInfo() {
        User user_session = (User) session.getAttribute("USER_SESSION");
        String email = user_session.getEmail();
        String password = user_session.getPassword();

        QueryWrapper<User> wrapper = new QueryWrapper<>();

        User user = userMapper.selectOne(wrapper.
                eq("email", email).
                eq("password", password));

        UserVO userVo = new UserVO();

        BeanUtils.copyProperties(user, userVo, "password");

        userVo.setDisableInfo(getUserDisableInfo(user.getId()));

        UserVO myInfo = userMapper.getUserInfo(null, user.getId());

        userVo.setAuditingCount(myInfo.getAuditingCount());
        userVo.setPassCount(myInfo.getPassCount());
        userVo.setNoPassCount(myInfo.getNoPassCount());
        userVo.setUp(myInfo.getUp());
        userVo.setDown(myInfo.getDown());
        userVo.setStar(myInfo.getStar());
        userVo.setHistory(myInfo.getHistory());
        return userVo;
    }

    public Disable getUserDisableInfo(long id) {
        QueryWrapper<Disable> disableWrapper = new QueryWrapper<>();
        Disable disable = disableMapper.selectOne(disableWrapper.
                eq("user_id", id).
                orderByDesc("end_time").
                last("limit 1"));

        // 确认用户被封禁
        if (disable != null && disable.getEndTime().getTime() > System.currentTimeMillis()) {
            return disable;
        } else { // 用户已解封

            return null;
        }
    }

    public boolean isExistUser(String email) {
        return userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
    }

    public boolean register(String email, String password) {
        if (isExistUser(email)) {
            return false;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        String avatar = "/avatar/" + (new Random().nextInt(48) + 1) + ".jpg";
        user.setAvatar(avatar);

        if (userMapper.insert(user) == 1) {
            File workDir = new File(SystemConfig.getLocalPath() + user.getId());
            return workDir.mkdir();
        }
        return false;
    }

    public void logout() {
        session.invalidate();
    }

    public boolean updateInfo(UserDTO dto) {
        Long id = getCurrentUser().getId();
        User user = new User();
        user.setId(id);
        user.setNickname(dto.getNickname());

        String url = SaveFile.saveAvatar(dto.getAvatar());
        if (url != null) {
            user.setAvatar(url);
        }
        return userMapper.updateById(user) == 1;
    }

    public boolean cancelAccount() {
        Long id = getCurrentUser().getId();

        recordMapper.delete(new LambdaQueryWrapper<Record>().eq(Record::getCreateBy, id));
        blogMapper.delete(new LambdaQueryWrapper<Blog>().eq(Blog::getCreateBy, id));

        return userMapper.deleteById(id) == 1;
    }

    public boolean sendRegisterCode(String email) {
        String code = sendEmailCode(email);

        if (StringUtils.isNotBlank(code)) {
            session.setAttribute(Code.REGISTER_CODE, new Code(email, code));
            return true;
        } else {
            return false;
        }
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

        try {
            mailSender.send(message);
        } catch (MailSendException e) {
            return null;
        }
        return verCode;
    }

    public List<User> getUserList(UserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        user.setAdmin(dto.getAdmin());
        user.setEnabled(dto.getEnabled());
        user.setIp(dto.getIp());
        user.setIpTerritory(dto.getIpTerritory());

        return userMapper.selectList(new QueryWrapper<>(user).
                ge(dto.getCreateTime()[0] != null, "create_time", dto.getCreateTime()[0]).
                le(dto.getCreateTime()[1] != null, "create_time", dto.getCreateTime()[1]));
    }

    public UserVO getUserInfo(Long userId) {
        return userMapper.getUserInfo(userId, getCurrentUser().getId());
    }

    public void disableUser(UserDTO dto) {
        if (dto.getEnabled()) {
            QueryWrapper<Disable> wrapper = new QueryWrapper<>();
            disableMapper.delete(wrapper.eq("user_id", dto.getUserId()));
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            userMapper.update(null, updateWrapper.set("is_enabled", true).
                    eq("id", dto.getUserId()));
        } else if (StringUtils.isNotBlank(dto.getReason()) && dto.getMinutes() != null && dto.getMinutes() > 0) {
            Disable disable = new Disable();

            disable.setUserId(dto.getUserId());
            disable.setReason(dto.getReason());
            disable.setEndTime(new Date(System.currentTimeMillis() + dto.getMinutes() * 60000));
            disableMapper.insert(disable);

            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            userMapper.update(null, wrapper.set("is_enabled", false).
                    eq("id", dto.getUserId()));
        }
    }

    public User getCurrentUser() {
        Object user = session.getAttribute("USER_SESSION");
        if (user == null) {
            return new User();
        }
        return (User) user;
    }
}
