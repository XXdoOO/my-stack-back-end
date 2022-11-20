package com.xx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.pojo.BlogView;
import com.xx.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper extends BaseMapper<User> {
    long getDeletedUserCount();

    List<User> getUserList(@Param("user") User user, @Param(
            "orderBy") String orderBy,
                               @Param("isAsc") Boolean isAsc, @Param("startIndex") long startIndex,
                               @Param("pageSize") long pageSize);

    long getUserListCount(@Param("user") User user, @Param(
            "orderBy") String orderBy,
                          @Param("isAsc") Boolean isAsc, @Param("startIndex") long startIndex,
                          @Param("pageSize") long pageSize);
}
