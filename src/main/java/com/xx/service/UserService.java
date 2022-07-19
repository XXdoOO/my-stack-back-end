package com.xx.service;

import com.xx.mapper.UserMapper;
import com.xx.pojo.User;
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
    private HttpSession session;

    public Map<String, Object> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        map.put("username", username);
        map.put("password", password);

        if (username != null && password != null && username.length() > 0 && password.length() > 0) {
            List<User> result = userMapper.login(map);
            resultMap.put("result", result);
            if (result.size() == 0) {
                resultMap.put("result", "用户名或密码错误！");
            } else {
                session.setAttribute("USER_SESSION", resultMap);
                resultMap.put("result", result);
            }
        } else {
            resultMap.put("result", "用户名或密码不符合规范！");
        }
        return resultMap;
    }

}
