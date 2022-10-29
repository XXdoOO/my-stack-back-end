package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.mapper.BlogMapper;
import com.xx.mapper.BlogViewMapper;
import com.xx.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

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
    private HttpSession session;

    public int auditBlog(int id, boolean status) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        return blogMapper.update(null, wrapper.
                eq("id", id).
                isNull("status").
                set("status", status));
    }

    public List<BlogView> getPostBlogList(Integer status, int startIndex, int pageSize) {
        QueryWrapper<BlogView> wrapper = new QueryWrapper<>();
        List<BlogView> blogViews;
        if (status == null) {
            blogViews = blogViewMapper.selectList(wrapper.isNull("status").
                    last("limit " + startIndex + ", " + pageSize));
        } else {
            blogViews = blogViewMapper.selectList(wrapper.eq("status", status).
                    last("limit " + startIndex + ", " + pageSize));
        }

        blogService.setOtherInfo(blogViews);

        return blogViews;
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

    public Blog getNearAuditBlogDetails(int id, boolean isNext) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();

        Blog blog;
        if (isNext) {
            blog = blogMapper.selectOne(wrapper.
                    isNull("status").
                    gt("id", id).
                    last("limit 1"));
        } else {
            blog = blogMapper.selectOne(wrapper.
                    isNull("status").
                    lt("id", id).
                    last("limit 1"));
        }

        if (blog != null) {
            String authorUsername = blog.getAuthorUsername();

            blog.setAuthorInfo(userService.getUserInfo(authorUsername));
        }

        return blog;
    }
}
