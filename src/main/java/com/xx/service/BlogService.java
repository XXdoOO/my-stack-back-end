package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.mapper.*;
import com.xx.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogViewMapper blogViewMapper;

    @Autowired
    private StarMapper starMapper;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private HttpSession session;

    public List<BlogView> getBlogListByKeywords(String keywords, String orderBy, int startIndex, int pageSize) {
        QueryWrapper<BlogView> wrapper = new QueryWrapper<>();
        return blogViewMapper.selectList(wrapper.
                eq("status", 1).
                and(i -> i.like("title", keywords).or().like("content", keywords)).
                orderByAsc(orderBy).
                last("limit " + startIndex + ", " + pageSize));
    }

    public List<BlogView> getUserBlogList(String username, int startIndex, int pageSize) {
        QueryWrapper<BlogView> wrapper = new QueryWrapper<>();

        return blogViewMapper.selectList(wrapper.
                select("id", "title", "star", "views", "author_username", "post_time").
                eq("author_username", username).
                orderByAsc("post_time").
                last("limit " + startIndex + ", " + pageSize));
    }

    public Blog getBlogDetails(int id) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        UpdateWrapper<Blog> updateWrapper = new UpdateWrapper<>();
        blogMapper.update(null, updateWrapper.
                setSql("views = views + 1").
                eq("status", 1).
                eq("id", id));

        Blog blog = blogMapper.selectOne(wrapper.
                eq("status", 1).
                eq("id", id));

        // 默认获取十条热评，十条新评
        List<Comments> comments = commentsService.getCommentsList(blog.getId(), "up", 0, 10);
        comments.addAll(commentsService.getCommentsList(blog.getId(), "time", 0, 10));
        blog.setCommentsList(comments);

        return blog;
    }

    public int postBlog(Blog blog) {
        User user = (User) session.getAttribute("USER_SESSION");
        Blog blog1 = new Blog();
        blog1.setTitle(blog.getTitle());
        blog1.setContent(blog.getContent());
        blog1.setAuthorUsername(user.getUsername());

        return blogMapper.insert(blog1);
    }

    public int deleteMyBlog(int id) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        return blogMapper.update(null, wrapper.
                eq("id", id).
                eq("author_username", username).
                set("logic_delete", 1));
    }

    public int updateMyBlog(Blog blog) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        Blog blog1 = new Blog();
        blog1.setTitle(blog.getTitle());
        blog1.setContent(blog.getContent());
        blog1.setStatus(null);

        return blogMapper.update(blog1, wrapper.
                eq("id", blog.getId()).
                eq("author_username", username));
    }

//    public boolean upBlog(int id) {
//
//        blogMapper.update()
//    }

    public boolean starBlog(int id) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        QueryWrapper<Star> queryWrapper = new QueryWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        Long count = starMapper.selectCount(queryWrapper.
                select("status").
                eq("username", username).
                eq("blog_id", id));

        if (count > 0) {
            Star star = new Star(id, username);
            int update = blogMapper.update(null, wrapper.setSql("star = star -1").eq("id", id));
            int insert = starMapper.insert(star);
            return update == 1 && insert == 1;
        }

        UpdateWrapper<Star> wrapper2 = new UpdateWrapper<>();
        int update = blogMapper.update(null, wrapper.setSql("star = star + 1").eq("id", id));
        int update1 = starMapper.update(null, wrapper2.set("logic_delete", 1));
        return update == 1 && update1 == 1;
    }

    public List<Blog> getMyStarList(int startIndex, int pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        map.put("username", username);
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);

        return blogMapper.getMyStar(map);
    }
}
