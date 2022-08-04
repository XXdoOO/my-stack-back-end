package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.mapper.CommentsMapper;
import com.xx.pojo.Comments;
import com.xx.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class CommentsService {

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private HttpSession session;

    public List<Comments> getCommentsList(int id, int startIndex, int pageSize) {
        QueryWrapper<Comments> wrapper = new QueryWrapper<>();
        return commentsMapper.selectList(wrapper.
                select("blog_id", "parent_comments", "sender_username", "acceptor_username", "content", "time").
                eq("blog_id", id).last("limit " + startIndex + ", " + pageSize));
    }

    public int postComments(Comments comments) {
        Comments comments1 = new Comments();

        comments1.setParentComments(comments.getParentComments());
        comments1.setSenderUsername(comments.getSenderUsername());
        comments1.setContent(comments.getContent());

        return commentsMapper.insert(comments1);
    }

    public int deleteMyComments(int id) {
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
        UpdateWrapper<Comments> wrapper = new UpdateWrapper<>();

        return commentsMapper.update(null, wrapper.
                set("logic_delete", 1).
                eq("sender_username", username).
                eq("blog_id", id));
    }
}
