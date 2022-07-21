drop database blog_management_system;

create database blog_management_system;

use blog_management_system;

create table user
(
    `username` varchar(10) primary key not null comment '用户名',
    `password` varchar(10)             not null comment '密码',
    `nickname` varchar(6)              not null comment '昵称',
    `avatar`   text                    not null comment '头像',
    `identity` tinyint(1) default 0    not null comment '身份，0为普通用户，1为管理员'
);

insert into user
values ('xx', 'xx', 'xx', 'logo.png', 1);

create table blog
(
    `id`       int auto_increment primary key not null comment 'id',
    `title`    varchar(20)                    not null comment '标题',
    `content`  text                           not null comment '内容',
    `star`     int default 0                  not null comment '点赞数',
    `author`   varchar(10)                    not null comment '作者',
    `time`     datetime                       not null comment '发布时间',
    `comments` int                            not null comment '评论'
)