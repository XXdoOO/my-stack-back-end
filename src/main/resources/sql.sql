drop
    database blog_management_system;

create
    database blog_management_system;

use
    blog_management_system;

create table `user`
(
    `id`          bigint unsigned auto_increment primary key                                not null comment 'id',
    `email`       varchar(32)                                                               not null comment '邮箱',
    `password`    varchar(16)                                                               not null comment '密码',
    `nickname`    varchar(32)         default '用户昵称'                                        not null comment '昵称',
    `avatar`      varchar(64)         default '/avatar/4.jpg'                               not null comment '头像',
    `is_admin`    tinyint(1) unsigned default 0                                             not null comment '身份，0为普通用户，1为管理员',
    `is_disable`  tinyint(1) unsigned default 0                                             not null comment '状态，0为正常，1为异常',
    `update_time` datetime            default current_timestamp on update current_timestamp not null comment '更新时间',
    `create_time` datetime            default current_timestamp                             not null comment '创建时间',
    `is_deleted`  tinyint(1) unsigned default 0                                             not null comment '逻辑删除，1为删除'
) comment '用户账号信息';

create table `disable`
(
    `id`          bigint unsigned auto_increment primary key                                not null comment 'id',
    `user_id`     bigint unsigned                                                           not null comment '用户名',
    `end_time`    datetime                                                                  not null comment '封号结束时间',
    `reason`      varchar(128)                                                              not null comment '封号原因',
    `update_time` datetime            default current_timestamp on update current_timestamp not null comment '更新时间',
    `create_time` datetime            default current_timestamp                             not null comment '创建时间',
    `is_deleted`  tinyint(1) unsigned default 0                                             not null comment '逻辑删除，1为删除',
    foreign key (`user_id`) references `user` (`id`)
) comment '用户封号记录';


create table `blog`
(
    `id`          bigint unsigned auto_increment primary key                                not null comment 'id',
    `title`       varchar(64)                                                               not null comment '标题',
    `description` varchar(256) comment '描述',
    `cover`       text comment '特色图片',
    `content`     mediumtext                                                                not null comment '内容',
    `up`          bigint unsigned     default 0                                             not null comment '顶',
    `down`        bigint unsigned     default 0                                             not null comment '踩',
    `star`        bigint unsigned     default 0                                             not null comment '收藏数',
    `views`       bigint unsigned     default 0                                             not null comment '浏览量',
    `author_id`   bigint unsigned                                                           not null comment '作者用户名',
    `status`      tinyint(2) unsigned default 0 comment '发布状态，0为审核中，1为通过审核，2为未通过审核',
    `update_time` datetime            default current_timestamp on update current_timestamp not null comment '更新时间',
    `create_time` datetime            default current_timestamp                             not null comment '创建时间',
    `is_deleted`  tinyint(1) unsigned default 0                                             not null comment '逻辑删除，1为删除',
    foreign key (`author_id`) references `user` (`id`)
) comment '博客信息';

create table `comment`
(
    `id`          bigint unsigned auto_increment primary key                                not null comment 'id',
    `blog_id`     bigint unsigned                                                           not null comment '所属博客id',
    `parent`      bigint unsigned comment '父级评论，null则为一级评论，!null则为二级评论',
    `sender_id`   bigint unsigned                                                           not null comment '评论者的id',
    `receive_id`  bigint unsigned comment '接收者的id，为null则回复的是根评论',
    `content`     varchar(128)                                                              not null comment '评论内容',
    `up`          bigint unsigned     default 0                                             not null comment '顶',
    `down`        bigint unsigned     default 0                                             not null comment '踩',
    `update_time` datetime            default current_timestamp on update current_timestamp not null comment '更新时间',
    `create_time` datetime            default current_timestamp                             not null comment '创建时间',
    `is_deleted`  tinyint(1) unsigned default 0                                             not null comment '逻辑删除，1为删除',
    foreign key (`blog_id`) references `blog` (`id`),
    foreign key (`sender_id`) references `user` (`id`),
    foreign key (`receive_id`) references `user` (`id`)
) comment '评论信息';

