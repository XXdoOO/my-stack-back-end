package com.xx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.pojo.Blog;
import com.xx.pojo.BlogView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BlogMapper extends BaseMapper<Blog> {
    List<BlogView> getMyStarList(Map<String, Object> map);

    List<BlogView> getMyUpList(Map<String, Object> map);

    List<BlogView> getMyDownList(Map<String, Object> map);

    long deletedBlogCount();

    List<BlogView> getBlogList(@Param("blogView") BlogView blogView, @Param(
            "orderBy") String orderBy,
                               @Param("isAsc") Boolean isAsc, @Param("startIndex") long startIndex,
                               @Param("pageSize") long pageSize);

    long getBlogListCount(@Param("blogView") BlogView blogView, @Param(
            "orderBy") String orderBy,
                          @Param("isAsc") Boolean isAsc, @Param("startIndex") long startIndex,
                          @Param("pageSize") long pageSize);
}
