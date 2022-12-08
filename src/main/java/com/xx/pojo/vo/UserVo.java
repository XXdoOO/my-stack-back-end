package com.xx.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.xx.pojo.Disable;
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
    private boolean disable;
    private boolean admin;
    private Disable disableInfo;
    private long passCount;
    private Long noPassCount;
    private Long auditingCount;
    private long upCount;
    private long downCount;
    private Long starCount;
}