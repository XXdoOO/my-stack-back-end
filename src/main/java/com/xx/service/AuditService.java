package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.mapper.*;
import com.xx.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuditService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogViewMapper blogViewMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DisableMapper disableMapper;

    @Autowired
    private HttpSession session;

    public int auditBlog(int id, boolean status) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        return blogMapper.update(null, wrapper.
                eq("id", id).
                isNull("status").
                set("status", status));
    }

    public Blog getAuditBlogDetails(int id) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();

        Blog blog = blogMapper.selectOne(wrapper.
                isNull("status").
                eq("id", id));

        if (blog != null) {
            String authorUsername = blog.getAuthorUsername();

            blog.setAuthorInfo(userService.getUserInfo(authorUsername));
        }

        return blog;
    }

    public Map<String, Object> getUserList(int startIndex, int pageSize) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Long total = userMapper.selectCount(wrapper);
        List<User> userList = userMapper.selectList(wrapper.last("limit " + startIndex + ", " + pageSize));
        return new HashMap<String, Object>() {{
            put("total", total);
            put("list", userList);
        }};
    }

    public boolean setUserDisableTime(String username, long timestamp, String reason) {
        User user = userMapper.selectById(username);

        if (user == null) {
            return false;
        }

        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        userMapper.update(null, wrapper.
                eq("username", username).
                set("status", 1));

        disableMapper.setUserDisableTime(new HashMap<String, Object>() {{
            put("username", username);
            put("timestamp", timestamp);
            put("reason", reason);
        }});

        return true;
    }

    public boolean cancelDisable(String username) {
        User user = userMapper.selectById(username);

        if (user == null) {
            return false;
        }

        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        userMapper.update(null, wrapper.
                eq("username", username).
                set("status", 0));

        QueryWrapper<Disable> queryWrapper = new QueryWrapper<>();
        disableMapper.delete(queryWrapper.eq("username", username));

        return true;
    }
}
