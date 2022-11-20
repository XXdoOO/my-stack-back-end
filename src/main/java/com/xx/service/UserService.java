package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.config.FilterConfigurer;
import com.xx.mapper.*;
import com.xx.pojo.*;
import com.xx.util.SaveFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogStarMapper blogStarMapper;

    @Autowired
    private BlogUpMapper blogUpMapper;

    @Autowired
    private BlogDownMapper blogDownMapper;

    @Autowired
    private DisableMapper disableMapper;

    @Autowired
    private HttpSession session;

    @Value("${images.local-path}")
    private String locPath;

    public User login(String username, String password) {
        System.out.println("用户：【" + username + "】请求登录");

        QueryWrapper<User> wrapper = new QueryWrapper<>();

        User user = userMapper.selectOne(wrapper.
                eq("username", username).
                eq("password", password));

        if (user == null) {
            return null;
        }

        if (user.getStatus()) {
            confirmUserStatus(user);
            if (!user.getStatus()) {
                session.setAttribute("USER_SESSION", user);
                user = getMyInfo();
            }
        }
        session.setAttribute("USER_SESSION", user);
        user = getMyInfo();

        return user;
    }

    // 确认用户状态
    public void confirmUserStatus(User user) {
        QueryWrapper<Disable> disableWrapper = new QueryWrapper<>();
        Disable disable = disableMapper.selectOne(disableWrapper.
                eq("username", user.getUsername()).
                orderByDesc("end_time").
                last("limit 1"));

        // 确认用户被封禁
        if (disable != null && disable.getEndTime() > System.currentTimeMillis()) {
            user.setDisableInfo(disable);
            user.setStatus(false);

            System.out.println("用户封禁解除");
        } else { // 用户已解封
            user.setStatus(true);

            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            userMapper.update(null, wrapper.set("status", false).
                    eq("username", user.getUsername()));

            QueryWrapper<Disable> queryWrapper = new QueryWrapper<>();
            disableMapper.delete(queryWrapper.eq("username", user.getUsername()));

            System.out.println("用户已封禁");
        }
    }

    public boolean register(String username, String password) {
        User result = userMapper.selectById(username);
        if (result != null) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        return userMapper.insert(user) == 1;
    }

    public void logout() {
        session.removeAttribute("USER_SESSION");
    }

    public User getUserInfo(String username) {
        QueryWrapper<Blog> blogWrapper = new QueryWrapper<>();
        QueryWrapper<BlogUp> upWrapper = new QueryWrapper<>();
        QueryWrapper<BlogDown> downWrapper = new QueryWrapper<>();

        User user = userMapper.selectById(username);

        if (user != null) {
            long passCount = blogMapper.selectCount(blogWrapper.eq("author_username", username).
                    eq("status", 1));
            long upCount = blogUpMapper.selectCount(upWrapper.eq("username", username));
            long downCount = blogDownMapper.selectCount(downWrapper.eq("username", username));

            user.setPassCount(passCount);
            user.setUpCount(upCount);
            user.setDownCount(downCount);
        }

        return user;
    }

    public User getMyInfo() {
        QueryWrapper<Blog> blogWrapper = new QueryWrapper<>();
        QueryWrapper<Blog> blogWrapper2 = new QueryWrapper<>();
        QueryWrapper<BlogStar> starWrapper = new QueryWrapper<>();

        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        long noPassCount = blogMapper.selectCount(blogWrapper.eq("author_username", username).eq("status", 0));
        long auditingCount = blogMapper.selectCount(blogWrapper2.eq("author_username", username).isNull("status"));
        long starCount = blogStarMapper.selectCount(starWrapper.eq("username", username));

        User user = getUserInfo(username);

        user.setNoPassCount(noPassCount);
        user.setAuditingCount(auditingCount);
        user.setStarCount(starCount);
        return user;
    }

    public User updateMyInfo(MultipartFile face, String nickname) {
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        if (face != null && nickname != null) {
            if (SaveFile.saveFile(face, locPath + "/avatarImg/", username + ".jpg")) {
                String avatar = "http://localhost:8080/avatarImg/" + username + ".jpg";

                UpdateWrapper<User> wrapper = new UpdateWrapper<>();
                userMapper.update(null, wrapper.
                        eq("username", username).
                        set("nickname", nickname).
                        set("avatar", avatar));
                return getMyInfo();
            }
        } else if (face == null && nickname != null) {
            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            userMapper.update(null, wrapper.
                    eq("username", username).
                    set("nickname", nickname));
            return getMyInfo();
        } else if (face != null) {
            if (SaveFile.saveFile(face, locPath + "/avatarImg/", username + ".jpg")) {
                String avatar = "http://localhost:8080/avatarImg/" + username + ".jpg";

                UpdateWrapper<User> wrapper = new UpdateWrapper<>();
                userMapper.update(null, wrapper.
                        eq("username", username).
                        set("avatar", avatar));
                return getMyInfo();
            }
        }
        return null;
    }

    public boolean deleteMy() {
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
        int i = userMapper.deleteById(username);

        return i == 1;
    }
}
