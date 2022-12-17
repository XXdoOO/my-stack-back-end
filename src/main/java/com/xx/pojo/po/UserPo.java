package com.xx.pojo.po;

import com.xx.pojo.entity.Disable;
import lombok.Data;

@Data
public class UserPo {
    private String nickname;
    private Long passCount;
    private Long noPassCount;
    private Long auditingCount;
    private Long upCount;
    private Long downCount;
    private Long starCount;
}
