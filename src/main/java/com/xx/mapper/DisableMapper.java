package com.xx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.pojo.Disable;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public interface DisableMapper extends BaseMapper<Disable> {
    void setUserDisableTime(Map<String, Object> map);
}
