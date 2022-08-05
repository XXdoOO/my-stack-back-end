drop database blog_management_system;

create database blog_management_system;

use blog_management_system;

create table `user`
(
    `username` varchar(10) primary key not null comment '用户名',
    `password` varchar(10)             not null comment '密码',
    `nickname` varchar(6)              not null comment '昵称',
    `avatar`   text                    not null comment '头像',
    `identity` tinyint(1) default 0    not null comment '身份，0为普通用户，1为管理员'
) comment '用户账号信息';


create table `blog`
(
    `id`              int auto_increment primary key       not null comment 'id',
    `title`           varchar(20)                          not null comment '标题',
    `content`         text                                 not null comment '内容',
    `star`            int        default 0                 not null comment '点赞数',
    `views`           int        default 0                 not null comment '浏览量',
    `author_username` varchar(10)                          not null comment '作者用户名',
    `time`            datetime   default current_timestamp not null comment '发布时间',
    `status`          tinyint(1) comment '发布状态，null为审核中，0为未通过审核，1为通过审核',
    `logic_delete`    tinyint(1) default 0                 not null comment '逻辑删除，1为删除，即审核不通过'
) comment '博客信息';
# 审核中               status = null and logic_delete = 0;
# 审核通过              status = 1 and logic_delete = 0;
# 审核不通过            status = 0 and logic_delete = 0;
# 删除的审核中的博客       status = null and logic_delete = 1;
# 删除的审核通过的博客     status = 1 and logic_delete = 1;
# 删除的未审核通过的博客    status = 0 and logic_delete = 1;


create table `star`
(
    `username`     varchar(10)          not null comment '用户名',
    `blog_id`      int                  not null comment '博客id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '收藏关系';

create table `comments`
(
    `id`              int auto_increment primary key       not null comment 'id',
    `blog_id`         int                                  not null comment '所属博客id',
    `parent_comments` int comment '所属父级评论，null则为一级评论，!null则为二级评论',
    `sender_username` varchar(10)                          not null comment '发送方username',
    `content`         varchar(100)                         not null comment '评论内容',
    `time`            datetime   default current_timestamp not null comment '发送时间',
    `logic_delete`    tinyint(1) default 0                 not null comment '逻辑删除，1为删除',
    foreign key (`blog_id`) references `blog` (`id`)
) comment '评论信息';