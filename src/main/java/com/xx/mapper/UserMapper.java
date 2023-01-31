package com.xx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.pojo.entity.User;
import com.xx.pojo.vo.UserVO;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper extends BaseMapper<User> {
    UserVO getUserInfo(Long createBy, Long userId);
}
