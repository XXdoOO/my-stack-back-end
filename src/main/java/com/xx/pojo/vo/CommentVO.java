package com.xx.pojo.vo;

import com.xx.pojo.entity.Comment;
import com.xx.pojo.entity.User;
import lombok.Data;

@Data
public class CommentVO extends Comment {
    private User sender;
    private User receiver;
    private Long up;
    private Long down;
    private Boolean isUp;
    private Boolean isDown;
    private Long childrenCount;
}
