package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mapper.CommentMapper;
import com.xx.mapper.RecordMapper;
import com.xx.mapper.UserMapper;
import com.xx.pojo.dto.CommentDTO;
import com.xx.pojo.entity.Comment;
import com.xx.pojo.entity.Record;
import com.xx.pojo.vo.CommentVO;
import com.xx.util.AddressUtils;
import com.xx.util.IpUtils;
import com.xx.util.UserInfoUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    private HttpServletRequest request;

    public List<CommentVO> getCommentsList(CommentDTO dto) {
        dto.setUserId(UserInfoUtils.getUser().getId());

        if (dto.getParent() == null) {
            dto.setParent(0L);
        }
        System.out.println(dto);
        return commentMapper.getCommentList(dto);
    }

    public List<CommentVO> getCommentsList2(CommentDTO dto) {
        return commentMapper.getCommentList2(dto);
    }

    public CommentVO postComments(CommentDTO dto) {
        Comment comment = new Comment();
        comment.setBlogId(dto.getBlogId());
        comment.setContent(dto.getContent());
        comment.setParent(dto.getParent());
        comment.setIp(IpUtils.getIpAddr(request));
        comment.setIpTerritory(AddressUtils.getRealAddressByIP(comment.getIp()));
        comment.setReceiveId(dto.getReceiveId());

        commentMapper.insert(comment);

        CommentVO commentVo = new CommentVO();

        BeanUtils.copyProperties(comment, commentVo);
        commentVo.setSender(userMapper.selectById(UserInfoUtils.getUser().getId()));
        commentVo.setReceiver(userMapper.selectById(dto.getReceiveId()));
        commentVo.setUp(0L);
        commentVo.setDown(0L);
        return commentVo;
    }

    public int deleteComment(Long id) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        return commentMapper.delete(wrapper.
                eq("id", id).
                and(i -> i.eq("create_by", UserInfoUtils.getUser().getId()).or().eq("parent", id)));
    }

    public boolean handleComment(long commentId, int type) {
        if (type > 1 || type < 0) {
            return false;
        }

        Long userId = UserInfoUtils.getUser().getId();

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
