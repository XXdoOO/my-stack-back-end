drop database blog_management_system;

create database blog_management_system;

use blog_management_system;

create table `user`
(
    `username`      varchar(10) primary key                        not null comment '用户名',
    `password`      varchar(10)                                    not null comment '密码',
    `nickname`      varchar(6)  default '用户昵称'                     not null comment '昵称',
    `avatar`        varchar(50) default '/img/cover.ac211716.webp' not null comment '头像',
    `register_time` datetime    default current_timestamp          not null comment '注册时间',
    `disable_time`  datetime    default current_timestamp          not null comment '封号时长',
    `identity`      tinyint(1)  default 0                          not null comment '身份，0为普通用户，1为管理员',
    `logic_delete`  tinyint(1)  default 0                          not null comment '逻辑删除，1为删除'
) comment '用户账号信息';

insert into `user`(`username`, `password`, `identity`) value ('xx', 'xx', 1);



create table `blog`
(
    `id`              int auto_increment primary key       not null comment 'id',
    `title`           varchar(20)                          not null comment '标题',
    `description`     varchar(100) comment '描述',
    `content`         text                                 not null comment '内容',
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

insert into `blog`(`title`, `content`, `author_username`, `status`)
values ('我的博客1', 'my blog', 'xx', 1),
       ('我的博客2', 'my blog', 'xx', 1),
       ('我的博客3', 'my blog', 'xx', 1),
       ('我的博客4', 'my blog', 'xx', 1),
       ('我的博客5', 'my blog', 'xx', 1),
       ('我的博客6', 'my blog', 'xx', 1),
       ('我的博客7', 'my blog', 'xx', 1),
       ('我的博客8', 'my blog', 'xx', 1),
       ('我的博客9', 'my blog', 'xx', 1),
       ('我的博客11', 'my blog', 'xx', 1),
       ('我的博客12', 'my blog', 'xx', 1),
       ('我的博客13', 'my blog', 'xx', 1),
       ('我的博客14', 'my blog', 'xx', 1),
       ('我的博客15', 'my blog', 'xx', 1);



create table `blog_star`
(
    `username`     varchar(10)          not null comment '用户名',
    `blog_id`      int                  not null comment '博客id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '博客-收藏关系';

insert into `blog_star` (username, blog_id)
values ('xx', 1),
       ('xx', 2),
       ('xx', 5),
       ('xx', 6),
       ('xx', 4),
       ('xx', 10),
       ('xx', 3);



create table `blog_up`
(
    `username`     varchar(10)          not null comment '用户名',
    `blog_id`      int                  not null comment '博客id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '博客-顶关系';

insert into `blog_up` (username, blog_id)
values ('xx', 1),
       ('xx', 2),
       ('xx', 3);



create table `blog_down`
(
    `username`     varchar(10)          not null comment '用户名',
    `blog_id`      int                  not null comment '博客id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '博客-踩关系';

insert into `blog_down` (username, blog_id)
values ('xx', 1),
       ('xx', 2),
       ('xx', 3);


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

insert into `comments` (`blog_id`, `parent`, `author_username`, `content`, `up`)
values (1, null, 'xx', '写的真不错！', 2),
       (1, 1, 'xx', '写的真不错22！', 10),
       (1, null, 'xx', '写的真不错23！', 9),
       (1, 1, 'xx', '写的真不错24！', 22),
       (1, 1, 'xx', '写的真不错25！', 0),
       (1, null, 'xx', '写的真不错26！', 0),
       (1, null, 'xx', '写的真不错26！', 0),
       (1, 1, 'xx', '写的真不错26！', 0),
       (1, 1, 'xx', '写的真不错26！', 11),
       (1, null, 'xx', '写的真不错26！', 0),
       (1, 1, 'xx', '写的真不错26！', 0),
       (1, null, 'xx', '写的真不错26！', 0),
       (1, 1, 'xx', '写的真不错26！', 0),
       (1, null, 'xx', '写的真不错27！', 23),
       (2, null, 'xx', '写的真不错3！', 0);

create table `comments_up`
(
    `username`     varchar(10)          not null comment '用户名',
    `comments_id`  int                  not null comment '评论id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`comments_id`) references `comments` (`id`)
) comment '评论-顶关系';

insert into `comments_up` (username, comments_id)
values ('xx', 1),
       ('xx', 2),
       ('xx', 3);



create table `comments_down`
(
    `username`     varchar(10)          not null comment '用户名',
    `comments_id`  int                  not null comment '评论id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`comments_id`) references `comments` (`id`)
) comment '评论-踩关系';

insert into `comments_up` (username, comments_id)
values ('xx', 1),
       ('xx', 2),
       ('xx', 3);



create table `category`
(
    `name`         varchar(10) primary key not null comment '名称',
    `description`  varchar(100) comment '描述',
    `cover`        text comment '特色图片',
    `parent`       varchar(10) comment '父分类',
    `logic_delete` tinyint(1) default 0    not null comment '逻辑删除，1为删除'
) comment '分类信息';

insert into `category`(`name`, `description`, `cover`)
values ('java', 'java知识分享圣地', '/img/cover.ac211716.webp'),
       ('js', 'java知识分享圣地2', '/img/cover.ac211716.webp'),
       ('html', 'java知识分享圣地3', '/img/cover.ac211716.webp'),
       ('css', 'java知识分享圣地4', '/img/cover.ac211716.webp'),
       ('python', 'java知识分享圣地5', '/img/cover.ac211716.webp'),
       ('php', 'java知识分享圣地6', '/img/cover.ac211716.webp'),
       ('c', 'java知识分享圣地7', '/img/cover.ac211716.webp'),
       ('c++', 'java知识分享圣地8', '/img/cover.ac211716.webp'),
       ('c#', 'java知识分享圣地9', '/img/cover.ac211716.webp');



create table `category_blog`
(
    `category_name` varchar(10)          not null comment '分类名称',
    `blog_id`       int                  not null comment '博客id',
    `logic_delete`  tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`category_name`) references `category` (`name`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '分类和博客关系';

insert into `category_blog`(`category_name`, `blog_id`)
values ('java', 1),
       ('java', 2),
       ('java', 3),
       ('js', 2),
       ('js', 3),
       ('js', 4),
       ('python', 6);