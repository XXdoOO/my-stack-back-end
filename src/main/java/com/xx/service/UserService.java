package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.mapper.*;
import com.xx.pojo.*;
import com.xx.pojo.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Random;

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

    public User login(String email, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        User user = userMapper.selectOne(wrapper.
                eq("email", email).
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
        } else {
            session.setAttribute("USER_SESSION", user);
            user = getMyInfo();
        }

        return user;
    }

    public void confirmUserStatus(User user) {
        QueryWrapper<Disable> disableWrapper = new QueryWrapper<>();
        Disable disable = disableMapper.selectOne(disableWrapper.
                eq("username", user.getId()).
                orderByDesc("end_time").
                last("limit 1"));

        // 确认用户被封禁
        if (disable != null && disable.getEndTime() > System.currentTimeMillis()) {
            user.setDisableInfo(disable);
            user.setStatus(true);
        } else { // 用户已解封
            user.setStatus(false);

            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            userMapper.update(null, wrapper.set("status", false).
                    eq("id", user.getId()));

            QueryWrapper<Disable> queryWrapper = new QueryWrapper<>();
            disableMapper.delete(queryWrapper.eq("user_id", user.getId()));
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

    public User getUserInfo(long id) {
        User user = userMapper.selectById(id);

        if (user != null) {
            QueryWrapper<Blog> blogWrapper = new QueryWrapper<>();
            QueryWrapper<BlogUp> upWrapper = new QueryWrapper<>();
            QueryWrapper<BlogDown> downWrapper = new QueryWrapper<>();

            long passCount = blogMapper.selectCount(blogWrapper.eq("author_id", id).
                    eq("status", 1));
            long upCount = blogUpMapper.selectCount(upWrapper.eq("blog_id", id));
            long downCount = blogDownMapper.selectCount(downWrapper.eq("blog_id", id));

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

        Long id = ((User) session.getAttribute("USER_SESSION")).getId();

        long noPassCount = blogMapper.selectCount(blogWrapper.eq("author_id", id).eq("status", 0));
        long auditingCount = blogMapper.selectCount(blogWrapper2.eq("author_id", id).eq("status", 0));
        long starCount = blogStarMapper.selectCount(starWrapper.eq("user_id", id));

        User user = getUserInfo(id);

        user.setNoPassCount(noPassCount);
        user.setAuditingCount(auditingCount);
        user.setStarCount(starCount);
        return user;
    }
    //
    // public User updateMyInfo(MultipartFile face, String nickname) {
    //     String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
    //
    //     if (face != null && nickname != null) {
    //         if (SaveFile.saveFile(face, locPath + "/avatar/", username + ".jpg")) {
    //             String avatar = "/avatar/" + username + ".jpg";
    //
    //             UpdateWrapper<User> wrapper = new UpdateWrapper<>();
    //             userMapper.update(null, wrapper.
    //                     eq("username", username).
    //                     set("nickname", nickname).
    //                     set("avatar", avatar));
    //             return getMyInfo();
    //         }
    //     } else if (face == null && nickname != null) {
    //         UpdateWrapper<User> wrapper = new UpdateWrapper<>();
    //         userMapper.update(null, wrapper.
    //                 eq("username", username).
    //                 set("nickname", nickname));
    //         return getMyInfo();
    //     } else if (face != null) {
    //         if (SaveFile.saveFile(face, locPath + "/avatar/", username + ".jpg")) {
    //             String avatar = "/avatar/" + username + ".jpg";
    //
    //             UpdateWrapper<User> wrapper = new UpdateWrapper<>();
    //             userMapper.update(null, wrapper.
    //                     eq("username", username).
    //                     set("avatar", avatar));
    //             return getMyInfo();
    //         }
    //     }
    //     return null;
    // }
    //
    // public boolean deleteMy() {
    //     String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
    //     int i = userMapper.deleteById(username);
    //
    //     return i == 1;
    // }
}
