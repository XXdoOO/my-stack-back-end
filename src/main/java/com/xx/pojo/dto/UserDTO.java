package com.xx.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class UserDTO extends BaseDTO {
    private String email;

    private String password;

    private String code;

    private String nickname;

    private MultipartFile avatar;

    private Boolean isAdmin;

    private Long userId;

    private Long minutes;

    private String reason;
}
