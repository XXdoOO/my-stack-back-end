package com.xx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.pojo.dto.CommentDTO;
import com.xx.pojo.entity.Comment;
import com.xx.pojo.vo.CommentVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    List<CommentVO> getCommentList(CommentDTO dto);
    List<CommentVO> getCommentList2(CommentDTO dto);
}
