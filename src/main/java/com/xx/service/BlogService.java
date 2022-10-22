package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xx.mapper.*;
import com.xx.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Value("${cover-img.request-path}")
    private String reqPath;

    @Value("${cover-img.local-path}")
    private String locPath;

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

    public List<BlogView> getUserPostBlogList(String username, int startIndex, int pageSize) {
        QueryWrapper<BlogView> wrapper = new QueryWrapper<>();

        return blogViewMapper.selectList(wrapper.
                eq("author_username", username).
                eq("status", 1).
                orderByAsc("post_time").
                last("limit " + startIndex + ", " + pageSize));
    }

    public List<BlogView> getUserUpBlogList(String username, int startIndex, int pageSize) {
        QueryWrapper<BlogUp> upWrapper = new QueryWrapper<>();

        List<BlogUp> blogUps = upMapper.selectList(upWrapper.eq("username", username).last("limit " + startIndex + "," +
                " " + pageSize));
        List<BlogView> blogViews = new ArrayList<>();

        for (BlogUp blogUp : blogUps) {
            BlogView blogView = blogViewMapper.selectById(blogUp.getBlogId());
            blogViews.add(blogView);
        }

        return blogViews;
    }

    public List<BlogView> getUserDownBlogList(String username, int startIndex, int pageSize) {
        QueryWrapper<BlogDown> downWrapper = new QueryWrapper<>();

        List<BlogDown> blogDowns =
                downMapper.selectList(downWrapper.eq("username", username).last("limit " + startIndex + ", " + pageSize));
        List<BlogView> blogViews = new ArrayList<>();

        for (BlogDown blogDown : blogDowns) {
            BlogView blogView = blogViewMapper.selectById(blogDown.getBlogId());
            blogViews.add(blogView);
        }

        return blogViews;
    }

    public Blog getBlogDetails(int id) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        UpdateWrapper<Blog> updateWrapper = new UpdateWrapper<>();
        int update = blogMapper.update(null, updateWrapper.
                setSql("views = views + 1").
                eq("status", 1).
                eq("id", id));

        if (update != 0) {
            Object userSession = session.getAttribute("USER_SESSION");

            Blog blog = blogMapper.selectOne(wrapper.
                    eq("status", 1).
                    eq("id", id));

            String authorUsername = blog.getAuthorUsername();

            blog.setAuthorInfo(userService.getUserInfo(authorUsername));

            if (userSession != null) {
                String username = ((User) userSession).getUsername();
                QueryWrapper<BlogUp> upWrapper = new QueryWrapper<>();
                QueryWrapper<BlogUp> downWrapper = new QueryWrapper<>();
                QueryWrapper<BlogUp> starWrapper = new QueryWrapper<>();
                Long upCount = upMapper.selectCount(upWrapper.eq("blog_id", blog.getId()).
                        eq("username", username));
                Long downCount = upMapper.selectCount(downWrapper.eq("blog_id", blog.getId()).
                        eq("username", username));
                Long starCount = upMapper.selectCount(starWrapper.eq("blog_id", blog.getId()).
                        eq("username", username));

                blog.setIsUp(upCount == 1);
                blog.setIsDown(downCount == 1);
                blog.setIsStar(starCount == 1);
            }

            HashMap<String, List<Comments>> map = new HashMap<>();
            // 默认获取最多五条热评，最多十条新评
            List<Comments> hotComments = commentsService.getCommentsList(blog.getId(), "up", 0, 5);
            List<Comments> newComments = commentsService.getCommentsList(blog.getId(), "post_time", 0, 10);

            map.put("hotComments", hotComments);
            map.put("newComments", newComments);
            blog.setCommentsList(map);

            return blog;
        }
        return null;

    }

    public int postBlog(Blog blog) {
        System.out.println(blog.getCoverImg());
        User user = (User) session.getAttribute("USER_SESSION");
        Blog blog1 = new Blog();

        blog1.setTitle(blog.getTitle());
        blog1.setDescription(blog.getDescription());
        blog1.setContent(blog.getContent());
        blog1.setAuthorUsername(user.getUsername());

        blogMapper.insert(blog1);

        String filename = blog1.getId() + ".jpg";
        if (saveFile(blog.getCoverImg(), filename)) {
            blog1.setCover("/cover/" + filename);
        }

        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();

        blogMapper.update(null, wrapper.set("cover", "/cover/" + filename));
        return blog1.getId();
    }

    public boolean saveFile(MultipartFile file, String filename) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        File temp = new File(locPath);
        if (!temp.exists()) {
            temp.mkdirs();
        }

        File localFile = new File(locPath + filename);
        try {
            //把上传的文件保存至本地
            file.transferTo(localFile);

            System.out.println(locPath + filename);
            System.out.println(file.getOriginalFilename() + " 上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deleteMyBlog(int id) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        int delete = blogMapper.delete(wrapper.
                eq("id", id).
                eq("author_username", username));

        return delete == 1;
    }

    public boolean updateMyBlog(Blog blog) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        Long count = blogMapper.selectCount(queryWrapper.
                eq("id", blog.getId()).
                eq("author_username", username));

        if (count == 1) {
            String filename = blog.getId() + ".jpg";
            if (saveFile(blog.getCoverImg(), filename)) {
                blog.setCover("/cover/" + filename);
            }

            UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();

            blogMapper.update(null, wrapper.
                    set("title", blog.getTitle()).
                    set("description", blog.getDescription()).
                    set("content", blog.getContent()).
                    set("cover", blog.getCover()).
                    set("status", null).
                    eq("id", blog.getId()).
                    eq("author_username", username));

            return true;
        }
        return false;
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

    public List<BlogView> getMyPostList(Integer status, int startIndex, int pageSize) {
        QueryWrapper<BlogView> wrapper = new QueryWrapper<>();
        String username = ((User) session.getAttribute("USER_SESSION")).getUsername();

        if (status != null) {
            return blogViewMapper.selectList(wrapper.
                    eq("author_username", username).
                    eq("status", status).
                    orderByAsc("post_time").
                    last("limit " + startIndex + ", " + pageSize));
        }
        return blogViewMapper.selectList(wrapper.
                eq("author_username", username).
                isNull("status").
                orderByAsc("post_time").
                last("limit " + startIndex + ", " + pageSize));
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
