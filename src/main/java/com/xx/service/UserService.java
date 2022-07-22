package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mapper.BlogMapper;
import com.xx.mapper.UserMapper;
import com.xx.pojo.Blog;
import com.xx.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpSession session;

    public Map<String, String> login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Map<String, String> map = new HashMap<>();

        User user = userMapper.selectOne(wrapper.eq("username", username).eq("password", password));

        if (user != null) {
            session.setAttribute("USER_SESSION", user);
            map.put("nickname", user.getNickname());
            map.put("avatar", user.getAvatar());
        }

        return map;
    }

    public int register(String username, String password) {
        User user = new User(username, password, "null", "null", null);

        return userMapper.insert(user);
    }

}
