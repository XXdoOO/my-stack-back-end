package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.mapper.BlogMapper;
import com.xx.mapper.BlogViewMapper;
import com.xx.pojo.Blog;
import com.xx.pojo.BlogView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Service
public class AuditService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogViewMapper blogViewMapper;

    public int auditBlog(int id, boolean status) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        return blogMapper.update(null, wrapper.
                eq("id", id).
                isNull("status").
                set("status", status));
    }

    public List<BlogView> getPostBlogList(Integer status, int startIndex, int pageSize) {
        QueryWrapper<BlogView> wrapper = new QueryWrapper<>();

        if (status == null) {
            return blogViewMapper.selectList(wrapper.isNull("status").
                    last("limit " + startIndex + ", " + pageSize));
        }

        return blogViewMapper.selectList(wrapper.eq("status", status).
                last("limit " + startIndex + ", " + pageSize));
    }
}
