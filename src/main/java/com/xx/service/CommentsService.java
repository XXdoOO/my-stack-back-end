package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mapper.CommentsMapper;
import com.xx.pojo.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService {

    @Autowired
    private CommentsMapper commentsMapper;

    public List<Comments> getCommentsList(int id) {
        QueryWrapper<Comments> wrapper = new QueryWrapper<>();
        return commentsMapper.selectList(wrapper.select("id", "parent_comments", "sender_username", "acceptor_username", "content", "time").
                eq("logic_delete", 0).
                eq("id", id));
    }
}
