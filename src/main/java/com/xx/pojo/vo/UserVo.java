package com.xx.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xx.pojo.entity.Disable;
import com.xx.pojo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo extends User {
    private Disable disableInfo;
    private Long passCount;
    private Long noPassCount;
    private Long auditingCount;
    private Long history;
    private Long up;
    private Long down;
    private Long star;
}