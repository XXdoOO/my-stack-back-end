drop database blog_management_system;

create database blog_management_system;

use blog_management_system;

create table `user`
(
    `username`      varchar(10) primary key                        not null comment '用户名',
    `password`      varchar(10)                                    not null comment '密码',
    `nickname`      varchar(20) default '用户昵称'                     not null comment '昵称',
    `avatar`        varchar(50) default '/img/cover.ac211716.webp' not null comment '头像',
    `register_time` datetime    default current_timestamp          not null comment '注册时间',
    `identity`      tinyint(1)  default 0                          not null comment '身份，0为普通用户，1为管理员',
    `logic_delete`  tinyint(1)  default 0                          not null comment '逻辑删除，1为删除'
) comment '用户账号信息';

create table `disable`
(
    `username`     varchar(10)                          not null comment '用户名',
    `start_time`   datetime   default current_timestamp not null comment '封号起始时间',
    `end_time`     datetime                             not null comment '封号结束时间',
    `reason`       varchar(100)                         not null comment '封号原因',
    `logic_delete` tinyint(1) default 0                 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`)
) comment '用户封号记录';


create table `blog`
(
    `id`              int auto_increment primary key       not null comment 'id',
    `title`           varchar(50)                          not null comment '标题',
    `description`     varchar(200) comment '描述',
    `cover`           text comment '特色图片',
    `content`         mediumtext                           not null comment '内容',
    `up`              int        default 0                 not null comment '顶',
    `down`            int        default 0                 not null comment '踩',
    `star`            int        default 0                 not null comment '收藏数',
    `views`           int        default 0                 not null comment '浏览量',
    `author_username` varchar(10)                          not null comment '作者用户名',
    `post_time`       datetime   default current_timestamp not null comment '发布时间',
    `status`          tinyint(1) comment '发布状态，null为审核中，0为未通过审核，1为通过审核',
    `logic_delete`    tinyint(1) default 0                 not null comment '逻辑删除，1为删除，即审核不通过',
    foreign key (`author_username`) references `user` (`username`)
) comment '博客信息';
# 审核中               status = null and logic_delete = 0;
# 审核通过              status = 1 and logic_delete = 0;
# 审核不通过            status = 0 and logic_delete = 0;
# 删除的审核中的博客       status = null and logic_delete = 1;
# 删除的审核通过的博客     status = 1 and logic_delete = 1;
# 删除的未审核通过的博客    status = 0 and logic_delete = 1;

create table `blog_star`
(
    `username`     varchar(10)          not null comment '用户名',
    `blog_id`      int                  not null comment '博客id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '博客-收藏关系';

create table `blog_up`
(
    `username`     varchar(10)          not null comment '用户名',
    `blog_id`      int                  not null comment '博客id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '博客-顶关系';

create table `blog_down`
(
    `username`     varchar(10)          not null comment '用户名',
    `blog_id`      int                  not null comment '博客id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '博客-踩关系';

create table `comments`
(
    `id`              int auto_increment primary key       not null comment 'id',
    `blog_id`         int                                  not null comment '所属博客id',
    `parent`          int comment '父级评论，null则为一级评论，!null则为二级评论',
    `author_username` varchar(10)                          not null comment '评论者的username',
    `content`         varchar(100)                         not null comment '评论内容',
    `up`              int        default 0                 not null comment '顶',
    `down`            int        default 0                 not null comment '踩',
    `post_time`       datetime   default current_timestamp not null comment '发布时间',
    `logic_delete`    tinyint(1) default 0                 not null comment '逻辑删除，1为删除',
    foreign key (`blog_id`) references `blog` (`id`),
    foreign key (`author_username`) references `user` (`username`)
) comment '评论信息';

create table `comments_up`
(
    `username`     varchar(10)          not null comment '用户名',
    `comments_id`  int                  not null comment '评论id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`comments_id`) references `comments` (`id`)
) comment '评论-顶关系';

create table `comments_down`
(
    `username`     varchar(10)          not null comment '用户名',
    `comments_id`  int                  not null comment '评论id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`comments_id`) references `comments` (`id`)
) comment '评论-踩关系';

insert into user(username, password, nickname, register_time, identity, logic_delete)
values ('admin', 'xx', '我是管理员', '2022-11-09 21:59:56', 1, 0),
       ('a', 'xx', '该用户已注销', '2022-12-09 21:59:56', 0, 1),
       ('b', 'xx', '夹狗屎', '2022-01-09 21:59:56', 0, 0),
       ('c', 'xx', '夕颜╰', '2002-11-09 21:59:56', 0, 1),
       ('d', 'xx', '不上清华不改名', '2022-11-09 22:59:56', 0, 0),
       ('e', 'xx', '卖女孩的小火柴 ɞ', '2022-11-09 21:19:56', 1, 0),
       ('f', 'xx', '4万人', '2022-11-09 21:59:56', 0, 0),
       ('g', 'xx', '一枚小黑子', '2022-11-09 21:53:56', 0, 1),
       ('h', 'xx', '超级无敌暴龙战士', '2022-11-09 21:59:16', 0, 1),
       ('i', 'xx', '韭菜哥', '2022-11-09 21:29:56', 0, 0),
       ('j', 'xx', '努力学习', '2012-11-09 21:32:56', 0, 0),
       ('k', 'xx', '春风十里', '2019-11-09 21:22:56', 0, 0),
       ('l', 'xx', '花开富贵', '2017-11-09 01:59:56', 0, 0),
       ('n', 'xx', '未命名', '2010-11-09 11:59:56', 1, 0),
       ('m', 'xx', '剩蛋老人', '2019-11-09 23:59:56', 0, 0),
       ('o', 'xx', '程序猿jj', '2019-11-09 22:59:56', 0, 0),
       ('p', 'xx', '千与千寻', '2017-11-09 12:59:56', 0, 0),
       ('q', 'xx', '用户232424', '2014-11-09 20:59:56', 0, 0),
       ('r', 'xx', '听我说谢谢你', '2013-11-09 11:59:56', 0, 0),
       ('s', 'xx', '床前明月光', '2012-11-09 15:59:56', 1, 0),
       ('t', 'xx', '疑是地上霜', '1222-01-09 15:59:56', 0, 1),
       ('u', 'xx', '举头望明月', '1022-08-09 15:59:56', 0, 0),
       ('v', 'xx', '低头思故乡', '1902-02-19 15:59:56', 0, 0),
       ('w', 'xx', '李白', '2021-10-09 05:59:56', 0, 0),
       ('x', 'xx', '芭比Q', '2002-01-19 10:59:56', 0, 0);

insert into disable(username, end_time, reason)
values ('c', '2023-01-19 10:59:56', '发布不当评论'),
       ('f', '2023-01-19 10:59:56', '对他人进行人身攻击'),
       ('x', '2023-01-19 10:59:56', '对他人进行人身攻击'),
       ('o', '2023-01-19 10:59:56', '对网站恶意攻击');
