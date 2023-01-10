package com.xx.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DictDTO extends PageDTO{
    @NotBlank
    private String dictName;
    private String label;
    private String value;
}
