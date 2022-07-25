package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName
public class User {
    @TableId
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private Boolean identity;
}