create table `star`
(
    `id`          bigint unsigned auto_increment primary key                                not null comment 'id',
    `user_id`     bigint unsigned                                                           not null comment '用户名',
    `blog_id`     bigint unsigned                                                           not null comment '博客id',
    `update_time` datetime            default current_timestamp on update current_timestamp not null comment '更新时间',
    `create_time` datetime            default current_timestamp                             not null comment '创建时间',
    `is_deleted`  tinyint(1) unsigned default 0                                             not null comment '逻辑删除，1为删除',
    foreign key (`user_id`) references `user` (`id`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '收藏关系';

create table `up`
(
    `id`          bigint unsigned auto_increment primary key                                not null comment 'id',
    `user_id`     bigint unsigned                                                           not null comment '用户名',
    `blog_id`     bigint unsigned                                                           not null comment '博客id',
    `comment_id`  bigint unsigned                                                           not null comment '评论id',
    `update_time` datetime            default current_timestamp on update current_timestamp not null comment '更新时间',
    `create_time` datetime            default current_timestamp                             not null comment '创建时间',
    `is_deleted`  tinyint(1) unsigned default 0                                             not null comment '逻辑删除，1为删除',
    foreign key (`user_id`) references `user` (`id`),
    foreign key (`blog_id`) references `blog` (`id`),
    foreign key (`comment_id`) references `comment` (`id`)
) comment '顶关系';

create table `down`
(
    `id`          bigint unsigned auto_increment primary key                                not null comment 'id',
    `user_id`     bigint unsigned                                                           not null comment '用户名',
    `blog_id`     bigint unsigned                                                           not null comment '博客id',
    `comment_id`  bigint unsigned                                                           not null comment '博客id',
    `update_time` datetime            default current_timestamp on update current_timestamp not null comment '更新时间',
    `create_time` datetime            default current_timestamp                             not null comment '创建时间',
    `is_deleted`  tinyint(1) unsigned default 0                                             not null comment '逻辑删除，1为删除',
    foreign key (`user_id`) references `user` (`id`),
    foreign key (`blog_id`) references `blog` (`id`),
    foreign key (`comment_id`) references `comment` (`id`)
) comment '踩关系';

create table category
(
    `id`          bigint unsigned auto_increment primary key                                not null comment 'id',
    `name`        varchar(16)                                                               not null comment '名称',
    `update_time` datetime            default current_timestamp on update current_timestamp not null comment '更新时间',
    `create_time` datetime            default current_timestamp                             not null comment '创建时间',
    `is_deleted`  tinyint(1) unsigned default 0                                             not null
) comment '分类';

create table blog_category
(
    `id`          bigint unsigned auto_increment primary key                                not null comment 'id',
    `blog_id`     bigint unsigned                                                           not null comment '博客id',
    `category_id` bigint unsigned                                                           not null comment '分类id',
    `update_time` datetime            default current_timestamp on update current_timestamp not null comment '更新时间',
    `create_time` datetime            default current_timestamp                             not null comment '创建时间',
    `is_deleted`  tinyint(1) unsigned default 0                                             not null,
    foreign key (`category_id`) references `category` (`id`)

) comment '博客和分类关系';

insert into category(`name`)
values ('java'),
       ('javascript'),
       ('vue'),
       ('css'),
       ('html'),
       ('typescript'),
       ('jquery'),
       ('springboot'),
       ('mybatisplus'),
       ('python');

insert into user(email, password, nickname, create_time, is_admin, is_deleted)
values ('1972524359@qq.com', 'xx', '我是管理员', '2022-11-09 21:59:56', 1, 0),
       ('1172524359@qq.com', 'xx', '该用户已注销', '2022-12-09 21:59:56', 0, 1),
       ('1272524359@qq.com', 'xx', '夹狗屎', '2022-01-09 21:59:56', 0, 0),
       ('1372524359@qq.com', 'xx', '夕颜╰', '2002-11-09 21:59:56', 0, 1),
       ('1472524359@qq.com', 'xx', '不上清华不改名', '2022-11-09 22:59:56', 0, 0),
       ('1572524359@qq.com', 'xx', '卖女孩的小火柴 ɞ', '2022-11-09 21:19:56', 1, 0),
       ('1672524359@qq.com', 'xx', '4万人', '2022-11-09 21:59:56', 0, 0),
       ('1772524359@qq.com', 'xx', '一枚小黑子', '2022-11-09 21:53:56', 0, 1),
       ('1872524359@qq.com', 'xx', '超级无敌暴龙战士', '2022-11-09 21:59:16', 0, 1),
       ('1972524359@qq.com', 'xx', '韭菜哥', '2022-11-09 21:29:56', 0, 0),
       ('1112524359@qq.com', 'xx', '努力学习', '2012-11-09 21:32:56', 0, 0),
       ('1122524359@qq.com', 'xx', '春风十里', '2019-11-09 21:22:56', 0, 0),
       ('1132524359@qq.com', 'xx', '花开富贵', '2017-11-09 01:59:56', 0, 0),
       ('1142524359@qq.com', 'xx', '未命名', '2010-11-09 11:59:56', 1, 0),
       ('1152524359@qq.com', 'xx', '剩蛋老人', '2019-11-09 23:59:56', 0, 0),
       ('1912524359@qq.com', 'xx', '程序猿jj', '2019-11-09 22:59:56', 0, 0),
       ('1922524359@qq.com', 'xx', '千与千寻', '2017-11-09 12:59:56', 0, 0),
       ('1932524359@qq.com', 'xx', '用户232424', '2014-11-09 20:59:56', 0, 0),
       ('1942524359@qq.com', 'xx', '听我说谢谢你', '2013-11-09 11:59:56', 0, 0),
       ('1952524359@qq.com', 'xx', '床前明月光', '2012-11-09 15:59:56', 1, 0),
       ('1962524359@qq.com', 'xx', '疑是地上霜', '1222-01-09 15:59:56', 0, 1),
       ('1972524359@qq.com', 'xx', '举头望明月', '1022-08-09 15:59:56', 0, 0),
       ('1982524359@qq.com', 'xx', '低头思故乡', '1902-02-19 15:59:56', 0, 0),
       ('1992524359@qq.com', 'xx', '李白', '2021-10-09 05:59:56', 0, 0),
       ('1971524359@qq.com', 'xx', '芭比Q', '2002-01-19 10:59:56', 0, 0);

