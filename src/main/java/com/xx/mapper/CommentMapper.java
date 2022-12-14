package com.xx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.pojo.entity.Comment;
import com.xx.pojo.vo.CommentVo;
import jdk.jfr.Registered;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    List<CommentVo> getCommentList(long blogId, Long userId, String orderBy);
}
