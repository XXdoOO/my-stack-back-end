package com.xx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.pojo.dto.BlogDTO;
import com.xx.pojo.entity.Blog;
import com.xx.pojo.vo.BlogViewVO;
import com.xx.pojo.vo.BlogVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogMapper extends BaseMapper<Blog> {
    List<BlogViewVO> getBlogList(BlogDTO dto);

    List<BlogViewVO> getBlogList2(BlogDTO dto);

    BlogVO getBlogDetails(long id, Long userId);

    BlogVO getBlogDetails2(long id);
}
