package com.xx.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO extends BaseDTO {

    private Long userId;
    @NotNull(message = "博客id不能为空")
    private Long blogId;
    private Long parent;
    private Long receiveId;

    @NotBlank(message = "评论内容不能为空")
    private String content;
    private String nickname;
}
