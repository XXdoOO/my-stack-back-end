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

    public Map<String, Object> login(String username, String password) {
        System.out.println("用户：【" + username + "】请求登录");

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Map<String, Object> map = new HashMap<>();

        User user = userMapper.selectOne(wrapper.eq("username", username).eq("password", password));

        if (user != null) {
            session.setAttribute("USER_SESSION", user);
            map.put("username", user.getUsername());
            map.put("nickname", user.getNickname());
            map.put("avatar", user.getAvatar());
            map.put("identity", user.getIdentity());
            map.put("registerTime", user.getRegisterTime());
            map.put("disableTime", user.getDisableTime());
        }

        return map;
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

    public Map<String, Object> getUserInfo(String username) {
        HashMap<String, Object> map = new HashMap<>();
        QueryWrapper<Blog> blogWrapper = new QueryWrapper<>();
        QueryWrapper<BlogStar> starWrapper = new QueryWrapper<>();
        QueryWrapper<BlogUp> upWrapper = new QueryWrapper<>();
        QueryWrapper<BlogDown> downWrapper = new QueryWrapper<>();

        User user = userMapper.selectById(username);

        String nickname = user.getNickname();
        String avatar = user.getAvatar();
        long postCount = blogMapper.selectCount(blogWrapper.eq("author_username", username).eq("status", 1));
        long starCount = blogStarMapper.selectCount(starWrapper.eq("username", username));
        long upCount = blogUpMapper.selectCount(upWrapper.eq("username", username));
        long downCount = blogDownMapper.selectCount(downWrapper.eq("username", username));

        map.put("nickname", nickname);
        map.put("avatar", avatar);
        map.put("postCount", postCount);
        map.put("upCount", upCount);
        map.put("downCount", downCount);
        map.put("starCount", starCount);

        return map;
    }
}
