package com.xx.pojo.dto;

import lombok.Data;

@Data
public class DictDTO extends PageDTO{
    private String dictName;
    private String label;
    private String value;
}
