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
import org.springframework.beans.BeanUtils;
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
    private UserService userService;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    public List<CommentVo> getCommentsList(CommentDTO dto) {
        dto.setUserId(userService.getCurrentUser().getId());

        if (dto.getParent() == null) {
            dto.setParent(0L);
        }
        System.out.println(dto);
        return commentMapper.getCommentList(dto);
    }

    public List<CommentVo> getCommentsList2(CommentDTO dto) {
        return commentMapper.getCommentList2(dto);
    }

    public CommentVo postComments(CommentDTO dto) {
        Comment comment = new Comment();
        comment.setBlogId(dto.getBlogId());
        comment.setContent(dto.getContent());
        comment.setParent(dto.getParent());
        comment.setIp(IpUtils.getIpAddr(request));
        comment.setIpTerritory(AddressUtils.getRealAddressByIP(comment.getIp()));
        comment.setCreateBy(userService.getCurrentUser().getId());
        comment.setReceiveId(dto.getReceiveId());

        commentMapper.insert(comment);

        CommentVo commentVo = new CommentVo();

        BeanUtils.copyProperties(comment, commentVo);
        return commentVo;
    }

    public int deleteComment(Long id) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        return commentMapper.delete(wrapper.
                eq("id", id).
                and(i -> i.eq("create_by", userService.getCurrentUser().getId()).or().eq("parent", id)));
    }

    public boolean handleComment(long commentId, int type) {
        if (type > 1 || type < 0) {
            return false;
        }

        Long userId = userService.getCurrentUser().getId();

        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        Record record1 = recordMapper.selectOne(wrapper.
                eq("create_by", userId).
                eq("comment_id", commentId).
                eq("type", type));

        if (record1 == null) {
            Record record = new Record();

            record.setCommentId(commentId);
            record.setType(type);
            record.setCreateBy(userId);

            return recordMapper.insert(record) == 1;
        } else {
            return recordMapper.deleteById(record1.getId()) == 1;
        }
    }
}
