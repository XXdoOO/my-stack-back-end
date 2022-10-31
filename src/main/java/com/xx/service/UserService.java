package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mapper.*;
import com.xx.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private HttpSession session;

    public User login(String username, String password) {
        System.out.println("用户：【" + username + "】请求登录");

        QueryWrapper<User> wrapper = new QueryWrapper<>();

        User user = userMapper.selectOne(wrapper.eq("username", username).
                eq("password", password).
                last("and disable_time <= register_time"));
        User userInfo = null;
        if (user != null) {
            session.setAttribute("USER_SESSION", user);

            userInfo = getMyInfo();
        }

        return userInfo;
    }

    public int register(String username, String password) {
        User result = userMapper.selectById(username);
        if (result != null) {
            return -1;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        return userMapper.insert(user);
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
            long passCount = blogMapper.selectCount(blogWrapper.eq("author_username", username).eq("status", 1));
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
}
