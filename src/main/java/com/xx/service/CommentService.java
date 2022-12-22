package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mapper.CommentMapper;
import com.xx.mapper.UserMapper;
import com.xx.pojo.dto.CommentDTO;
import com.xx.pojo.entity.Comment;
import com.xx.pojo.entity.User;
import com.xx.pojo.vo.CommentVo;
import com.xx.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    public List<CommentVo> getCommentsList(CommentDTO dto) {
        User user = (User) session.getAttribute("USER_SESSION");

        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }

        dto.setUserId(userId);

        if (dto.getParent() == null) {
            dto.setParent(0L);
        }

        return commentMapper.getCommentList(dto);
    }

    public CommentVo postComments(CommentDTO dto) {
        User user = (User) session.getAttribute("USER_SESSION");

        Comment comment = new Comment();
        comment.setBlogId(dto.getBlogId());
        comment.setContent(dto.getContent());
        comment.setParent(dto.getParent());
        comment.setSenderId(user.getId());
        comment.setReceiveId(dto.getReceiveId());

        commentMapper.insert(comment);

        CommentVo commentVo = new CommentVo();

        commentVo.setId(comment.getId());
        commentVo.setBlogId(comment.getBlogId());
        commentVo.setContent(comment.getContent());
        commentVo.setParent(comment.getParent());
        commentVo.setSenderId(comment.getSenderId());
        commentVo.setReceiveId(comment.getReceiveId());
        commentVo.setIp(IpUtil.getIpAddr(request));
        commentVo.setChildrenCount(0L);
        commentVo.setUp(0L);
        commentVo.setDown(0L);
        commentVo.setIsUp(false);
        commentVo.setIsDown(false);
        commentVo.setCreateTime(comment.getCreateTime());
        return commentVo;
    }

    public boolean deleteComment(Long id) {
        User user = (User) session.getAttribute("USER_SESSION");

        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        return commentMapper.delete(wrapper.eq("id", id).eq("sender_id", user.getId())) == 1;
    }
}
