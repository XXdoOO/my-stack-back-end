package com.xx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper extends BaseMapper<User> {
    long getDeletedUserCount();
}
