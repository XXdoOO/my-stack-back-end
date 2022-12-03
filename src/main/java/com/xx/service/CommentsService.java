// package com.xx.service;
//
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
// import com.xx.mapper.CommentsDownMapper;
// import com.xx.mapper.CommentsMapper;
// import com.xx.mapper.CommentsUpMapper;
// import com.xx.mapper.UserMapper;
// import com.xx.pojo.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// import javax.servlet.http.HttpSession;
// import java.util.HashMap;
// import java.util.List;
//
// @Service
// public class CommentsService {
//
//     @Autowired
//     private CommentsMapper commentsMapper;
//
//     @Autowired
//     private CommentsUpMapper commentsUpMapper;
//
//     @Autowired
//     private CommentsDownMapper commentsDownMapper;
//
//     @Autowired
//     private UserMapper userMapper;
//
//     @Autowired
//     private HttpSession session;
//
//     public List<Comments> getCommentsList(long id, String orderBy, int startIndex, int pageSize) {
//         QueryWrapper<Comments> wrapper = new QueryWrapper<>();
//
//         List<Comments> comments = commentsMapper.selectList(wrapper.
//                 eq("blog_id", id).
//                 isNull("parent").
//                 orderByDesc(orderBy).
//                 last("limit " + startIndex + ", " + pageSize));
//
//         QueryWrapper<Comments> commentsWrapper = new QueryWrapper<>();
//
//         setCommentsInfo(comments);
//         for (Comments comment : comments) {
//             List<Comments> children = commentsMapper.selectList(commentsWrapper.eq("parent", comment.getId()));
//             setCommentsInfo(children);
//
//             comment.setChildren(children);
//         }
//
//
//         return comments;
//     }
//
//     public void setCommentsInfo(List<Comments> comments) {
//         Object userSession = session.getAttribute("USER_SESSION");
//
//         for (Comments comment : comments) {
//             if (comment.getReceiveUsername() != null) {
//                 comment.setReceiveNickname(userMapper.selectById(comment.getReceiveUsername()).getNickname());
//             }
//
//             if (userSession == null) {
//                 User user = userMapper.selectById(comment.getSenderUsername());
//
//                 HashMap<String, Object> map = new HashMap<>();
//                 map.put("avatar", user.getAvatar());
//                 map.put("nickname", user.getNickname());
//                 comment.setAuthorInfo(map);
//
//             } else {
//                 QueryWrapper<CommentsUp> commentsUpWrapper = new QueryWrapper<>();
//                 QueryWrapper<CommentsDown> commentsDownWrapper = new QueryWrapper<>();
//                 String username = ((User) userSession).getUsername();
//
//                 User user = userMapper.selectById(comment.getSenderUsername());
//                 Long upCount = commentsUpMapper.selectCount(commentsUpWrapper.
//                         eq("username", username).
//                         eq("comments_id", comment.getId()));
//                 Long downCount = commentsDownMapper.selectCount(commentsDownWrapper.
//                         eq("username", username).
//                         eq("comments_id", comment.getId()));
//
//                 HashMap<String, Object> map = new HashMap<>();
//                 map.put("avatar", user.getAvatar());
//                 map.put("nickname", user.getNickname());
//                 comment.setAuthorInfo(map);
//                 comment.setIsUp(upCount != 0);
//                 comment.setIsDown(downCount != 0);
//             }
//         }
//     }
//
//
//     public Comments postComments(Comments comments) {
//         User user = (User) session.getAttribute("USER_SESSION");
//         Comments comments1 = new Comments();
//
//         comments1.setBlogId(comments.getBlogId());
//         comments1.setParent(comments.getParent());
//         comments1.setSenderUsername(user.getUsername());
//         comments1.setReceiveUsername(comments.getReceiveUsername());
//         comments1.setContent(comments.getContent());
//
//         commentsMapper.insert(comments1);
//
//         Comments comment = commentsMapper.selectById(comments1.getId());
//         HashMap<String, Object> map = new HashMap<>();
//         map.put("avatar", user.getAvatar());
//         map.put("nickname", user.getNickname());
//         comment.setAuthorInfo(map);
//         return comment;
//     }
//
//     public int deleteMyComments(long id) {
//         String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
//         UpdateWrapper<Comments> wrapper = new UpdateWrapper<>();
//
//         return commentsMapper.delete(wrapper.
//                 eq("author_username", username).
//                 eq("id", id));
//     }
//
//     public boolean upComments(long commentsId) {
//         UpdateWrapper<Comments> wrapper = new UpdateWrapper<>();
//         QueryWrapper<CommentsUp> queryWrapper = new QueryWrapper<>();
//         String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
//
//         // 查询顶记录是否存在
//         Long count = commentsUpMapper.selectCount(queryWrapper.
//                 eq("username", username).
//                 eq("comments_id", commentsId));
//
//         // 顶记录不存在则添加记录
//         if (count == 0) {
//             System.out.println("顶记录不存在");
//             CommentsUp up = new CommentsUp(commentsId, username, false);
//             int update = commentsMapper.update(null, wrapper.setSql("up = up + 1").eq("id", commentsId));
//             int insert = commentsUpMapper.insert(up);
//             return update == 1 && insert == 1;
//         }
//
//         // 顶记录存在则逻辑删除该记录
//         System.out.println("顶记录存在");
//         UpdateWrapper<CommentsUp> wrapper2 = new UpdateWrapper<>();
//         int update = commentsMapper.update(null, wrapper.setSql("up = up - 1").eq("id", commentsId));
//         int update1 = commentsUpMapper.delete(wrapper2.eq("username", username).eq("comments_id", commentsId));
//         return update == 1 && update1 == 1;
//     }
//
//     public boolean downComments(long commentsId) {
//         UpdateWrapper<Comments> wrapper = new UpdateWrapper<>();
//         QueryWrapper<CommentsDown> queryWrapper = new QueryWrapper<>();
//         String username = ((User) session.getAttribute("USER_SESSION")).getUsername();
//
//         // 查询顶记录是否存在
//         Long count = commentsDownMapper.selectCount(queryWrapper.
//                 eq("username", username).
//                 eq("comments_id", commentsId));
//
//         // 记录不存在则添加记录
//         if (count == 0) {
//             System.out.println("踩记录不存在");
//             CommentsDown up = new CommentsDown(commentsId, username, false);
//             int update = commentsMapper.update(null, wrapper.setSql("down = down + 1").eq("id", commentsId));
//             int insert = commentsDownMapper.insert(up);
//             return update == 1 && insert == 1;
//         }
//
//         // 记录存在则逻辑删除该记录
//         System.out.println("踩记录存在");
//         UpdateWrapper<CommentsDown> wrapper2 = new UpdateWrapper<>();
//         int update = commentsMapper.update(null, wrapper.setSql("down = down - 1").eq("id", commentsId));
//         int update1 = commentsDownMapper.delete(wrapper2.eq("username", username).eq("comments_id", commentsId));
//         return update == 1 && update1 == 1;
//     }
// }
