package com.xx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xx.pojo.entity.Blog;
import com.xx.pojo.po.BlogViewPo;
import com.xx.pojo.vo.BlogViewVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogMapper extends BaseMapper<Blog> {
    List<BlogViewVo> getBlogListByKeywords(Long userId, String keywords);

    Blog getBlogDetails(long id);
}
