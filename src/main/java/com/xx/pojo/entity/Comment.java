package com.xx.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class Comment extends BaseEntity {
    private Long blogId;
    private Long parent;
    private Long senderId;
    private Long receiveId;
    private String content;
    private String ip;
    private String ipTerritory;
}
