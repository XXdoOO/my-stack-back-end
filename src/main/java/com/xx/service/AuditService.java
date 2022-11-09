package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.config.FilterConfigurer;
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

        List<User> userList = userMapper.selectList(wrapper.
                last("limit " + startIndex + ", " + pageSize));

        for (User user : userList) {
            QueryWrapper<Disable> queryWrapper = new QueryWrapper<>();
            Disable disable = disableMapper.selectOne(queryWrapper.eq("username", user.getUsername()).
                    last("limit 1"));
            if (disable != null) {
                user.setStatus(true);
            }
        }
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

        QueryWrapper<Disable> queryWrapper = new QueryWrapper<>();
        disableMapper.delete(queryWrapper.eq("username", username));

        return true;
    }

    public Map<String, Object> getWatchData() {
        // 获取用户情况，在线、离线（小黑屋）、离线（正常）、已注销
        Long total = userMapper.selectCount(null);
        int onlineCount = FilterConfigurer.session.size();
        long offlineCount = disableMapper.selectCount(null);
        long offlineCount2 = total - onlineCount - offlineCount;
        long deletedCount = userMapper.getDeletedUserCount();
        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("onlineCount", onlineCount);
            put("offlineCount", offlineCount);
            put("offlineCount2", offlineCount2);
            put("deletedCount", deletedCount);
        }};

        // 获取博客情况，已发布、待审核、不通过、已删除
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Blog> blogQueryWrapper2 = new QueryWrapper<>();
        QueryWrapper<Blog> blogQueryWrapper3 = new QueryWrapper<>();
        long passCount = blogMapper.selectCount(blogQueryWrapper.eq("status", 1));
        long auditingCount = blogMapper.selectCount(blogQueryWrapper2.isNull("status"));
        long noPassCount = blogMapper.selectCount(blogQueryWrapper3.eq("status", 0));
        long deletedCount2 = blogMapper.deletedBlogCount();

        HashMap<String, Object> map2 = new HashMap<String, Object>() {{
            put("passCount", passCount);
            put("auditingCount", auditingCount);
            put("noPassCount", noPassCount);
            put("deletedCount", deletedCount2);
        }};

        return new HashMap<String, Object>() {{
            put("userStatus", map);
            put("blogStatus", map2);
        }};
    }
}
