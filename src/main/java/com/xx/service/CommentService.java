package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mapper.CommentMapper;
import com.xx.mapper.RecordMapper;
import com.xx.mapper.UserMapper;
import com.xx.pojo.dto.CommentDTO;
import com.xx.pojo.entity.Comment;
import com.xx.pojo.entity.Record;
import com.xx.pojo.entity.User;
import com.xx.pojo.vo.CommentVo;
import com.xx.util.AddressUtils;
import com.xx.util.IpUtils;
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
    private RecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    public List<CommentVo> getCommentsList(CommentDTO dto) {
        User user = (User) session.getAttribute("USER_SESSION");

        if (user != null) {
            dto.setUserId(user.getId());
        } else {
            dto.setUserId(null);
        }

        if (dto.getParent() == null) {
            dto.setParent(0L);
        }
        System.out.println(dto);
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
        commentVo.setIp(IpUtils.getIpAddr(request));
        commentVo.setIpTerritory(AddressUtils.getRealAddressByIP(commentVo.getIp()));
        commentVo.setChildrenCount(0L);
        commentVo.setUp(0L);
        commentVo.setDown(0L);
        commentVo.setIsUp(false);
        commentVo.setIsDown(false);
        commentVo.setCreateTime(comment.getCreateTime());
        return commentVo;
    }

    public int deleteComment(Long id) {
        User user = (User) session.getAttribute("USER_SESSION");

        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        return commentMapper.delete(wrapper.
                eq("id", id).
                and(i -> i.eq("sender_id", user.getId()).or().eq("parent", id)));
    }

    public boolean handleComment(long commentId, int type) {
        if (type > 1 || type < 0) {
            return false;
        }

        User user = (User) session.getAttribute("USER_SESSION");

        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }

        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        Record record1 = recordMapper.selectOne(wrapper.
                eq("user_id", userId).
                eq("comment_id", commentId).
                eq("type", type));

        if (record1 == null) {
            Record record = new Record();

            record.setCommentId(commentId);
            record.setType(type);
            record.setUserId(userId);

            return recordMapper.insert(record) == 1;
        } else {
            return recordMapper.deleteById(record1.getId()) == 1;
        }
    }
}
