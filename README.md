游客：/

| 接口地址            | 参数                                                         | SQL                                                          | 参数默认值描述                                      |
| ------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | --------------------------------------------------- |
| login               | String username、String password                             | select  * from user where username = username and password = password | 登录                                                |
| register            | String username、String password                             | insert into user value (username, password, 'xx', 'xx');     | 注册                                                |
| getBlogByKeywords   | String keywords = null、Integer startIndex = 0、Integer pageSize = 10 | select id, title, star, views, author_username, time from blog where status = 1 and logic_delete = 0 and (title like %keywords% or  content like %keywords%) limit startIndex, pageSize; | 从下标0开始获取10篇最新blog封面                     |
| **getUserBlogList** | **String username  = null、Integer status = 1、Integer startIndex = 0、Integer pageSize = 10** | **select id, title, star, views, author_username, time from blog where status= status and logic_delete = 0 and username = username limit startIndex, pageSize;** | **从下标0开始获取全部用户的10篇通过审核的blog封面** |
| getBlogDetails      | int id                                                       | select id, title, content, star, views, author_username, time comments_id from blog where status= 1 and logic_delete = 0 and id = id;<br /><br />update blog set views = views + 1 where logic_delete = 0 and status = 1 and id = id; | 获取指定blog详情，博客浏览量加一                    |
| getCommentsList     | int id, int startIndex, int pageSize                         | select id, parent_comments, sender_username, acceptor_username, content, time from comments where logic_delete = 0 and  id = id limit startIndex, pageSize; | 获取指定blog下的评论列表                            |

普通用户：/user/

| 接口地址         | 参数                          | SQL                                                          | 参数默认值描述        |
| ---------------- | ----------------------------- | ------------------------------------------------------------ | --------------------- |
| logout           |                               |                                                              | 登出                  |
| postBlog         | Blog blog                     | insert into blog(title, content, author_username) value (blog.title, blog.content, session.username); | 发布blog              |
| deleteMyBlog     | int id                        | update blog set logic_delete = 1 where author_username = session.username and logic_delete = 0 and id = id; | 删除我的blog          |
| updateMyBlog     | Blog blog                     | update blog set title = blog.title, content = blog.content, star = blog.star, views = blog.views, status = 0 where author_username = session.username and logic_delete = 0 and id = blog.id; | 更新我的blog          |
| starBlog         | int id, boolean option        | update blog set star = star + 1 where blog_id = id;<br />insert into star value(session.username, blog_id, logic_delete=option); | 收藏/取消收藏指定blog |
| getMyStarList    | int startIndex、 int pageSize | select id, title, star, views, author_username, time from blog where id in (select blog_id from star where logic_delete = 0 and username = session.username) limit startIndex, pageSize; | 获取收藏的blog列表    |
| postComments     | Comments comments             | insert into comments(parent_comments, sender_username, content ) value(comments.parent_comments, session.username, comments.acceptor_username, comments.content); | 在指定blog下评论      |
| deleteMyComments | int id                        | update comments set logic_delete = 1 where sender_username = session.username and id = id and logic_delete = 0; | 删除我的评论          |

管理员：/admin/

| 接口地址   | 参数                 | SQL                                                          | 参数默认值描述            |
| ---------- | -------------------- | ------------------------------------------------------------ | ------------------------- |
| auditBlog  | int id、int status=1 | update blog set status = status where status = null and logic_delete = 0 and id = id; | 使指定blog通过/不通过审核 |
| deleteBlog | int id               | update blog set logic_delete = 1 where logic_delete = 0 and id = id; | 删除指定的blog            |

