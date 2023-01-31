package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private HttpServletRequest request;

    public List<BlogViewVO> getBlogList(BlogDTO dto) {
        dto.setUserId(userService.getCurrentUser().getId());

        return blogMapper.getBlogList(dto);
    }

    public List<BlogViewVO> getBlogList2(BlogDTO dto) {
        System.out.println(Arrays.toString(dto.getCreateTime()));
        System.out.println(dto.getPageNum());
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

    public BlogVO getBlogDetails(long id) {
        User user = userService.getCurrentUser();

        Long userId = null;
        if (user.getId() != null) {
            userId = user.getId();
            Record record = new Record();
            record.setBlogId(id);
            record.setType(3);
            record.setCreateBy(userId);

            recordMapper.insert(record);
        } else {
            blogMapper.update(null, new UpdateWrapper<Blog>().
                    setSql("view = view + 1").eq("id", id));
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

    public void postBlog(BlogDTO dto) {
        User user = userService.getCurrentUser();

        Blog blog = new Blog();

        blog.setTitle(dto.getTitle());
        blog.setDescription(dto.getDescription());
        blog.setContent(dto.getContent());
        blog.setCreateBy(user.getId());
        blog.setIp(IpUtils.getIpAddr(request));
        blog.setIpTerritory(AddressUtils.getRealAddressByIP(blog.getIp()));

        blogMapper.insert(blog);

        if (SaveFile.saveFile(dto.getCoverImg(), blog.getId())) {
            UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
            blogMapper.update(null, wrapper.
                    set("cover", "/cover/" + blog.getId() + ".jpg").
                    eq("id", blog.getId()).
                    eq("create_by", user.getId()));
        }
    }

    public void updateBlog(BlogDTO dto) {
        User user = userService.getCurrentUser();

        Blog blog = new Blog();

        blog.setTitle(dto.getTitle());
        blog.setDescription(dto.getDescription());
        blog.setContent(dto.getContent());

        if (SaveFile.saveFile(dto.getCoverImg(), dto.getId())) {
            blog.setCover("/cover/" + dto.getId() + ".jpg");
        }
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        blogMapper.update(blog, wrapper.
                eq("id", dto.getId()).
                eq("create_by", user.getId()));
    }

    public boolean deleteBlog(long blogId) {
        User user = userService.getCurrentUser();

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        recordMapper.delete(queryWrapper.eq("blog_id", blogId));

        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        return blogMapper.delete(wrapper.eq("id", blogId).eq("create_by", user.getId())) == 1;
    }
}
