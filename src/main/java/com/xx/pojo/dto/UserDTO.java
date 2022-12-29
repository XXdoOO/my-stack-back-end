package com.xx.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO extends PageDTO {
    private String email;

    private String password;

    private String code;

    private String nickname;

    private MultipartFile avatar;

    private Boolean isAdmin;

    private Boolean isDisable;
}
