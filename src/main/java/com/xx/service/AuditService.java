package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.mapper.BlogMapper;
import com.xx.pojo.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Service
public class AuditService {
    @Autowired
    private BlogMapper blogMapper;

    // 审核发布的博客
    public int auditBlog(int id, boolean isPass) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        return blogMapper.update(null, wrapper.
                eq("id", id).
                eq("logic_post", null).
                eq("logic_delete", 0).
                set("logic_post", isPass));
    }

    // 删除审核中、审核通过、审核不通过的博客
    public int deleteBlog(int id) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        return blogMapper.update(null, wrapper.
                eq("id", id).
                eq("logic_delete", 0).
                set("logic_delete", 1));
    }
}
