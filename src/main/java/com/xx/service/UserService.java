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

}
