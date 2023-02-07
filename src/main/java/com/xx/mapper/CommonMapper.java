package com.xx.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonMapper {
    Integer deleteItem(@Param("table") String table, @Param("id") long id);

    Integer enableItem(@Param("table") String table, @Param("id") long id);
}