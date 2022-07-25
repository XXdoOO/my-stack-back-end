package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mapper.BlogMapper;
import com.xx.mapper.CommentsMapper;
import com.xx.mapper.UserMapper;
import com.xx.pojo.Blog;
import com.xx.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private HttpSession session;

    public int pushBlog(String title, String content) {
        User user = (User) session.getAttribute("USER_SESSION");
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setAuthorUsername(user.getUsername());

        return blogMapper.insert(blog);
    }

    public List<Blog> getBlogList(int startIndex, int pageSize) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        return blogMapper.selectList(wrapper.last("limit " + startIndex + ", " + pageSize));
    }
}
