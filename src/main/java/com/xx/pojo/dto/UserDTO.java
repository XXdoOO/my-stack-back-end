package com.xx.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class UserDTO extends BaseDTO {
    @Email(message = "邮箱无效")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Length(min = 4, max = 4)
    @NotBlank(message = "验证码不能为空")
    private String code;
    private String nickname;
    private MultipartFile avatar;
    private Boolean isAdmin;
    private Long userId;
    private Long minutes;
    private String reason;
}
