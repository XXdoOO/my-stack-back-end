package com.xx.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xx.pojo.entity.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentVo {
    private Long id;
    private Long blogId;
    private Long parent;
    private Long senderId;
    private String senderNickname;
    private String senderAvatar;
    private Long receiveId;
    private String receiveNickname;
    private String content;
    private String ip;
    private String ipTerritory;
    private Long up;
    private Long down;
    private Boolean isUp;
    private Boolean isDown;
    private Long childrenCount;
    private Date createTime;
}
