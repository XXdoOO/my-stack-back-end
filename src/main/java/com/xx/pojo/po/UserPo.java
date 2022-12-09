package com.xx.pojo.po;

import com.xx.pojo.entity.Disable;
import lombok.Data;

@Data
public class UserPo {
    private String nickname;
    private Disable disableInfo;
    private long passCount;
    private long noPassCount;
    private long auditingCount;
    private long upCount;
    private long downCount;
    private long starCount;
}
