package com.xx.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class DictDTO extends BaseDTO {
    @NotBlank
    private String dictName;
    private String label;
    private String value;
}
