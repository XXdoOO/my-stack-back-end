package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "comment")
public class Comment extends BaseEntity {
    @TableField(value = "blog_id")
    private Long blogId;

    @TableField(value = "parent")
    private Long parent;

    @TableField(value = "sender_id")
    private Long senderId;

    @TableField(value = "receive_id")
    private Long receiveId;

    @TableField(value = "content")
    private String content;

    @TableField(value = "up")
    private Long up;

    @TableField(value = "down")
    private Long down;

}
