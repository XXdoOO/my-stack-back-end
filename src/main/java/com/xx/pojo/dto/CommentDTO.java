package com.xx.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
//import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO extends BaseDTO {

    private Long userId;
    @NotNull(message = "博客id不能为空")
    private Long blogId;
    private Long parent;
    private Long receiveId;
    private String content;
    private String nickname;
}
