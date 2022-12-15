package com.xx.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xx.pojo.entity.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo {
    private Long id;
    private Long blogId;
    private Long parent;
    private Long senderId;
    private Long receiveId;
    private String content;
    private Long upCount;
    private Long downCount;
    private Boolean isUp = false;
    private Boolean isDown = false;
}
