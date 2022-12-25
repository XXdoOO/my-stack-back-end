package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xx.mapper.*;
import com.xx.pojo.dto.BlogDTO;
import com.xx.pojo.entity.Blog;
import com.xx.pojo.entity.Record;
import com.xx.pojo.entity.User;
import com.xx.pojo.vo.BlogViewVo;
import com.xx.pojo.vo.BlogVo;
import com.xx.util.MyResponse;
import com.xx.util.SaveFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

@Transactional
@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpSession session;

    @Value("${images.local-path}")
    private String locPath;

    public List<BlogViewVo> getBlogList(BlogDTO dto) {
        User user = (User) session.getAttribute("USER_SESSION");

        if (user != null) {
            dto.setUserId(user.getId());
        } else {
            dto.setUserId(null);
        }

        System.out.println(dto);
        return blogMapper.getBlogList(dto);
    }

    public BlogVo getBlogDetails(long id) {
        User user = (User) session.getAttribute("USER_SESSION");

        Long userId = null;
        if (user != null) {
            userId = user.getId();

            Record record = new Record();
            record.setBlogId(id);
            record.setType(3);
            record.setUserId(userId);

            recordMapper.insert(record);
        }

        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        blogMapper.update(null, wrapper.setSql("views = views + 1").eq("id", id));

        return blogMapper.getBlogDetails(id, userId);
    }

    public boolean handleBlog(long blogId, int type) {
        if (type > 2 || type < 0) {
            return false;
        }

        User user = (User) session.getAttribute("USER_SESSION");

        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }

        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        Record record1 = recordMapper.selectOne(wrapper.
                eq("user_id", userId).
                eq("blog_id", blogId).
                eq("type", type));

        if (record1 == null) {
            Record record = new Record();

            record.setBlogId(blogId);
            record.setType(type);
            record.setUserId(userId);

            return recordMapper.insert(record) == 1;
        } else {
            return recordMapper.deleteById(record1.getId()) == 1;
        }
    }

    public void postBlog(BlogDTO dto) {
        User user = (User) session.getAttribute("USER_SESSION");

        Blog blog = new Blog();

        blog.setTitle(dto.getTitle());
        blog.setDescription(dto.getDescription());
        blog.setContent(dto.getContent());
        blog.setAuthorId(user.getId());
    }
}
