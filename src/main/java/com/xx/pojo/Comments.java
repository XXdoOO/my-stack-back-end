package com.xx.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@TableName
public class Comments {
    private Integer id;
    private Integer parentComments;
    private String senderUsername;
    private String content;
    private Timestamp time;

    @TableLogic
    private boolean logicDelete;
}
