package com.xx.pojo.entity;

import lombok.Data;

@Data
public class DictData extends BaseEntity {
    private String dictName;
    private String label;
    private String value;
}
