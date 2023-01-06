package com.xx.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xx.pojo.entity.Disable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    private Long id;
    private String email;
    private String nickname;
    private String avatar;
    private String ip;
    private String ipTerritory;
    private Date createTime;
    private Boolean disable;
    private Boolean admin;
    private Disable disableInfo;
    private Long passCount;
    private Long noPassCount;
    private Long auditingCount;
    private Long history;
    private Long up;
    private Long down;
    private Long star;
}