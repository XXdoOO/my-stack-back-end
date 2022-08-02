package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.mapper.BlogMapper;
import com.xx.mapper.CommentsMapper;
import com.xx.mapper.UserMapper;
import com.xx.pojo.Blog;
import com.xx.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private HttpSession session;

    public List<Blog> getBlogListByKeywords(String keywords, int startIndex, int pageSize) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        return blogMapper.selectList(wrapper.
                select("id", "title", "star", "views", "author_username", "time").
                eq("logic_post", 1).
                eq("logic_delete", 0).
                and(i -> i.like("title", keywords).or().like("content", keywords)).
                last("limit " + startIndex + ", " + pageSize));
    }

    public List<Blog> getUserBlogList(String username, Integer flag, int startIndex, int pageSize) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();

        return blogMapper.selectList(wrapper.
                select("id", "title", "star", "views", "author_username", "time").
                eq("logic_post", flag).
                eq("logic_delete", 0).
                eq("author_username", username).
                last("limit " + startIndex + ", " + pageSize));
    }

    public Blog getBlogDetails(int id) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        return blogMapper.selectOne(wrapper.
                select("id", "title", "content", "star", "views", "author_username", "time", "comments_id").
                eq("logic_post", 1).
                eq("logic_delete", 0).
                eq("id", id));
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
                eq("logic_delete", 0).
                set("logic_delete", 1));
    }

    public int updateMyBlog(Blog blog) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        Blog blog1 = new Blog();
        blog1.setTitle(blog.getTitle());
        blog1.setContent(blog.getContent());
        blog1.setStar(blog.getStar());
        blog1.setViews(blog.getViews());
        blog1.setLogicPost(false);

        return blogMapper.update(blog1, wrapper.
                eq("id", blog.getId()).
                eq("author_username", username).
                eq("logic_delete", 0));
    }

    public int starBlog(int id, boolean option) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        return blogMapper.update(null, wrapper.setSql("'star' = 'star' +" + (option ? 1 : -1)).eq("blog_id", id));
    }

    public List<Blog> getMyStar(int startIndex, int pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        map.put("username", username);
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);

        return blogMapper.getMyStar(map);
    }
}
