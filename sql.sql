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

insert into `user`
values ('xx', 'xx', 'xx', 'logo.png', 1);

create table `blog`
(
    `id`              int auto_increment primary key not null comment 'id',
    `title`           varchar(20)                    not null comment '标题',
    `content`         text                           not null comment '内容',
    `star`            int default 0                  not null comment '点赞数',
    `views`           int default 0                  not null comment '浏览量',
    `author_username` varchar(10)                    not null comment '作者用户名',
    `time`            datetime                       not null comment '发布时间',
    `comments_id`     int                            not null comment '评论id'
) comment '博客信息';

create table `star`
(
    `username` varchar(10) not null comment '用户名',
    `blog_id`  int         not null comment '博客id',
    foreign key (`username`) references `user` (`username`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '收藏关系';

create table `comments`
(
    `id`                int auto_increment primary key not null comment 'id',
    `sender_username`   varchar(10)                    not null comment '发送方username',
    `acceptor_username` varchar(10)                    not null comment '接受方username',
    `content`           varchar(100)                   not null comment '评论内容',
    `time`              datetime                       not null comment '发送时间'
) comment '评论信息';