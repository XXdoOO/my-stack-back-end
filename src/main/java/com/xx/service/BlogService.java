package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xx.mapper.*;
import com.xx.pojo.dto.BlogDTO;
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

    // public Map<String, Object> getBlogList(BlogView blogView, String orderBy, Boolean isAsc, long startIndex,
    //                                        long pageSize) {
    //
    //     List<BlogView> blogViews = blogMapper.getBlogList(blogView, orderBy, isAsc, startIndex, pageSize);
    //
    //     for (BlogView view : blogViews) {
    //         view.setAuthorNickname(userMapper.selectById(view.getAuthorId()).getNickname());
    //     }
    //
    //     long total = blogMapper.getBlogListCount(blogView, "post_time", isAsc, startIndex, pageSize);
    //
    //     return new HashMap<String, Object>() {{
    //         put("total", total);
    //         put("list", blogViews);
    //     }};
    // }
    //
    // public Map<String, Object> getUserPostBlogList(String username, long startIndex, long pageSize) {
    //     QueryWrapper<BlogView> wrapper = new QueryWrapper<>();
    //
    //     Long total = blogViewMapper.selectCount(wrapper.
    //             eq("author_username", username).
    //             eq("status", 1).
    //             orderByAsc("post_time"));
    //
    //     List<BlogView> blogViews = blogViewMapper.selectList(wrapper.
    //             last("limit " + startIndex + ", " + pageSize));
    //
    //     setOtherInfo(blogViews);
    //
    //     return new HashMap<String, Object>() {{
    //         put("total", total);
    //         put("list", blogViews);
    //     }};
    // }
    //
    // public Map<String, Object> getUserUpBlogList(String username, long startIndex, long pageSize) {
    //     QueryWrapper<BlogUp> upWrapper = new QueryWrapper<>();
    //
    //     Long total = upMapper.selectCount(upWrapper.eq("username", username));
    //     List<BlogUp> blogUps = upMapper.selectList(upWrapper.
    //             last("limit " + startIndex + ", " + pageSize));
    //     List<BlogView> blogViews = new ArrayList<>();
    //
    //     for (BlogUp blogUp : blogUps) {
    //         BlogView blogView = blogViewMapper.selectById(blogUp.getBlogId());
    //         blogViews.add(blogView);
    //     }
    //
    //     setOtherInfo(blogViews);
    //
    //     return new HashMap<String, Object>() {{
    //         put("total", total);
    //         put("list", blogViews);
    //     }};
    // }
    //
    // public Map<String, Object> getUserDownBlogList(String username, long startIndex, long pageSize) {
    //     QueryWrapper<BlogDown> downWrapper = new QueryWrapper<>();
    //
    //     Long total = downMapper.selectCount(downWrapper.eq("username", username));
    //     List<BlogDown> blogDowns =
    //             downMapper.selectList(downWrapper.
    //                     last("limit " + startIndex + ", " + pageSize));
    //     List<BlogView> blogViews = new ArrayList<>();
    //
    //     for (BlogDown blogDown : blogDowns) {
    //         BlogView blogView = blogViewMapper.selectById(blogDown.getBlogId());
    //         blogViews.add(blogView);
    //     }
    //
    //     setOtherInfo(blogViews);
    //     return new HashMap<String, Object>() {{
    //         put("total", total);
    //         put("list", blogViews);
    //     }};
    // }
    //
    public BlogVo getBlogDetails(long id) {
        User user = (User) session.getAttribute("USER_SESSION");

        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }

        return blogMapper.getBlogDetails(id, userId);
    }
    //
    // public long postBlog(Blog blog) {
    //     System.out.println(blog.getCoverImg());
    //     User user = (User) session.getAttribute("USER_SESSION");
    //     Blog blog1 = new Blog();
    //
    //     blog1.setTitle(blog.getTitle());
    //     blog1.setDescription(blog.getDescription());
    //     blog1.setContent(blog.getContent());
    //     blog1.setAuthorId(user.getId());
    //
    //     blogMapper.insert(blog1);
    //
    //     String filename = blog1.getId() + ".jpg";
    //     String cover = null;
    //
    //     if (SaveFile.saveFile(blog.getCoverImg(), locPath + "/cover/", filename)) {
    //         cover = "/cover/" + filename;
    //     }
    //
    //     UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
    //
    //     blogMapper.update(null, wrapper.set("cover", cover).eq("id", blog1.getId()));
    //     return blog1.getId();
    // }
    //
    // public boolean deleteMyBlog(long id) {
    //     QueryWrapper<Blog> wrapper = new QueryWrapper<>();
    //     String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
    //
    //     long delete = blogMapper.delete(wrapper.
    //             eq("id", id).
    //             eq("author_username", username));
    //
    //     QueryWrapper<BlogStar> starWrapper = new QueryWrapper<>();
    //     starMapper.delete(starWrapper.eq("username", username));
    //
    //     QueryWrapper<BlogUp> upWrapper = new QueryWrapper<>();
    //     upMapper.delete(upWrapper.eq("username", username));
    //
    //     QueryWrapper<BlogDown> downWrapper = new QueryWrapper<>();
    //     downMapper.delete(downWrapper.eq("username", username));
    //
    //     QueryWrapper<Comments> commentsWrapper = new QueryWrapper<>();
    //     commentsMapper.delete(commentsWrapper.eq("author_username", username));
    //
    //     return delete == 1;
    // }
    //
    // public boolean updateMyBlog(Blog blog) {
    //     QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
    //     String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
    //
    //     Long count = blogMapper.selectCount(queryWrapper.
    //             eq("id", blog.getId()).
    //             eq("author_username", username));
    //
    //     if (count == 1) {
    //         String filename = blog.getId() + ".jpg";
    //         if (SaveFile.saveFile(blog.getCoverImg(), locPath + "/cover/", filename)) {
    //             blog.setCover("/cover/" + filename);
    //         }
    //
    //         UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
    //
    //         blogMapper.update(null, wrapper.
    //                 set("title", blog.getTitle()).
    //                 set("description", blog.getDescription()).
    //                 set("content", blog.getContent()).
    //                 set("cover", blog.getCover()).
    //                 set("status", null).
    //                 eq("id", blog.getId()).
    //                 eq("author_username", username));
    //
    //         return true;
    //     }
    //     return false;
    // }
    //
    // public boolean upBlog(long id) {
    //     UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
    //     QueryWrapper<BlogUp> queryWrapper = new QueryWrapper<>();
    //     String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
    //
    //     Blog blog = blogMapper.selectById(id);
    //     if (blog == null || blog.getStatus() == 0 || blog.getStatus() == 2) {
    //         return false;
    //     }
    //
    //     // 查询顶记录是否存在
    //     Long count = upMapper.selectCount(queryWrapper.
    //             eq("username", username).
    //             eq("blog_id", id));
    //
    //     // 顶记录不存在则添加记录
    //     if (count == 0) {
    //         System.out.println("顶记录不存在");
    //         BlogUp up = new BlogUp(id, username, false);
    //         long update = blogMapper.update(null, wrapper.
    //                 setSql("up = up + 1").
    //                 eq("id", id));
    //         long insert = upMapper.insert(up);
    //         return update == 1 && insert == 1;
    //     }
    //
    //     // 顶记录存在则逻辑删除该记录
    //     System.out.println("顶记录存在");
    //     UpdateWrapper<BlogUp> wrapper2 = new UpdateWrapper<>();
    //     long update = blogMapper.update(null, wrapper.
    //             setSql("up = up - 1").
    //             eq("id", id));
    //     long update1 = upMapper.delete(wrapper2.
    //             eq("username", username).
    //             eq("blog_id", id));
    //     return update == 1 && update1 == 1;
    // }
    //
    // public boolean downBlog(long id) {
    //     UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
    //     QueryWrapper<BlogDown> queryWrapper = new QueryWrapper<>();
    //     String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
    //
    //     Blog blog = blogMapper.selectById(id);
    //     if (blog == null || blog.getStatus() == 0 || blog.getStatus() == 2) {
    //         return false;
    //     }
    //
    //     // 查询踩记录是否存在
    //     Long count = downMapper.selectCount(queryWrapper.
    //             eq("username", username).
    //             eq("blog_id", id));
    //
    //     // 踩记录不存在则添加记录
    //     if (count == 0) {
    //         System.out.println("记录不存在");
    //         BlogDown down = new BlogDown(id, username, false);
    //         long update = blogMapper.update(null, wrapper.
    //                 setSql("down = down + 1").
    //                 eq("id", id));
    //         long insert = downMapper.insert(down);
    //         return update == 1 && insert == 1;
    //     }
    //
    //     // 踩记录存在则逻辑删除该记录
    //     System.out.println("记录存在");
    //     UpdateWrapper<BlogDown> wrapper2 = new UpdateWrapper<>();
    //     long update = blogMapper.update(null, wrapper.
    //             setSql("down = down - 1").
    //             eq("id", id));
    //     long update1 = downMapper.delete(wrapper2.
    //             eq("username", username).
    //             eq("blog_id", id));
    //     return update == 1 && update1 == 1;
    // }
    //
    // public boolean starBlog(long id) {
    //     UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
    //     QueryWrapper<BlogStar> queryWrapper = new QueryWrapper<>();
    //     String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
    //
    //     Blog blog = blogMapper.selectById(id);
    //     if (blog == null || blog.getStatus() == 0 || blog.getStatus() == 2) {
    //         return false;
    //     }
    //
    //     // 查询记录是否存在
    //     Long count = starMapper.selectCount(queryWrapper.
    //             eq("username", username).
    //             eq("blog_id", id));
    //
    //     // 记录不存在则添加记录
    //     if (count == 0) {
    //         System.out.println("记录不存在");
    //         BlogStar star = new BlogStar(id, username, false);
    //         long update = blogMapper.update(null, wrapper.
    //                 setSql("star = star + 1").
    //                 eq("id", id));
    //         long insert = starMapper.insert(star);
    //         return update == 1 && insert == 1;
    //     }
    //
    //     // 收藏记录存在则逻辑删除该记录
    //     System.out.println("记录存在");
    //     UpdateWrapper<BlogStar> wrapper2 = new UpdateWrapper<>();
    //     long update = blogMapper.update(null, wrapper.
    //             setSql("star = star - 1").
    //             eq("id", id));
    //     long update1 = starMapper.delete(wrapper2.
    //             eq("username", username).
    //             eq("blog_id", id));
    //     return update == 1 && update1 == 1;
    // }
    //
    // public Map<String, Object> getMyPostList(Integer status, long startIndex, long pageSize) {
    //     QueryWrapper<BlogView> wrapper = new QueryWrapper<>();
    //     Long userId = ((User) session.getAttribute("USER_SESSION")).getId();
    //
    //     List<BlogView> blogViews;
    //     long total;
    //     if (status != null) {
    //         total = blogViewMapper.selectCount(wrapper.
    //                 eq("author_id", userId).
    //                 eq("status", status).
    //                 orderByAsc("post_time"));
    //         blogViews = blogViewMapper.selectList(wrapper.
    //                 last("limit " + startIndex + ", " + pageSize));
    //
    //     } else {
    //         total = blogViewMapper.selectCount(wrapper.
    //                 eq("author_username", userId).
    //                 isNull("status").
    //                 orderByAsc("post_time"));
    //         blogViews = blogViewMapper.selectList(wrapper.
    //                 last("limit " + startIndex + ", " + pageSize));
    //     }
    //
    //     setOtherInfo(blogViews);
    //     return new HashMap<String, Object>() {{
    //         put("total", total);
    //         put("list", blogViews);
    //     }};
    // }

    // public Map<String, Object> getMyStarList(int startIndex, long pageSize) {
    //     HashMap<String, Object> map = new HashMap<>();
    //     Long userId = ((User) session.getAttribute("USER_SESSION")).getId();
    //
    //     map.put("username", userId);
    //     map.put("startIndex", startIndex);
    //     map.put("pageSize", pageSize);
    //
    //     QueryWrapper<BlogStar> wrapper = new QueryWrapper<>();
    //     long total = starMapper.selectCount(wrapper.eq("username", userId));
    //     List<BlogView> myStarList = blogMapper.getMyStarList(map);
    //
    //     setOtherInfo(myStarList);
    //     return new HashMap<String, Object>() {{
    //         put("total", total);
    //         put("list", myStarList);
    //     }};
    // }

    // public Map<String, Object> getMyUpList(int startIndex, long pageSize) {
    //     HashMap<String, Object> map = new HashMap<>();
    //     Long userId = ((User) session.getAttribute("USER_SESSION")).getId();
    //
    //     map.put("username", userId);
    //     map.put("startIndex", startIndex);
    //     map.put("pageSize", pageSize);
    //
    //     QueryWrapper<BlogUp> wrapper = new QueryWrapper<>();
    //     long total = upMapper.selectCount(wrapper.eq("username", userId));
    //     List<BlogView> myUpList = blogMapper.getMyUpList(map);
    //
    //     setOtherInfo(myUpList);
    //     return new HashMap<String, Object>() {{
    //         put("total", total);
    //         put("list", myUpList);
    //     }};
    // }
    //
    // public Map<String, Object> getMyDownList(int startIndex, long pageSize) {
    //     HashMap<String, Object> map = new HashMap<>();
    //     Long username = ((User) session.getAttribute("USER_SESSION")).getId();
    //
    //     map.put("username", username);
    //     map.put("startIndex", startIndex);
    //     map.put("pageSize", pageSize);
    //
    //     QueryWrapper<BlogDown> wrapper = new QueryWrapper<>();
    //     long total = downMapper.selectCount(wrapper.eq("username", username));
    //     List<BlogView> myDownList = blogMapper.getMyDownList(map);
    //
    //     setOtherInfo(myDownList);
    //     return new HashMap<String, Object>() {{
    //         put("total", total);
    //         put("list", myDownList);
    //     }};
    // }
    //
    // public void setOtherInfo(List<BlogView> blogViews) {
    //     Object userSession = session.getAttribute("USER_SESSION");
    //
    //     if (userSession == null) {
    //         System.out.println("用户未登录");
    //         for (BlogView blogView : blogViews) {
    //             User user = userMapper.selectById(blogView.getAuthorUsername());
    //             blogView.setAuthorNickname(user.getNickname());
    //         }
    //         return;
    //     }
    //     System.out.println("用户已登录");
    //     String username = ((User) userSession).getUsername();
    //
    //     Long upCount;
    //     Long downCount;
    //     Long starCount;
    //     User user;
    //     for (BlogView blogView : blogViews) {
    //         QueryWrapper<BlogUp> upWrapper = new QueryWrapper<>();
    //         QueryWrapper<BlogDown> downWrapper = new QueryWrapper<>();
    //         QueryWrapper<BlogStar> starWrapper = new QueryWrapper<>();
    //
    //         upCount = upMapper.selectCount(upWrapper.eq("blog_id", blogView.getId()).
    //                 eq("username", username));
    //         downCount = downMapper.selectCount(downWrapper.eq("blog_id", blogView.getId()).
    //                 eq("username", username));
    //         starCount = starMapper.selectCount(starWrapper.eq("blog_id", blogView.getId()).
    //                 eq("username", username));
    //
    //         user = userMapper.selectById(blogView.getAuthorUsername());
    //
    //         blogView.setAuthorNickname(user.getNickname());
    //         blogView.setIsUp(upCount == 1);
    //         blogView.setIsDown(downCount == 1);
    //         blogView.setIsStar(starCount == 1);
    //     }
    // }
}
