package com.xx.pojo.vo;

import com.xx.pojo.entity.Disable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    private Long id;
    private String email;
    private String nickname;
    private String avatar;
    private Timestamp createTime;
    private Boolean isDisable;
    private Boolean isAdmin;
    private Disable disableInfo;
    private Long passCount;
    private Long noPassCount;
    private Long auditingCount;
    private Long upCount;
    private Long downCount;
    private Long starCount;
}