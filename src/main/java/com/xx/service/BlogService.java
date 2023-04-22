package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.config.SystemConfig;
import com.xx.mapper.*;
import com.xx.pojo.dto.BlogDTO;
import com.xx.pojo.entity.Blog;
import com.xx.pojo.entity.Record;
import com.xx.pojo.entity.User;
import com.xx.pojo.vo.BlogViewVO;
import com.xx.pojo.vo.BlogVO;
import com.xx.util.AddressUtils;
import com.xx.util.IpUtils;
import com.xx.util.SaveFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
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

    @Autowired
    private HttpServletRequest request;

    public List<BlogViewVO> getBlogList(BlogDTO dto) {
        dto.setUserId(userService.getCurrentUser().getId());

        return blogMapper.getBlogList(dto);
    }

    public List<BlogViewVO> getBlogList2(BlogDTO dto) {
        return blogMapper.getBlogList2(dto);
    }

    public boolean auditBlog(long blogId, int status) {
        if (status != 0 && status != 1 && status != 2) {
            return false;
        }
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        return blogMapper.update(null, wrapper.set("status", status).
                eq("id", blogId)) == 1;
    }

    public boolean enableBlog(long blogId) {
        return blogMapper.update(null, new LambdaUpdateWrapper<Blog>().setSql("is_enabled = !is_enabled").
                eq(Blog::getId, blogId)) == 1;
    }

    public BlogVO getBlogDetails(long id) {
        User user = userService.getCurrentUser();

        blogMapper.update(null, new LambdaUpdateWrapper<Blog>().
                setSql("view = view + 1").eq(Blog::getId, id));

        Long userId = null;
        if (user.getId() != null) {
            userId = user.getId();

            if (!recordMapper.exists(new LambdaQueryWrapper<Record>().
                    eq(Record::getBlogId, id).
                    eq(Record::getType, 3).
                    eq(Record::getCreateBy, userId))) {

                Record record = new Record();
                record.setBlogId(id);
                record.setType(3);
                record.setCreateBy(userId);

                recordMapper.insert(record);
            }
        }
        return blogMapper.getBlogDetails(id, userId);
    }

    public BlogVO getBlogDetails2(long id) {
        return blogMapper.getBlogDetails2(id);
    }

    public boolean handleBlog(long blogId, int type) {
        if (type > 2 || type < 0) {
            return false;
        }

        Long userId = userService.getCurrentUser().getId();

        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        Record record1 = recordMapper.selectOne(wrapper.
                eq("create_by", userId).
                eq("blog_id", blogId).
                eq("type", type));

        if (record1 == null) {
            Record record = new Record();

            record.setBlogId(blogId);
            record.setType(type);
            record.setCreateBy(userId);

            return recordMapper.insert(record) == 1;
        } else {
            return recordMapper.deleteById(record1.getId()) == 1;
        }
    }

    public Long postBlog(BlogDTO dto) {
        User user = userService.getCurrentUser();

        Blog blog = new Blog();

        blog.setTitle(dto.getTitle());
        blog.setDescription(dto.getDescription());
        blog.setContent(dto.getContent());
        blog.setCreateBy(user.getId());
        blog.setIp(IpUtils.getIpAddr(request));
        blog.setIpTerritory(AddressUtils.getRealAddressByIP(blog.getIp()));

        if (blogMapper.insert(blog) == 1) {
            File workDir = new File(SystemConfig.getLocalPath() + user.getId() + "/" + blog.getId());

            if (workDir.mkdir()) {
                String cover = SaveFile.saveCover(dto.getCoverImg(), blog.getId());
                if (cover != null) {
                    blogMapper.update(null, new LambdaUpdateWrapper<Blog>().
                            set(Blog::getCover, cover).
                            eq(Blog::getId, blog.getId()));
                }
                return blog.getId();
            }
        }
        return null;
    }

    public void updateBlog(BlogDTO dto) {
        User user = userService.getCurrentUser();

        Blog blog = new Blog();

        blog.setTitle(dto.getTitle());
        blog.setDescription(dto.getDescription());
        blog.setContent(dto.getContent());
        blog.setStatus(0);

        String cover = SaveFile.saveCover(dto.getCoverImg(), dto.getId());
        if (cover != null) {
            blog.setCover(cover);
        }
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        blogMapper.update(blog, wrapper.
                eq("id", dto.getId()).
                eq("create_by", user.getId()));
    }

    public boolean deleteBlog(long blogId) {
        User user = userService.getCurrentUser();

        recordMapper.delete(new LambdaQueryWrapper<Record>().
                eq(Record::getBlogId, blogId).
                eq(Record::getCreateBy, user.getId()));

        if (blogMapper.delete(new LambdaQueryWrapper<Blog>().
                eq(Blog::getId, blogId).
                eq(Blog::getCreateBy, user.getId())) == 1) {
            FileSystemUtils.deleteRecursively(new File("/" + user.getId() + "/" + blogId));

            return true;
        }
        return false;
    }
}
