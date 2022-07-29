游客：/

| 接口地址          | 参数                                                         | SQL                                                          | 参数默认值描述                                  |
| ----------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ----------------------------------------------- |
| login             | String username、String password                             | select  * from user where username = username and password = password | 登录                                            |
| register          | String username、String password                             | insert into user value (username, password, 'xx', 'xx');     | 注册                                            |
| getBlogByKeywords | String keywords=null、Integer startIndex=0、Integer pageSize=10 | select id, title, star, views, author_username, time from blog where logic_post = flag and logic_delete = 0 and like %keywords% limit startIndex, pageSize; | 从下标0开始获取10篇最新blog封面                 |
| getBlogList       | String username、Integer flag=1、Integer startIndex=0、Integer pageSize=10 | select id, title, star, views, author_username, time from blog where logic_post = flag and logic_delete = 0 and username = username limit startIndex, pageSize; | 从下标0开始获取指定用户的10篇通过审核的blog封面 |
| getBlogDetails    | int id                                                       | select id, title, content, star, views, author_username, time comments_id from blog where id = id; | 获取指定blog详情                                |
| getComments       | int id                                                       | select * from comments where id = id and logic_delete = 0;   | 获取指定blog下的评论                            |
|                   |                                                              |                                                              |                                                 |

普通用户：/user/

| 接口地址       | 参数                    | SQL                                                          | 参数默认值描述   |
| -------------- | ----------------------- | ------------------------------------------------------------ | ---------------- |
| logout         |                         |                                                              | 登出             |
| postBlog       | Blog blog               | insert into blog(title, content, author_username) value (blog.title, blog.content, session.username); | 发布blog         |
| deleteBlog     | int id                  | update blog set logic_delete = 1 where author_username = session.username and logic_delete = 0 and id = id; | 删除指定blog     |
| updateBlog     | Blog blog               | update blog set title = blog.title, content = blog.content, star = blog.star, views = blog.views where author_username = session.username and logic_delete = 0 and id = blog.id; | 更新指定blog     |
| starBlog       | int id                  | update blog set star = star + 1 where blog = id;             | 收藏指定blog     |
| postComments   | int id、String comments |                                                              | 在指定blog下评论 |
| deleteComments | int id                  |                                                              | 删除指定评论     |

管理员：/admin/

| 接口地址     | 参数                                      | SQL                                                          | 参数默认值描述                    |
| ------------ | ----------------------------------------- | ------------------------------------------------------------ | --------------------------------- |
| getAuditList | Integer startIndex=0、Integer pageSize=10 | select id, title, star, views, author_username, time from blog where logic_post = null and logic_delete = 0; | 从下标0开始获取10篇需要审核的blog |
| auditBlog    | int id、int flag=1                        | update blog set logic_post = flag where id = id;             | 使指定blog通过审核                |
| deleteBlog   | int id                                    | update blog set logic_delete = 1 where id = id;              | 删除指定的blog                    |

