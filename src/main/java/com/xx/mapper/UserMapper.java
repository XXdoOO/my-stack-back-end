package com.xx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.pojo.entity.User;
import com.xx.pojo.vo.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper extends BaseMapper<User> {
    UserVo getMyInfo(long id);

    UserVo getUserInfo(long id);
}
