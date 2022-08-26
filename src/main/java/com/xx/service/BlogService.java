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
    private BlogStarMapper starMapper;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private CategoryService categoryService;

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

        // 默认获取最多五条热评，最多十条新评
        List<Comments> comments = commentsService.getCommentsList(blog.getId(), "up", 0, 5);

        comments.addAll(commentsService.getCommentsList(blog.getId(), "post_time", 0, 10));
        blog.setCommentsList(comments);

        return blog;
    }

    public boolean postBlog(Blog blog) {
        User user = (User) session.getAttribute("USER_SESSION");
        Blog blog1 = new Blog();
        blog1.setTitle(blog.getTitle());
        blog1.setContent(blog.getContent());
        blog1.setAuthorUsername(user.getUsername());

        int insert = blogMapper.insert(blog1);

        boolean result = categoryService.addCategoryBlog(blog1.getId(), blog.getCategories());

        return insert > 0 && result;
    }

    public boolean deleteMyBlog(int id) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        int delete = blogMapper.delete(wrapper.
                eq("id", id).
                eq("author_username", username));

        int i = categoryService.deleteCategoryBlog(id);

        return i > 0 && delete > 0;
    }

    public int updateMyBlog(Blog blog) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        Blog blog1 = new Blog();
        blog1.setTitle(blog.getTitle());
        blog1.setContent(blog.getContent());
        blog1.setStatus(null);

        categoryService.updateCategoryBlog(blog.getId(), blog.getCategories());

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
        QueryWrapper<BlogStar> queryWrapper = new QueryWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        Long count = starMapper.selectCount(queryWrapper.
                select("status").
                eq("username", username).
                eq("blog_id", id));

        if (count > 0) {
            BlogStar star = new BlogStar(id, username, null);
            int update = blogMapper.update(null, wrapper.setSql("star = star -1").eq("id", id));
            int insert = starMapper.insert(star);
            return update == 1 && insert == 1;
        }

        UpdateWrapper<BlogStar> wrapper2 = new UpdateWrapper<>();
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

        return blogMapper.getMyStarList(map);
    }

    public List<BlogView> getBlogListByCategories(List<String> categories, String orderBy, int startIndex, int pageSize) {
        QueryWrapper<BlogView> wrapper = new QueryWrapper<>();

        Map<String, Object> map = new HashMap<>();
        map.put("categories", categories);
        map.put("orderBy", orderBy);
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);

        return blogViewMapper.getBlogListByCategories(map);
    }
}
