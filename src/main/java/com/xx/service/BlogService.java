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
    private BlogUpMapper upMapper;

    @Autowired
    private BlogDownMapper downMapper;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpSession session;

    public List<BlogView> getBlogListByKeywords(String keywords, String orderBy, int startIndex, int pageSize) {
        QueryWrapper<BlogView> wrapper = new QueryWrapper<>();

        List<BlogView> blogViews = blogViewMapper.selectList(wrapper.
                eq("status", 1).
                and(i -> i.like("title", keywords).or().like("content", keywords)).
                orderByAsc(orderBy).
                last("limit " + startIndex + ", " + pageSize));

        for (BlogView blogView : blogViews) {
            User user = userMapper.selectById(blogView.getAuthorUsername());
            blogView.setAuthorNickname(user.getNickname());
        }

        return blogViews;
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
        int update = blogMapper.update(null, updateWrapper.
                setSql("views = views + 1").
                eq("status", 1).
                eq("id", id));

        if (update != 0) {
            Blog blog = blogMapper.selectOne(wrapper.
                    eq("status", 1).
                    eq("id", id));

            String username = blog.getAuthorUsername();
            blog.setAuthorInfo(userService.getUserInfo(username));

            // 默认获取最多五条热评，最多十条新评
            List<Comments> comments = commentsService.getCommentsList(blog.getId(), "up", 0, 5);

            comments.addAll(commentsService.getCommentsList(blog.getId(), "post_time", 0, 10));
            blog.setCommentsList(comments);

            // categoryService

            return blog;
        }
        return null;

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

    public boolean upBlog(int id) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        QueryWrapper<BlogUp> queryWrapper = new QueryWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        // 查询顶记录是否存在
        Long count = upMapper.selectCount(queryWrapper.
                eq("username", username).
                eq("blog_id", id));

        // 顶记录不存在则添加记录
        if (count == 0) {
            System.out.println("顶记录不存在");
            BlogUp up = new BlogUp(id, username, false);
            int update = blogMapper.update(null, wrapper.setSql("up = up + 1").eq("id", id));
            int insert = upMapper.insert(up);
            return update == 1 && insert == 1;
        }

        // 顶记录存在则逻辑删除该记录
        System.out.println("顶记录存在");
        UpdateWrapper<BlogUp> wrapper2 = new UpdateWrapper<>();
        int update = blogMapper.update(null, wrapper.setSql("up = up - 1").eq("id", id));
        int update1 = upMapper.delete(wrapper2.eq("username", username).eq("blog_id", id));
        return update == 1 && update1 == 1;
    }

    public boolean downBlog(int id) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        QueryWrapper<BlogDown> queryWrapper = new QueryWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        // 查询踩记录是否存在
        Long count = downMapper.selectCount(queryWrapper.
                eq("username", username).
                eq("blog_id", id));

        // 踩记录不存在则添加记录
        if (count == 0) {
            System.out.println("踩记录不存在");
            BlogDown down = new BlogDown(id, username, false);
            int update = blogMapper.update(null, wrapper.setSql("down = down + 1").eq("id", id));
            int insert = downMapper.insert(down);
            return update == 1 && insert == 1;
        }

        // 踩记录存在则逻辑删除该记录
        System.out.println("踩记录存在");
        UpdateWrapper<BlogDown> wrapper2 = new UpdateWrapper<>();
        int update = blogMapper.update(null, wrapper.setSql("down = down - 1").eq("id", id));
        int update1 = downMapper.delete(wrapper2.eq("username", username).eq("blog_id", id));
        return update == 1 && update1 == 1;
    }

    public boolean starBlog(int id) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        QueryWrapper<BlogStar> queryWrapper = new QueryWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        // 查询收藏记录是否存在
        Long count = starMapper.selectCount(queryWrapper.
                eq("username", username).
                eq("blog_id", id));

        // 收藏记录不存在则添加记录
        if (count == 0) {
            System.out.println("收藏记录不存在");
            BlogStar star = new BlogStar(id, username, false);
            int update = blogMapper.update(null, wrapper.setSql("star = star + 1").eq("id", id));
            int insert = starMapper.insert(star);
            return update == 1 && insert == 1;
        }

        // 收藏记录存在则逻辑删除该记录
        System.out.println("收藏记录存在");
        UpdateWrapper<BlogStar> wrapper2 = new UpdateWrapper<>();
        int update = blogMapper.update(null, wrapper.setSql("star = star - 1").eq("id", id));
        int update1 = starMapper.delete(wrapper2.eq("username", username).eq("blog_id",
                id));
        return update == 1 && update1 == 1;
    }

    public List<BlogView> getMyStarList(int startIndex, int pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        map.put("username", username);
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);

        List<BlogView> myStarList = blogMapper.getMyStarList(map);

        for (BlogView blogView : myStarList) {
            User user = userMapper.selectById(blogView.getAuthorUsername());
            blogView.setAuthorNickname(user.getNickname());
        }

        return myStarList;
    }

    public List<BlogView> getMyUpList(int startIndex, int pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        map.put("username", username);
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);

        List<BlogView> myUpList = blogMapper.getMyUpList(map);

        for (BlogView blogView : myUpList) {
            User user = userMapper.selectById(blogView.getAuthorUsername());
            blogView.setAuthorNickname(user.getNickname());
        }

        return myUpList;
    }

    public List<BlogView> getMyDownList(int startIndex, int pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        map.put("username", username);
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);

        List<BlogView> myDownList = blogMapper.getMyDownList(map);

        for (BlogView blogView : myDownList) {
            User user = userMapper.selectById(blogView.getAuthorUsername());
            blogView.setAuthorNickname(user.getNickname());
        }

        return myDownList;
    }

    public List<BlogView> getBlogListByCategories(List<String> categories, String orderBy, int startIndex,
                                                  int pageSize) {
        QueryWrapper<BlogView> wrapper = new QueryWrapper<>();

        Map<String, Object> map = new HashMap<>();
        map.put("categories", categories);
        map.put("orderBy", orderBy);
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);

        return blogViewMapper.getBlogListByCategories(map);
    }
}
